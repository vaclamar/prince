package vm;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 12:32
 * To change this template use File | Settings | File Templates.
 */
public class Game {
    int position = 0;
    Map<Integer,GameObject> map = new HashMap<>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Map<Integer, GameObject> getMap() {
        return map;
    }

    public void setMap(Map<Integer, GameObject> map) {
        this.map = map;
    }
}
