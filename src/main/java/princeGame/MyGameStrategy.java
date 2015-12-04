package princeGame;

import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Direction;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.PickUp;
import cz.yellen.xpg.common.action.Use;
import cz.yellen.xpg.common.action.Wait;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;

import java.util.Set;

import static java.lang.Integer.parseInt;

public class MyGameStrategy implements GameStrategy {

    private GameObject prince;
    private GameObject sword;
    private Set<GameObject> gameObjects;
    private Action action;
    private Direction direction = Direction.FORWARD;
    private GameObject inventoryBottle;
    private GameSituation gameSituation;

    public Action step(GameSituation situation) {

        this.gameSituation = situation;
        action = null;
        gameObjects = situation.getGameObjects();
        prince = findPrince();
        if (prince.getProperty("health").equals("1")) {
            return new Use(inventoryBottle, prince);
        }

        if (gameObjects.size() == 1) {
            return move();
        } else {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("gate")) {
                    action = actionForGate(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("sword")) {
                    action = actionForSword(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("bottle")) {
                    action = actionForBottle(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("guard") && gameObject.getProperties().get("dead").equals("false")) {
                    if (parseInt(prince.getProperties().get("health")) > parseInt(gameObject.getProperties().get("health"))) {
                        action = actionForGuard(gameObject);
                    } else {
                        changeDirection();
                        return step(situation);
                    }

                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("chopper")) {
                    action = actionForChopper(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("pit")) {
                    action = actionForPit(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

             for (GameObject gameObject : gameObjects) {
                if (gameObject.getType().equals("wall")) {
                    action = actionForWall(gameObject);
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

    private Action actionForSword(GameObject gameObject) {
        if (gameObject.getPosition() == prince.getPosition()) {
            return pickUp(gameObject);
        }
        return null;
    }

    private Action actionForBottle(GameObject gameObject) {
        if (gameObject.getPosition() == prince.getPosition() && !gameObject.getProperty("odour").equals("puke")) {
            return pickUp(gameObject);
        }
        return null;
    }

    private Action actionForGuard(GameObject guard) {
        if (direction == Direction.FORWARD && guard.getPosition() == prince.getPosition() + 1) {
            for (GameObject stuff : prince.getStuff()) {
                if (stuff.getType().equals("sword") &&
                        parseInt(guard.getProperty("health")) < parseInt(prince.getProperty("health")) + parseInt(inventoryBottle.getProperty("volume")) - 1) {
                    sword = stuff;
                    return attack(guard);
                } else {
                    changeDirection();
                    return null;
                }
            }
        } else if (direction == Direction.BACKWARD && guard.getPosition() == prince.getPosition() - 1) {
            for (GameObject stuff : prince.getStuff()) {
                if (stuff.getType().equals("sword")) {
                    sword = stuff;
                    return attack(guard);
                } else {
                    changeDirection();
                    return null;
                }
            }
        }

        return null;
    }

    private Action actionForPit(GameObject gameObject) {
        if (direction == Direction.FORWARD && gameObject.getPosition() == prince.getPosition() + 1) {
            return jump();
        } else if (direction == Direction.BACKWARD && gameObject.getPosition() == prince.getPosition() - 1) {
            return jump();
        }
        return null;
    }

    private Action actionForChopper(GameObject chopper) {
        if (chopper.getProperty("opening").equals("true") && !chopper.getProperty("closing").equals("true")) {
            if (direction == Direction.FORWARD && chopper.getPosition() == prince.getPosition() + 1) {
                return jump();
            } else if (direction == Direction.BACKWARD && chopper.getPosition() == prince.getPosition() - 1) {
                return jump();
            }
        } else {
            return waitAction();
        }
        return null;
    }

    private Action actionForGate(GameObject gameObject) {
        if (gameObject.getPosition() == prince.getPosition()) {
            return enterGate(gameObject);
        } else {
            return null;
        }
    }

    private Action actionForWall(GameObject gameObject) {
        if (direction == Direction.FORWARD && gameObject.getPosition() == prince.getPosition() + 1) {
            changeDirection();
        } else if (direction == Direction.BACKWARD && gameObject.getPosition() == prince.getPosition() - 1) {
            changeDirection();
        }
        return null;
    }

    private GameObject findPrince() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getType().equals("prince")) {
                if(gameObject.getStuff()!=null){
                    for (GameObject staff : gameObject.getStuff()) {
                        if (staff.getType().equals("bottle")) {
                            inventoryBottle = staff;
                        }
                    }
                }
                return gameObject;
            }
        }
        return null;
    }

    private void changeDirection() {
        if (direction == Direction.FORWARD) {
            direction = Direction.BACKWARD;
        } else {
            direction = Direction.FORWARD;
        }
    }

    // PRINCE ACTIONS
    private Action enterGate(GameObject gameObject) {
        return new Enter(gameObject);
    }

    private Action move() {
        return new Move(direction);
    }

    private Action jump() {
        return new Jump(direction);
    }

    private Action waitAction() {
        return new Wait();
    }

    private Action attack(GameObject enemy) {
        return new Use(sword, enemy);
    }

    private Action pickUp(GameObject item) {
        return new PickUp(item);
    }
}
