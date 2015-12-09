/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.action.Direction;
import static cz.yellen.xpg.common.action.Direction.*;
import cz.yellen.xpg.common.stuff.GameObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class Prince extends WarningGameObject implements IDeadable {

    private Direction direction = FORWARD;

    public Prince(GameObject prince) {
        super(prince);
    }

    @Override
    public int getHealth() {
        return Integer.parseInt(this.getGameObject().getProperty("health"));
    }

    @Override
    public boolean isLive() {
        return !Boolean.parseBoolean(this.getGameObject().getProperty("dead"));
    }

    public boolean canFigh(IDeadable gameObject) {
        //TODO sum of bottle's volume should be used
        return hasSword() && (this.getHealth() + (hasFullBottle()? getFirstFullBottle().getVolume() : 0) > gameObject.getHealth());
    }

    public boolean isBefore(WarningGameObject gameObject) {
        return (getDirection() == BACKWARD && gameObject.getPosition() == this.getPosition() - 1) || (getDirection() == FORWARD && gameObject.getPosition() == this.getPosition() + 1);
    }

    public boolean isOnSameField(WarningGameObject gameObject) {
        return gameObject.getPosition() == this.getPosition();
    }

    public void changeDirection() {
        if (getDirection() == FORWARD) {
            direction = BACKWARD;
        } else {
            direction = FORWARD;
        }
    }

    public boolean isNextForward(WarningGameObject gameObject) {
        return gameObject.getPosition() == this.getPosition() + 1;
    }

    public boolean isNextBackward(WarningGameObject gameObject) {
        return gameObject.getPosition() == this.getPosition() - 1;
    }

    public List<Bottle> getBottles() {
        List<Bottle> ret = new ArrayList<>();
        for (WarningGameObject staff : this.getStuff()) {
            if (staff instanceof Bottle) {
                Bottle bottle = (Bottle)staff;
                if(!bottle.isPuke()) {
                    ret.add(bottle);
                }
            }
        }
        return ret;
    }

    public Bottle getFirstFullBottle() {
        for (Bottle bottle : getBottles()) {
            if(bottle.getVolume() > 0) {
                /*TODO return the most convinient bottle for situation*/
                return bottle;
            }
        }
        return null;
    }

    public Sword getSword() {
        for (WarningGameObject staff : this.getStuff()) {
            if (staff instanceof Sword) {
                return (Sword)staff;
            }
        }
        return null;
    }

    public boolean hasSword() {
        return getSword() != null;
    }

    public boolean hasFullBottle() {
        return getFirstFullBottle() != null;
    }


    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    public int getJumpFieldPosition() {
        return getDirection() == BACKWARD ? -2 : 2;
    }

    public int getMoveFieldPosition() {
        return getDirection() == BACKWARD ? -1 : 1;
    }
}
