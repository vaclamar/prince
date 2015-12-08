package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
abstract class GameObjectImpl implements GameObject {
    int absolutePossition = 0;
    int princePosition = 0;
    private static int CreateId=0;
    Map<String, String> properties = new HashMap<>();
    private Set<GameObject> stuff = new HashSet<>();
    private final int id;

    protected GameObjectImpl(int absolutePossition) {
        this.absolutePossition = absolutePossition;
        id=CreateId++;
    }

    public boolean isPickable(){
        return false;
    }
    public boolean isMoveAble(){
        return false;
    }
    public boolean isJumpable(){
        return false;
    }
    @Override
    public int getPosition() {
        return absolutePossition - princePosition;  //To change body of implemented methods use File | Settings | File Templates.
    }

    int getAbsolutePossition() {
        return absolutePossition;
    }

    void setAbsolutePossition(int absolutePossition) {
        this.absolutePossition = absolutePossition;
    }

    void setPrincePosition(int princePosition) {
        this.princePosition = princePosition;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProperty(String s) {
        return properties.get(s);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getId() {
        return id;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<GameObject> getStuff() {
        return stuff;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
