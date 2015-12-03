package vm;

import cz.yellen.xpg.common.action.*;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;
import cz.yellen.xpg.common.stuff.GameStatus;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;


public class VmStrategyTest {

    class Game implements GameSituation {

        public void doAction(cz.yellen.xpg.common.action.Action action){

        }

        @Override
        public int getStepNr() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean isGameOver() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public GameStatus getStatus() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getGameId() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Set<GameObject> getGameObjects() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private VmStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new VmStrategy();
    }

    @Test
    public void testfirstLevelTest() throws Exception {
        Game game
                = new Game();
        while (true) {
            strategy.step(game);
        }
    }


}
