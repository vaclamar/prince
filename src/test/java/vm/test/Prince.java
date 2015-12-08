package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:50
 * To change this template use File | Settings | File Templates.
 */
public class Prince extends LiveGo {
    public Prince(int absolutePossition) {
        super(absolutePossition, false, true, true);
        getProperties().put(HEALTH, "5");
    }

    @Override
    public String getType() {
        return "prince";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
