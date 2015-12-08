package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Gate extends GameObjectImpl {
    protected Gate(int absolutePossition) {
        super(absolutePossition);
        getProperties().put("opened","true");
        getProperties().put("closed", "false");
    }

    @Override
    public String getType() {
        return "gate";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
