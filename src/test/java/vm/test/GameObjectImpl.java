package vm.test;

import cz.yellen.xpg.common.stuff.GameObject;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 16:13
 * To change this template use File | Settings | File Templates.
 */
abstract class GameObjectImpl implements GameObject {
    int absolutePossition=0;
    int princePosition=0;

    protected GameObjectImpl(int absolutePossition) {
        this.absolutePossition = absolutePossition;
    }

    @Override
    public int getPosition() {
        return absolutePossition-princePosition;  //To change body of implemented methods use File | Settings | File Templates.
    }

    int getAbsolutePossition() {
        return absolutePossition;
    }

    void setAbsolutePossition(int absolutePossition) {
        this.absolutePossition = absolutePossition;
    }

    int getPrincePosition() {
        return princePosition;
    }

    void setPrincePosition(int princePosition) {
        this.princePosition = princePosition;
    }
}
