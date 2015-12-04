package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 04.12.15
 * Time: 9:33
 * To change this template use File | Settings | File Templates.
 */
public class Guard extends GameObjectImpl {


    protected Guard(int absolutePossition) {
        super(absolutePossition);
        getProperties().put("dead", "false");
        getProperties().put("health", "3");
    }

    @Override
    public String getType() {
        return "guard";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
