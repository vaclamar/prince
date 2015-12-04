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
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return "sword";  //To change body of implemented methods use File | Settings | File Templates.
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
