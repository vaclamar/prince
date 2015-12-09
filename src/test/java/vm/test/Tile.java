package vm.test;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 08.12.15
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class Tile extends GameObjectImpl {
    private Runnable onStep = ()->{};

    public Tile(int absolutePossition) {
        super(absolutePossition, false, true, true);
        setVisibility(1);
    }

    public void doOnStep(){
        onStep.run();
    }

    public void setOnStep(Runnable onStep){
        this.onStep=onStep;
    }
}
