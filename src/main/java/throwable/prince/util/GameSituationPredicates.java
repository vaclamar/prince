package throwable.prince.util;

import static java.lang.System.out;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import cz.yellen.xpg.common.stuff.GameObject;

public class GameSituationPredicates {

	private static final String GATE = "gate";

	public static void main(String[] args) {

		List<String> names = asList("Ted", "gate", "Jed", "Ned");
		out.println(names);
		List<String> shortNames = new ArrayList<String>();
		shortNames.addAll(names);
		CollectionUtils.filter(shortNames, new Predicate<String>() {
			public boolean evaluate(String input) {
				return  input.length() < 4;
			}
		});
		out.println(shortNames.size());
		for (String s : shortNames)
			out.println(s);
	}
	
	public static boolean isGate(Set<GameObject> objects) {
		CollectionUtils.filter(objects, new Predicate<GameObject>() {
			public boolean evaluate(GameObject gameObject) {
				String gameObjectType = gameObject.getType();
				if (gameObjectType != null) {
					gameObjectType = gameObjectType.trim();
					if (gameObjectType.equalsIgnoreCase(GATE)) {
						return true;
					} 
				}
				return false;
			}
		}
		);
		return false;
	}
	
	public static GameObject getObject(String type, Set<GameObject> objects) {
		for (GameObject gameObject : objects) {
			if (gameObject != null && gameObject.getType().equalsIgnoreCase(type)) {
				return gameObject;
			}
		}
		return null;
	}
	
	public static List<GameObject> getObjects(String type, Set<GameObject> objects) {
		List<GameObject> result = new ArrayList<GameObject>();
		for (GameObject gameObject : objects) {
			if (gameObject != null && gameObject.getType().equalsIgnoreCase(type)) {
				result.add(gameObject);
			}
		}
		return result.size() > 0 ? result : null;
	}

}
