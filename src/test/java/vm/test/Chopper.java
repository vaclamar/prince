package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public class Chopper extends GameObjectImpl {

    public static final String OPENING = "opening";
    public static final String CLOSING = "closing";

    public Chopper(int absolutePossition) {
        super(absolutePossition, false, false, false);
        getProperties().put(OPENING, "true");
        getProperties().put(CLOSING, "false");
    }

    public void open(){
        getProperties().put(OPENING, "true");
        getProperties().put(CLOSING, "false");
    }

    public void close(){
        getProperties().put(OPENING, "false");
        getProperties().put(CLOSING, "true");
    }

    public void change(){
        if (Boolean.parseBoolean(getProperty(OPENING))){
            close();
        }else{
            open();
        }
    }

    @Override
    public boolean isJumpable() {
        return Boolean.parseBoolean(getProperty(OPENING));
    }
}
