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
public class Bottle extends WarningGameObject implements IPickable{

    public Bottle(GameObject bottle) {
        super(bottle);
    }

    public int getVolume() {
        return Integer.parseInt(getProperty("volume"));
    }

    public boolean isPuke() {
        return getProperty("odour").equals("puke");
    }
}
