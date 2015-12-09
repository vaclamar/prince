package princeGame;

import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.PickUp;
import cz.yellen.xpg.common.action.Use;
import cz.yellen.xpg.common.action.Wait;

import cz.yellen.xpg.common.stuff.GameSituation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import princeGame.gameobjects.*;

public class MyGameStrategy implements GameStrategy {

    private Prince prince;
    private List<WarningGameObject> gameObjects;
    private List<WarningGameObject> portcullis;
    private Action action;
    private GameSituation gameSituation;
    private List<WarningGameObject> portcullises = new ArrayList<>();
    final private List<WarningGameObject> oldPortcullises = new ArrayList<>();
    final private List<Tile> closerTiles = new ArrayList<>();

    public boolean isSaveJump() {
        List<WarningGameObject> jumpableGameObjects = getJumpableGameObjects();
        boolean jump = false;
         if(jumpableGameObjects == null || jumpableGameObjects.isEmpty()) {
             jump = true;
         }
         List<WarningGameObject> moveableGameObjects = getMoveableGameObjects();
         if(moveableGameObjects == null || moveableGameObjects.isEmpty()) {
             return true;
         }
         return  jump || moveableGameObjects.stream().anyMatch(wgo -> wgo.isJumpOver()) &&
                 jumpableGameObjects.stream().anyMatch(wgo -> wgo.isStepInto());
    }

        public boolean isSaveMove() {
         List<WarningGameObject> moveableGameObjects = getMoveableGameObjects();
         if(moveableGameObjects == null || moveableGameObjects.isEmpty()) {
             return true;
         }
         Guard g = getGuard();
         boolean gb = true;
         if(g != null) {
             gb = prince.canFigh(g);
         }
         return moveableGameObjects.stream().anyMatch(wgo -> wgo.isStepInto()) && gb;
    }

     public Guard getGuard() {
         List<WarningGameObject> ret = getJumpableGameObjects().stream().filter(wgo -> wgo instanceof Guard).collect(Collectors.toList());
         if(ret != null && !ret.isEmpty()) {
             return (Guard)ret.get(0);
         }
         return null;
     }

    public List<WarningGameObject> getJumpableGameObjects() {
        return gameObjects.stream().filter(wgo -> wgo.getPosition() == prince.getJumpFieldPosition()).collect(Collectors.toList());
    }

    public List<WarningGameObject> getMoveableGameObjects() {
        return gameObjects.stream().filter(wgo -> wgo.getPosition() == prince.getMoveFieldPosition()).collect(Collectors.toList());
    }

    public List<WarningGameObject> findPortcullis() {
        return gameObjects.stream().filter(wgo -> wgo instanceof Portcullis).collect(Collectors.toList());
    }

    public List<Portcullis> findClosedPortcullis() {
        List<Portcullis> ret = new ArrayList();
        for(WarningGameObject wgo : oldPortcullises) {
            Portcullis old = (Portcullis)wgo;
            for(WarningGameObject nwgo : portcullises) {
                Portcullis n = (Portcullis)nwgo;
                if(old.equals(n) && old.isOpened() && !n.isOpened()) {
                    ret.add(n);
                }
            }
        }
        return ret;
    }

