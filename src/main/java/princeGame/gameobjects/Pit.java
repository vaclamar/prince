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
public class Pit extends WarningGameObject {

    public Pit(GameObject pit) {
        super(pit);
    }

    @Override
    public boolean isDanger() {
        return true;
    }
}