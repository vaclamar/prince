package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class Portcullis extends CloseableGO {

    public Portcullis(int absolutePossition) {
        super(absolutePossition, false, false, false);

    }

    @Override
    public boolean isMoveAble() {
        return Boolean.parseBoolean(getProperty(OPENED));
    }

    @Override
    public boolean isJumpable() {
        return Boolean.parseBoolean(getProperty(OPENED));
    }
}
