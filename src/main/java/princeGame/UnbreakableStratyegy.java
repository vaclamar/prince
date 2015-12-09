package princeGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import throwable.prince.util.GameSituationPredicates;

public class UnbreakableStratyegy implements GameStrategy {

	private static final String GUARD = "guard";
	private static final int JUMP_LENGTH = 2;
	private static final String PIT = "pit";
	private static final String GATE = "gate";
	private static final String WALL = "wall";
	private static final String PRINCE = "prince";
	private static final String PRINCESS = "princess";
	private static final String BOTTLE = "bottle";
	private static final String CHOPPER = "chopper";
	private static final String TILE = "tile";
	private static final String PORTCULLIS = "portcullis";

	private static Map<String, Integer> positions = new HashMap<>();

	Direction actualDirection = Direction.BACKWARD;

	int startPosition = 0;
	int actualPosition = 0;
	Action actualAction = move();
	boolean stopProcessing = false;

	GameObject prince;
	GameObject princess;
	GameObject wall;
	GameObject gate;
	List<GameObject> pits;
	List<GameObject> bottles;
	List<GameObject> guards;
	List<GameObject> choppers;
	List<GameObject> tiles;
	List<GameObject> portcullises;
	private GameObject sword;
	private static Set<Integer> porticullisSet = new HashSet<>();
	private static Integer lastporticullisId = null;

	Set<GameObject> gameObjects = new HashSet<GameObject>();

	Map<Integer, GameObject> gameMap = new HashMap<Integer, GameObject>();

	Map<String, String> tillActionsMemory = new HashMap<>();

	public UnbreakableStratyegy() {
	}

	public Action step(GameSituation situation) {

		actualAction = move();

		lookAround(situation);

		// If the gate is visible

		if (gateHandler())
			return actualAction;

		if ( princessHandler())
			return actualAction;
		
		
		if (tileHandler())
			return actualAction;

		if (wallHandler())
			return actualAction;

		if (pitHandler())
			return actualAction;

		if (guardHandler())
			return actualAction;

		if (swordHandler())
			return actualAction;

		if (chopperHandler())
			return actualAction;

		if (portcullisHandler())
			return actualAction;

		if (bottleHandler())
			return actualAction;

		return actualAction;
	}

	private boolean gateHandler() {
		if (gate != null) { // We can see the gate so we move towards the gate

			System.out.println("gate " + gate.getPosition());
			System.out.println("prince " + prince.getPosition());
			System.out.println("dir " + actualDirection);
			String opened = gate.getProperty("opened");
			// opened != null && "true".equals(opened)
			if (gate.getPosition() == prince.getPosition()) {
				actualAction = new Enter(gate);
				return true;
			}

			List<GameObject> tmpList = new ArrayList<>();
			tmpList.add(gate);
			GameObject tmpGate = getObstackleInDirectionOfMove(tmpList);

			if (tmpGate != null && opened != null && "true".equals(opened)) {
				actualAction = move();
				return true;
			} else if (tmpGate != null) {
				switchActualDirection();
				actualAction = move();
				return true;
			}
		}

		// Action retAction = null;
		// if ( isObjectOnMyPosition())
		// {
		// retAction = new Enter(getOrgObject());
		// } //&& isOpened() getProperty("opened").equals("true")
		// else if (isObjectBeforePosition() &&
		// getProperty("opened").equals("true")) {
		// retAction = new Move(getContext().getCurrentDirection());
		//
		// } else if (isObjectBeforePosition() && isClosed()) {
		// getContext().changeDirection();
		// retAction = new Move(getContext().getCurrentDirection());
		// }

		return false;
	}

	private boolean bottleHandler() {
		Integer health = Integer.valueOf(prince.getProperty("health"));
		if (bottles != null) {
			for (GameObject bottle : bottles) {
				if (bottle.getPosition() == prince.getPosition()) {
					actualAction = new PickUp(bottle);
				}
			}
		}

		if (health < 5) {
			// FIXME bootels from prince
			for (GameObject obj : prince.getStuff()) {
				if (obj.getProperty("odour") != null && obj.getProperty("odour").equals("mint")
						&& !obj.getProperty("volume").equals("0")) {
					actualAction = new Use(obj, prince);
				}

			}
		}
		return false;
	}

