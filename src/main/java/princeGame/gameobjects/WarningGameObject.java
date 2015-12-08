/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
abstract public class WarningGameObject {

    private GameObject gameObject;

    protected WarningGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * @return the gameObject
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    public int getId() {
        return gameObject.getId();
    }

    abstract public String getType();

    public String getProperty(String string) {
        return gameObject.getProperty(string);
    }

    public Map<String, String> getProperties() {
        return gameObject.getProperties();
    }

    public int getPosition() {
        return gameObject.getPosition();
    }

    public Set<GameObject> getStuff() {
        return gameObject.getStuff();
    }

    /**
     * @param gameObject the gameObject to set
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

}
