package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 20:54
 * To change this template use File | Settings | File Templates.
 */
public class Pit extends GameObjectImpl{

    protected Pit(int absolutePossition) {
        super(absolutePossition);
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return "pit";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(String s) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getProperties() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<GameObject> getStuff() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
