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
public class Gate extends WarningGameObject implements ICloseable{

    public Gate(GameObject gate) {
        super(gate);
    }

    @Override
    public boolean isOpened() {
        return this.getProperty("opened").equals("true") && !this.getProperty("closed").equals("true");
    }
}
