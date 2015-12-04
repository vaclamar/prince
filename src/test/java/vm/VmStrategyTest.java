package vm;

import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.stuff.GameStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import princeGame.MyGameStrategy;
import vm.test.Game;


public class VmStrategyTest {

    private MyGameStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = new MyGameStrategy();
    }

    @Test
    public void testL1() throws Exception {
        Game game = new Game("W__X__G");
        test(game, 200);
    }

    @Test
    public void testL1ChangeDirection() throws Exception {
//        Game game = new Game("G__X__W");
        Game game = new Game("G_XW");
        test(game, 200);
    }

    @Test
    public void testL2() throws Exception {
        Game game = new Game("W__X_U__U_G");
        test(game, 200);
    }


    @Test
    public void testL2UG() throws Exception {
        Game game = new Game("W__X_U__UG");
        test(game, 200);
    }


    @Test
    public void testL2WithChangeDirection() throws Exception {
        Game game = new Game("G__X_U__U_W");
        test(game, 200);
    }

    @Test
    public void testL3WithChangeDirection() throws Exception {
        Game game = new Game("G__X__Y");
        test(game, 200);
    }

    @Test
    public void testL3WithSG() throws Exception {
        Game game = new Game("W_I__X__YG");
        test(game, 200);
    }

    @Test
    public void testL3_2() throws Exception {
        Game game = new Game("W_I__X__Y__Y__G");
        test(game, 200);
    }


    private void test(Game game, int maxSteps) {
        while (game.getStatus().equals(GameStatus.CONTINUE)) {
            System.out.println(game);
            Action action = strategy.step(game);
            System.out.println(action.getClass().getName());
            game.doAction(action);
            maxSteps--;
            if (maxSteps < 0) {
                Assert.fail("max steps used");
            }
        }
        Assert.assertEquals("end as " + game.getStatus(), GameStatus.VICTORY, game.getStatus());
        System.out.flush();
    }


}
