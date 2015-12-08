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
public class Chopper extends WarningGameObject implements ICloseable{

    public Chopper(GameObject chopper) {
        super(chopper);
    }

    @Override
    public boolean isOpened() {
        return this.getProperty("opening").equals("true");
    }
}
