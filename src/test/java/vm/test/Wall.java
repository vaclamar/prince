package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public class Wall extends GameObjectImpl {

    protected Wall(int absolutePossition) {
        super(absolutePossition);
    }

    @Override
    public String getType() {
        return "wall";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
