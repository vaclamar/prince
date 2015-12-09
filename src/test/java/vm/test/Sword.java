package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 04.12.15
 * Time: 9:32
 * To change this template use File | Settings | File Templates.
 */
public class Sword extends GameObjectImpl {
    public Sword(int absolutePossition) {
        super(absolutePossition, true, true, true);
        setVisibility(1);
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
