package princeGame;

import java.util.Set;

import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Direction;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.PickUp;
import cz.yellen.xpg.common.action.Use;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;

public class MyGameStrategy implements GameStrategy {

	private GameObject prince;
	private GameObject sword;
	private Set<GameObject> gameObjects;
	private Action action;
	private Direction direction = Direction.FORWARD;

	public Action step(GameSituation situation) {

		action = null;
		gameObjects = situation.getGameObjects();
		prince = findPrince();

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
				if (gameObject.getType().equals("guard") && gameObject.getProperties().get("dead").equals("false")) {
					if(Integer.parseInt(prince.getProperties().get("health")) > Integer.parseInt(gameObject.getProperties().get("health"))){
						action = actionForGuard(gameObject);
					} else {
						changeDirection();
					}
						
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
						continue;
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

	private Action actionForGuard(GameObject gameObject) {
		if (direction == Direction.FORWARD && gameObject.getPosition() == prince.getPosition() + 1) {
			for (GameObject stuff : prince.getStuff()) {
				if (stuff.getType().equals("sword")) {
					sword = stuff;
					return attack(gameObject);
				} else {
					changeDirection();
					return null;
				}
			}
		}else if (direction == Direction.BACKWARD && gameObject.getPosition() == prince.getPosition() - 1) {
			for (GameObject stuff : prince.getStuff()) {
				if (stuff.getType().equals("sword")) {
					sword = stuff;
					return attack(gameObject);
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
	
	private Action attack(GameObject enemy) {
		return new Use(sword, enemy);
	}
	
	private Action pickUp(GameObject item){
		return new PickUp(item);
	}
}
