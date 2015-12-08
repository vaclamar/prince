package princeGame;

import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.PickUp;
import cz.yellen.xpg.common.action.Use;
import cz.yellen.xpg.common.action.Wait;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;

import java.util.HashMap;
import java.util.Map;

import static cz.yellen.xpg.common.action.Direction.*;
import java.util.List;
import princeGame.gameobjects.*;

public class MyGameStrategy implements GameStrategy {

    private Prince prince;
    private List<WarningGameObject> gameObjects;
    private Action action;
    private GameSituation gameSituation;

    private Map<Integer, GameObject> saveJumpForward = new HashMap<>();
    private Map<Integer, GameObject> saveJumpBackward = new HashMap<>();

    public boolean isSaveJump(Integer id) {
        if (prince.getDirection() == FORWARD) {
            return saveJumpForward.containsKey(id);
        } else {
            return saveJumpBackward.containsKey(id);
        }
    }

    @Override
    public Action step(GameSituation situation) {

        this.gameSituation = situation;
        action = null;
        gameObjects = Factory.createList(situation.getGameObjects());
        prince = findPrince();

        if (prince.getHealth() == 1) {
            return new Use(prince.getFirstFullBottle().getGameObject(), prince.getGameObject());
        }

        if (gameObjects.size() == 1) {
            return move();
        } else {
            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Gate) {
                    action = actionForGate((Gate)gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Sword) {
                    action = actionForSword((Sword)gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Guard) {
                    Guard guard = (Guard)gameObject;
                    if(guard.isLive()) {
                        action = actionForGuard(guard);
                        if (action == null) {
                            step(gameSituation);
                        } else {
                            return action;
                        }
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Bottle) {
                    action = actionForBottle((Bottle)gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Chopper) {
                    action = actionForChopper(new Chopper(gameObject));
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Pit) {
                    action = actionForPit((Pit)gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Tile) {
                    action = actionForTile((Tile)gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Portcullis) {
                    Portcullis portcullis = (Portcullis)gameObject;
                    action = actionForPortcullis(portcullis);
                    if (action == null) {
                        if (!portcullis.isOpened()) {
                            return step(situation);
                        } else {
                            continue;
                        }

                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Wall) {
                    action = actionForWall((Wall)gameObject);
                    if (action == null) {
                        return step(situation);
                    } else {
                        return action;
                    }
                }
            }
        }

        return move();
    }

    // ACTIONS FOR OBJECTS

    private Action actionForSword(Sword sword) {
        if (prince.isOnSameField(sword)) {
            return pickUp(sword.getGameObject());
        }
        return null;
    }

    private Action actionForBottle(Bottle bottle) {
        if (prince.isOnSameField(bottle) && !bottle.isPuke()) {
            return pickUp(bottle.getGameObject());
        }
        return null;
    }

    private Action actionForGuard(Guard guard) {
        if (prince.isBefore(guard)) {
                if (prince.hasSword() && prince.isStrongerWithBottles(guard)) {
                    return attack(guard.getGameObject());
                } else {
                    prince.changeDirection();
                    return null;
                }
        }
        prince.changeDirection();
        return null;
    }

    private Action actionForPit(Pit pit) {
        if (prince.isBefore(pit)) {
            return jump();
        }
        return null;
    }

    private Action actionForChopper(Chopper chopper) {
        if (prince.isBefore(chopper)) {
            if (chopper.isOpened()) {
                return jump();
            } else {
                return waitAction();
            }
        }
        return null;
    }

    private Action actionForGate(Gate gate) {
        if (prince.isOnSameField(gate)) {
            if (gate.isOpened()) {
                return enterGate(gate);
            }
        }
        return null;
    }

    private Action actionForTile(Tile tile) {
        if (prince.isBefore(tile)) {
            if (Math.random() * 3 < 1) {
                if (isSaveJump(tile.getId())) {
                    return jump();
                }
            } else {
                return null;
            }
        }
        if (prince.isOnSameField(tile)) {
            saveJumpBackward.put(tile.getId(), tile);
            saveJumpForward.put(tile.getId(), tile);
            for (GameObject gameObject : gameObjects) {
                switch (gameObject.getType()) {
                    case "guard":
                        if (!((Guard)gameObject).isLive()) {
                            break;
                        }
                    case "pit":
                    case "chopper":
                        if (prince.isNextForward(gameObject)) {
                            saveJumpForward.remove(gameObject.getId());
                        } else if (prince.isNextBackward(gameObject)) {
                            saveJumpBackward.remove(gameObject.getId());
                        }
                }
            }
        }
        return null;
    }

    private Action actionForPortcullis(Portcullis portcullis) {
        if (!portcullis.isOpened()) {
            prince.changeDirection();
            return null;
        }
        return null;
    }

    private Action actionForWall(Wall wall) {
        if (prince.isBefore(wall)) {
            prince.changeDirection();
        }
        return null;
    }


    private Prince findPrince() {
        for (WarningGameObject gameObject : gameObjects) {
            if (gameObject instanceof Prince) {
                if(prince == null) {
                    return (Prince)gameObject;
                } else {
                    prince.setGameObject(gameObject.getGameObject());
                    return prince;
                }
            }
        }
        return null;
    }

    // PRINCE ACTIONS
    private Action enterGate(GameObject gameObject) {
        return new Enter(gameObject);
    }

    private Action move() {
        return new Move(prince.getDirection());
    }

    private Action jump() {
        return new Jump(prince.getDirection());
    }

    private Action waitAction() {
        return new Wait();
    }

    private Action attack(GameObject enemy) {
        return new Use(prince.getSword().getGameObject(), enemy);
    }

    private Action pickUp(GameObject item) {
        return new PickUp(item);
    }
}
