package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Gate extends GameObjectImpl {
    protected Gate(int absolutePossition) {
        super(absolutePossition, false, true, true);
        getProperties().put("opened", "true");
        getProperties().put("closed", "false");

    }

    @Override
    public String getType() {
        return "gate";  //To change body of implemented methods use File | Settings | File Templates.
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
