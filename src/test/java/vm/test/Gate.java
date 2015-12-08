package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Gate extends CloseableGO {

    public Gate(int absolutePossition) {
        super(absolutePossition, false, true, true);
    }
}