    @Override
    public Action step(GameSituation situation) {

        this.gameSituation = situation;
        action = null;
        gameObjects = Factory.createList(situation.getGameObjects(), closerTiles);
        prince = findPrince();

        if (prince.getHealth() == 1 && prince.hasFullBottle()) {
            return new Use(prince.getFirstFullBottle().getGameObject(), prince.getGameObject());
        }

        oldPortcullises.clear();
        oldPortcullises.addAll(portcullises);
        portcullises = findPortcullis();

        if (gameObjects.size() == 1) {
            return move();
        } else {
            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Princess) {
                    action = actionForPrincess((Princess) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Gate) {
                    action = actionForGate((Gate) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Sword) {
                    action = actionForSword((Sword) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Guard) {
                    Guard guard = (Guard) gameObject;
                    if (guard.isLive()) {
                        action = actionForGuard(guard);
                        if (action == null) {
                            step(gameSituation);
                        } else {
                            return action;
                        }
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Bottle) {
                    action = actionForBottle((Bottle) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Chopper) {
                    action = actionForChopper((Chopper) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Pit) {
                    action = actionForPit((Pit) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (gameObject instanceof Tile) {
                    action = actionForTile((Tile) gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Portcullis) {
                    Portcullis portcullis2 = (Portcullis) gameObject;
                    action = actionForPortcullis(portcullis2);
                    if (action == null) {
                        if (!portcullis2.isOpened()) {
                            return step(situation);
                        } else {
                            continue;
                        }

                    } else {
                        return action;
                    }
                }
            }

            for (WarningGameObject gameObject : gameObjects) {
                if (prince.isBefore(gameObject) && gameObject instanceof Wall) {
                    action = actionForWall((Wall) gameObject);
                    if (action == null) {
                        return step(situation);
                    } else {
                        return action;
                    }
                }
            }
        }

        action = move();
        if (action == null) {
             return step(situation);
        } else {
            return action;
        }
    }

    // ACTIONS FOR OBJECTS
    private Action actionForPrincess(Princess princess) {
        if (prince.isBefore(princess)) {
            return usePrincess(princess);
        }
        return null;
    }

    private Action actionForSword(Sword sword) {
        if (prince.isOnSameField(sword)) {
            return pickUp(sword);
        }
        return null;
    }

    private Action actionForBottle(Bottle bottle) {
        if (prince.isOnSameField(bottle) && !bottle.isPuke()) {
            return pickUp(bottle);
        }
        return null;
    }

    private Action actionForGuard(Guard guard) {
        if (prince.isBefore(guard)) {
            if (prince.canFigh(guard)) {
                return attack(guard);
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
            if (chopper.isJumpOver()) {
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
        if(prince.isOnSameField(tile)) {
            if (!findClosedPortcullis().isEmpty()) {
                closerTiles.add(tile);
            }
        }

        if (prince.isBefore(tile)) {
            if(tile.isCloser() != null && tile.isCloser()) {
                if(isSaveJump()) {
                    return jump();
                } else {
                    return null;
                }
            }

            if (Math.random() * 3 < 1) {
                if (isSaveJump()) {
                    return jump();
                }
            } else {
                return null;
            }
        }
//        if (prince.isOnSameField(tile)) {
//            for (WarningGameObject gameObject : gameObjects) {
//                if (gameObject.isDanger()) {
//                    if (prince.isNextForward(gameObject)) {
//                        saveJumpForward.remove(gameObject.getId());
//                    } else if (prince.isNextBackward(gameObject)) {
//                        saveJumpBackward.remove(gameObject.getId());
//                    }
//                }
//            }
//        }
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
                if (prince == null) {
                    return (Prince) gameObject;
                } else {
                    prince.setGameObject(gameObject.getGameObject());
                    return prince;
                }
            }
        }
        return null;
    }

    // PRINCE ACTIONS
    private Action enterGate(WarningGameObject gameObject) {
        return new Enter(gameObject.getGameObject());
    }

    private Action move() {
        if(isSaveMove()) {
        return new Move(prince.getDirection());
        } else {
            prince.changeDirection();
            return null;
        }
    }

    private Action jump() {
        if(isSaveJump()) {
            return new Jump(prince.getDirection());
        } else {
            return move();
        }
        
    }

    private Action waitAction() {
        return new Wait();
    }

    private Action attack(WarningGameObject enemy) {
        return new Use(prince.getSword().getGameObject(), enemy.getGameObject());
    }

    private Action usePrincess(Princess princess) {
        return new Use(prince.getGameObject(), princess.getGameObject());
    }

    private Action pickUp(WarningGameObject item) {
        return new PickUp(item.getGameObject());
    }
}
