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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static cz.yellen.xpg.common.action.Direction.*;
import static java.lang.Integer.parseInt;

public class MyGameStrategy implements GameStrategy {

    private GameObject prince;
    private GameObject sword;
    private Set<GameObject> gameObjects;
    private Action action;
    private Direction direction = FORWARD;
    private GameObject inventoryBottle;
    private GameSituation gameSituation;

    private Map<Integer, GameObject> saveJumpForward = new HashMap<>();
    private Map<Integer, GameObject> saveJumpBackward = new HashMap<>();

    public boolean isSaveJump(Integer id) {
        if (direction == FORWARD) {
            return saveJumpForward.containsKey(id);
        } else {
            return saveJumpBackward.containsKey(id);
        }
    }

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
                if (isBefore(gameObject) && gameObject.getType().equals("guard") && gameObject.getProperties().get("dead").equals("false")) {
                    action = actionForGuard(gameObject);
                    if (action == null) {
                        step(gameSituation);
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
                if (gameObject.getType().equals("tile")) {
                    action = actionForTile(gameObject);
                    if (action == null) {
                        continue;
                    } else {
                        return action;
                    }
                }
            }

            for (GameObject gameObject : gameObjects) {
                if (isBefore(gameObject) && gameObject.getType().equals("portcullis")) {
                    action = actionForPortcullis(gameObject);
                    if (action == null) {

                        if (gameObject.getProperty("opened").equals("false")) {
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
                if (isBefore(gameObject) && gameObject.getType().equals("wall")) {
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
        if (isBefore(guard)) {
            for (GameObject stuff : prince.getStuff()) {
                int volume = 0;
                if (inventoryBottle != null) {
                    volume = parseInt(inventoryBottle.getProperty("volume"));
                }
                if (stuff.getType().equals("sword") &&
                        parseInt(guard.getProperty("health")) < parseInt(prince.getProperty("health")) + volume - 1) {
                    sword = stuff;
                    return attack(guard);
                } else {
                    changeDirection();
                    return null;
                }
            }
        }
        changeDirection();
        return null;
    }

    private Action actionForPit(GameObject gameObject) {
        if (isBefore(gameObject)) {
            return jump();
        }
        return null;
    }

    private Action actionForChopper(GameObject chopper) {
        if (isBefore(chopper)) {
            if (chopper.getProperty("opening").equals("true") && !chopper.getProperty("closing").equals("true")) {
                return jump();
            } else {
                return waitAction();
            }
        }
        return null;
    }

    private Action actionForGate(GameObject gate) {
        if (gate.getPosition() == prince.getPosition()) {
            if (gate.getProperty("opened").equals("true") && !gate.getProperty("closed").equals("true")) {
                return enterGate(gate);
            }
        }
        return null;
    }

    private Action actionForTile(GameObject tile) {
        if (isBefore(tile)) {
            if (Math.random() * 2 < 1) {
                if (isSaveJump(tile.getId())) {
                    return jump();
                }
            } else {
                return null;
            }
        }
        if (tile.getPosition() == prince.getPosition()) {
            saveJumpBackward.put(tile.getId(), tile);
            saveJumpForward.put(tile.getId(), tile);
            for (GameObject gameObject : gameObjects) {


                switch (gameObject.getType()) {
                    case "guard":
                        if (gameObject.getProperty("dead").equals("true")) {
                            break;
                        }
                    case "pit":
                    case "chopper":
                        if (gameObject.getPosition() > prince.getPosition()) {
                            saveJumpForward.remove(gameObject);
                        } else {
                            saveJumpBackward.remove(gameObject);
                        }
                }
            }
        }
        return null;
    }

    private Action actionForPortcullis(GameObject portcullis) {
        if (portcullis.getProperty("opened").equals("false")) {
            changeDirection();
            return null;
        } else if (portcullis.getProperty("opened").equals("true")) {
            return null;
        }
        return null;
    }

    private Action actionForWall(GameObject gameObject) {
        if (isBefore(gameObject)) {
            changeDirection();
        }
        return null;
    }

    boolean isBefore(GameObject gameObject) {
        return (direction == BACKWARD && gameObject.getPosition() < prince.getPosition()) || (direction == FORWARD && gameObject.getPosition() > prince.getPosition());
    }

    private GameObject findPrince() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.getType().equals("prince")) {
                if (gameObject.getStuff() != null) {
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
        if (direction == FORWARD) {
            direction = BACKWARD;
        } else {
            direction = FORWARD;
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
