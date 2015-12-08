package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 23:31
 * To change this template use File | Settings | File Templates.
 */
public abstract class CloseableGO extends GameObjectImpl {
    public static final String OPENED = "opened";
    public static final String CLOSED = "closed";

    public CloseableGO(int absolutePossition, boolean pickable, boolean moveable, boolean jumpable) {
        super(absolutePossition, pickable, moveable, jumpable);
        getProperties().put(OPENED, "true");
        getProperties().put(CLOSED, "false");
    }

    public void open(){
        getProperties().put(OPENED, "true");
        getProperties().put(CLOSED, "false");
    }

    public void close(){
        getProperties().put(OPENED, "false");
        getProperties().put(CLOSED, "true");
    }

    public void change(){
        if(Boolean.parseBoolean(getProperty(OPENED))){
            close();
        }else{
            open();
        }
    }
}
