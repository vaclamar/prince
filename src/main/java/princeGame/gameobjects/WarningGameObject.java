/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
 public class WarningGameObject {

    private GameObject gameObject;
    private List<WarningGameObject> warningStuff;

    protected WarningGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
        warningStuff = Factory.createList(gameObject.getStuff());
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

    public String getType() {
        return gameObject.getType();
    }

    public String getProperty(String string) {
        return gameObject.getProperty(string);
    }

    public Map<String, String> getProperties() {
        return gameObject.getProperties();
    }

    public int getPosition() {
        return gameObject.getPosition();
    }

    public List<WarningGameObject> getStuff() {
        return warningStuff;
    }

    /**
     * @param gameObject the gameObject to set
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public boolean isDanger() {
        return false;
    }

}