	/**
	 * If we see wall and we headed towards it, prince changes direction.
	 * <p>
	 * After direction change prince is obstacle aware.
	 */
	private boolean wallHandler() {
		if (wall != null) {
			List<GameObject> tmpList = new ArrayList<GameObject>();
			tmpList.add(wall);
			GameObject tmpWall = getObstackleInDirectionOfMove(tmpList);
			if (tmpWall != null) {
				switchActualDirection();
			}
		}
		return false;
	}



	private boolean princessHandler() {
			// warning! Pit spotted // the pit handler :D
			if (princess != null) {
				List<GameObject> list = new ArrayList<>();
				list.add(princess);
				// we are interested in the actual direction located obstacle
				GameObject princess = getObstackleInDirectionOfMove(list);
				if (princess != null) {
					actualAction = new Use(prince, princess);
					return true;
				}
			}
			return false;
	}	
	
	
	/**
	 * If prince sees a pit TODO
	 */
	private boolean pitHandler() {
		// warning! Pit spotted // the pit handler :D
		if (pits != null) {
			// we are interested in the actual direction located obstacle
			GameObject pit = getObstackleInDirectionOfMove(pits);
			if (pit != null) {
				actualAction = jump();
			}

			// // jump over it
			// if (pit != null && prince.getPosition() < pit.getPosition()) {
			// if (actualDirection == Direction.FORWARD) {
			// actualAction = jump();
			// } else {
			// actualAction = move();
			// System.out.println("mmmmmmmmmmmmmmmmmmmmmove");
			// }
			// } else { // pit on the left
			// if (actualDirection == Direction.BACKWARD) {
			// actualAction = jump();
			// } else {
			// actualAction = move();
			// System.out.println("mmmmmmmmmmmmmmmmmmmmmove");
			// }
			// }
		}
		return false;
	}

	private boolean tileHandler() {
		// TODO Auto-generated method stub
		GameObject tile = getObstackleInDirectionOfMove(tiles);
		boolean b = false;
		if (tile != null) {

			String pictureKey = "KEY " + tile.getId() + actualDirection;
			String picture = makePicture(pictureKey, gameObjects);
			String memoryKey = picture;
			String lastActionForPicture = tillActionsMemory.get(memoryKey);
			if (lastActionForPicture != null) {
				if ("jump".equals(lastActionForPicture)) {
					actualAction = move();
					tillActionsMemory.put(memoryKey, "move");
				} else {
					actualAction = jump();
					tillActionsMemory.put(memoryKey, "jump");
				}
				System.out.println(
						"YES ! .... using memory ...................................................................");
			} else // null
			{
				// default action jump
				actualAction = jump();
				tillActionsMemory.put(memoryKey, "jump");
			}

			// Random random = new Random();
			// boolean randBool = random.nextBoolean();
			// if (randBool)
			// actualAction = jump();
			// else
			// actualAction = move();

			b = true;
		}
		return b;
	}

	private String makePicture(String pictureKey, Set<GameObject> gameObjects) {
		StringBuilder picture = new StringBuilder();
		picture.append(pictureKey);
		for (GameObject go : gameObjects) {
			picture.append(" ID:" + go.getId() + " POS:" + go.getPosition());
			for (Map.Entry<String, String> entry : go.getProperties().entrySet()) {
				picture.append(" PROPKEY:" + entry.getKey() + " PROPVAL" + entry.getValue());
			}
		}
		return picture.toString();
	}

	private boolean portcullisHandler() {
		if (portcullises != null) {
			GameObject portcullise = getObstackleInDirectionOfMove(portcullises);
			if (portcullise != null && portcullise.getProperty("closed").equals("true")) {
				switchActualDirection();

			}
			actualAction = move();

			// if (isObjectBeforePosition() &&
			// getProperty("closed").equals("true")) {
			// getContext().changeDirection();
			// retAction = new Move(getContext().getCurrentDirection());
			// } else if (isObjectAnywhereBefore()) {
			// retAction = new Move(getContext().getCurrentDirection());
			// }
			// return (retAction == null) ? null: new ActionRet(retAction,
			// true);
		}
		return true;
	}

	private boolean chopperHandler() {
		// TODO Auto-generated method stub
		if (choppers == null)
			return false;

		// GameObject chopper = getChopperInDirectionOfMove(choppers);
		GameObject chopper = getObstackleInDirectionOfMove(choppers);
		if (chopper != null && chopper.getProperty("opening").equals("true")) {
			actualAction = new Jump(actualDirection);
		} else if (chopper != null) {
			actualAction = new Wait();
		}
		return false;
	}

