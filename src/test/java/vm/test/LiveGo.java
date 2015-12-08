package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 6:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class LiveGo extends GameObjectImpl {

    public static final String HEALTH = "health";
    public static final String DEAD = "dead";

    LiveGo(int absolutePossition) {
        super(absolutePossition);
        properties.put(HEALTH,"5");
        properties.put(DEAD, "false");
    }

    public void hit(int amount){
        int health = Integer.parseInt(properties.get(HEALTH)) - amount;
        properties.put(HEALTH, health +"");
        if (health<0){
            properties.put(DEAD,"false");
        }
    }




}
