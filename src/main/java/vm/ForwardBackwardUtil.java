package vm;

import cz.yellen.xpg.common.action.Direction;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public class ForwardBackwardUtil {
    public static int getsign(Direction d){
        switch (d){
            case FORWARD:
                return 1;
            case BACKWARD:
                return -1;
            default:
                return 0;
        }

    }
}
