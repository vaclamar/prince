/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        warningStuff = Factory.createList(gameObject.getStuff(), new ArrayList<Tile>());
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
         warningStuff = Factory.createList(gameObject.getStuff(),new ArrayList<Tile>());
    }

    public boolean isDanger() {
        return false;
    }

    public boolean isStepInto() {
        return true;
    }

    public boolean isJumpOver() {
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WarningGameObject other = (WarningGameObject) obj;
        if (this.gameObject.getId() != other.gameObject.getId()) {
            return false;
        }
        return true;
    }


}
