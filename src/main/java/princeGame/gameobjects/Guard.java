/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class Guard extends WarningGameObject implements IDeadable {

    public Guard(GameObject guard) {
        super(guard);
    }

    @Override
    public int getHealth() {
        return Integer.parseInt(this.getGameObject().getProperty("health"));
    }

    @Override
    public boolean isLive() {
        return !Boolean.parseBoolean(this.getGameObject().getProperty("dead"));
    }
}
