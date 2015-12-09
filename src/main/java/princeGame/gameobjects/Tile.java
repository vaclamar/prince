/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package princeGame.gameobjects;

import cz.yellen.xpg.common.stuff.GameObject;
import java.util.List;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class Tile extends WarningGameObject {

    private Boolean closer;

    public Tile(GameObject pit) {
        super(pit);
    }

    /**
     * @return the closer
     */
    public Boolean isCloser() {
        return closer;
    }

    /**
     * @param closer the closer to set
     */
    public void setCloser(Boolean closer) {
        this.closer = closer;
    }
}
