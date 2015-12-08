package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class Bottle extends GameObjectImpl {
    protected Bottle(int absolutePossition) {
        super(absolutePossition);
        getProperties().put("volume", "3");
        getProperties().put("odour", "mint"); //puke
    }

    @Override
    public String getType() {
        return "bottle";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isMoveAble() {
        return true;
    }

    @Override
    public boolean isJumpable() {
        return true;
    }
}
