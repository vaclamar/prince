package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 04.12.15
 * Time: 9:33
 * To change this template use File | Settings | File Templates.
 */
public class Guard extends LiveGo {


    protected Guard(int absolutePossition) {
        super(absolutePossition, false, false, false);
        getProperties().put(HEALTH, "1");
    }

    @Override
    public String getType() {
        return "guard";  //To change body of implemented methods use File | Settings | File Templates.
    }
}
