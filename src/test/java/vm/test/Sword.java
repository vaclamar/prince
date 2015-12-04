package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 04.12.15
 * Time: 9:32
 * To change this template use File | Settings | File Templates.
 */
public class Sword extends GameObjectImpl{
    protected Sword(int absolutePossition) {
        super(absolutePossition);
    }

    @Override
    public String getType() {
        return "sword";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
