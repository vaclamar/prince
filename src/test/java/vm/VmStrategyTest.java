package vm;

import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.stuff.GameStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import princeGame.MyGameStrategy;
import vm.test.Bottle;
import vm.test.Game;
import vm.test.Guard;
import vm.test.LiveGo;


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
        Game game = new Game("G XW");
        test(game, 200);
    }

    @Test
    public void testL2() throws Exception {
        Game game = new Game("W X U  U G");
        test(game, 200);
    }


    @Test
    public void testL2UG() throws Exception {
        Game game = new Game("W X U  UG");
        test(game, 200);
    }


    @Test
    public void testL2WithChangeDirection() throws Exception {
        Game game = new Game("G X U U W");
        test(game, 200);
    }

    @Test
    public void testL3WithChangeDirection() throws Exception {
        Game game = new Game("G XY");
        test(game, 200);
    }

    @Test
    public void testL3WithSG() throws Exception {
        Game game = new Game("W I X YG");
        test(game, 200);
    }

    @Test
    public void testL3_2() throws Exception {
        Game game = new Game("WI X Y Y G");
        test(game, 200);
    }

    @Test
    public void testBottlesHell() throws Exception {
        Game game = new Game("W  XI YGW");
        Bottle bottle = new Bottle(1);
        bottle.getProperties().put(Bottle.VOLUME, "5");
        game.getAllGameObjects().add(bottle);
        Guard guard = (Guard) game.getAllGameObjects().stream().filter(go -> go.getType().equals(new Guard(0).getType())).findFirst().get();
        guard.getProperties().put(LiveGo.HEALTH, "9");
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
