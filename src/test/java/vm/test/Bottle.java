package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class Bottle extends GameObjectImpl {

    public static final String VOLUME = "volume";

    public Bottle(int absolutePossition) {
        super(absolutePossition, true, true, true);
        getProperties().put(VOLUME, "3");
        getProperties().put("odour", "mint"); //puke
        setVisibility(1);
    }

    public int getLiveAmount() {
        Integer volume = Integer.parseInt(getProperty(VOLUME));
        if (properties.get("odour").equals("puke")) {
            return -volume;
        }
        return volume;
    }
}