	private boolean guardHandler() {

		if (guards == null)
			return false;

		GameObject guard = getObstackleInDirectionOfMove(guards);
		GameObject sword = GameSituationPredicates.getObject("sword", prince.getStuff());
		if (guard != null && !isGuardDead(guard)) {
			if (sword != null) {
				actualAction = new Use(sword, guard);
			} else {
				switchActualDirection();
				actualAction = new Move(actualDirection);
			}
		}
		return false;
	}

	private boolean swordHandler() {
		if (sword != null) { // We can see the gate so we move towards the gate
			if (sword.getPosition() == prince.getPosition()) {
				actualAction = new PickUp(sword);
			} else if (sword.getPosition() < prince.getPosition()) {
				actualAction = new Move(Direction.BACKWARD);
			} else if (sword.getPosition() > prince.getPosition()) {
				actualAction = new Move(Direction.FORWARD);
			}
		}
		return false;
	}

	// FIXME
	private GameObject getObstackleInDirectionOfMove(List<GameObject> obstackles) {
	
		if ( obstackles == null) return null;
		int position = prince.getPosition();
		if (actualDirection == Direction.FORWARD) {
			for (GameObject obstacle : obstackles) {
				if (obstacle.getPosition() == position + 1) {
					return obstacle;
				}
			}
		} else { // BACK
			for (GameObject obstacle : obstackles) {
				if (obstacle.getPosition() == position - 1) {
					return obstacle;
				}
			}
		}
		return null;
	}

	private Action move() {
		if (actualDirection == Direction.BACKWARD) {
			actualPosition--;

		} else {
			actualPosition++;
		}

		// if (tiles != null) {
		// for (GameObject tile : tiles) {
		// int position = prince.getPosition();
		// if (visitedTile == null || visitedTile.isEmpty())
		// return new Move(actualDirection);
		// Integer currid = tile.getId();
		// Integer visited = visitedTile.get(currid);
		// if (visited == null)
		// return new Move(actualDirection);
		//
		// if (tile.getPosition() > position && visited.intValue() > 2)
		// return new Jump(Direction.BACKWARD);
		// else if (tile.getPosition() < position && visited.intValue() > 2) {
		// return new Jump(Direction.FORWARD);
		// }
		// }
		// }
		return new Move(actualDirection);
	}

	private Jump jump() {
		// adjust actual position
		if (actualDirection == Direction.BACKWARD) {
			actualPosition = actualPosition - JUMP_LENGTH;
		} else {
			actualPosition = actualPosition + JUMP_LENGTH;
		}
		return new Jump(actualDirection);
	}

	private void switchActualDirection() {
		if (actualDirection == Direction.FORWARD) {
			actualDirection = Direction.BACKWARD;
		} else {
			actualDirection = Direction.FORWARD;
		}

		System.out.println("Switched direction to " + actualDirection);
	}

	private void lookAround(GameSituation situation) {
		gameObjects = situation.getGameObjects();
		// get prince
		prince = GameSituationPredicates.getObject(PRINCE, gameObjects);
		// get wall
		wall = GameSituationPredicates.getObject(WALL, gameObjects);
		// find the gate
		gate = GameSituationPredicates.getObject(GATE, gameObjects);
		// find pit
		pits = GameSituationPredicates.getObjects(PIT, gameObjects);

		guards = GameSituationPredicates.getObjects(GUARD, gameObjects);

		sword = GameSituationPredicates.getObject("sword", gameObjects);

		bottles = GameSituationPredicates.getObjects(BOTTLE, gameObjects);

		choppers = GameSituationPredicates.getObjects(CHOPPER, gameObjects);

		tiles = GameSituationPredicates.getObjects(TILE, gameObjects);

		portcullises = GameSituationPredicates.getObjects(PORTCULLIS, gameObjects);

		princess = GameSituationPredicates.getObject(PRINCESS, gameObjects);
	}

	private boolean isGuardDead(GameObject guard) {
		return guard.getProperties().get("dead").equals("true");
	}

	// class PortcullisHelper
	// {
	// Integer id;
	// //<id tile, >
	// Map<Integer, Integer> tiles = new HashMap<>();
	//
	// public PortcullisHelper(Integer idPort, Integer idTile) {
	// // TODO Auto-generated constructor stub
	// }
	//
	// public Integer getId() {
	// return id;
	// }
	//
	// private putTail()
	// {
	//
	// }
	//
	// }

}
