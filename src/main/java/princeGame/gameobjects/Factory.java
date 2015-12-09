/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class Factory {

    public static WarningGameObject create(GameObject gameObject, List<Tile> closedTiles) {
        switch (gameObject.getType()) {
            case "bottle":
                return new Bottle(gameObject);
            case "chopper":
                return new Chopper(gameObject);
            case "gate":
                return new Gate(gameObject);
            case "guard":
                return new Guard(gameObject);
            case "pit":
                return new Pit(gameObject);
            case "portcullis":
                return new Portcullis(gameObject);
            case "prince":
                return new Prince(gameObject);
            case "sword":
                return new Sword(gameObject);
            case "tile":
                Tile tile = new Tile(gameObject);
                if(closedTiles.contains(tile)) {
                    tile.setCloser(Boolean.TRUE);
                }
                return tile;
            case "wall":
                return new Wall(gameObject);
            case "princess":
                return new Princess(gameObject);
            default:
                System.err.println("Unexpected type " + gameObject.getType());
                return new WarningGameObject(gameObject);
        }
    }

    public static List<WarningGameObject> createList(Set<GameObject> gameObjects, List<Tile> closedTiles) {
        List<WarningGameObject> ret = new ArrayList<>();
        for (GameObject go : gameObjects) {
            ret.add(create(go, closedTiles));
        }
        return ret;
    }
}
