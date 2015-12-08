/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.action.Direction;
import static cz.yellen.xpg.common.action.Direction.*;
import cz.yellen.xpg.common.stuff.GameObject;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class Prince extends WarningGameObject {

    private Direction direction = FORWARD;

    public Prince(GameObject prince) {
        super(prince);
    }

    @Override
    public String getType() {
        return "prince";
    }

    public int getHealth() {
        return Integer.parseInt(getGameObject().getProperty("health"));
    }

    public boolean isBefore(GameObject gameObject) {
        return (getDirection() == BACKWARD && gameObject.getPosition() == this.getPosition() - 1) || (getDirection() == FORWARD && gameObject.getPosition() == this.getPosition() + 1);
    }

    public void changeDirection() {
        if (getDirection() == FORWARD) {
            direction = BACKWARD;
        } else {
            direction = FORWARD;
        }
    }

    /**
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }
}
