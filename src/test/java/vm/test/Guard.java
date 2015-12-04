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
    Map<String, String> property = new HashMap<>();


    protected Guard(int absolutePossition) {
        super(absolutePossition);
        property.put("dead", "false");
        property.put("health", "3");
    }

    @Override
    public int getId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getType() {
        return "guard";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(String s) {
        return property.get(s);
    }

    @Override
    public Map<String, String> getProperties() {
        return property;
    }

    @Override
    public Set<GameObject> getStuff() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
