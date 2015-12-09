package vm;


import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.stuff.GameStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.stuff.GameStatus;
import princeGame.MyGameStrategy;
import vm.test.Bottle;
import vm.test.Game;
import vm.test.Gate;
import vm.test.Guard;
import vm.test.LiveGo;
import vm.test.Portcullis;
import vm.test.Tile;


public class VmStrategyTest {


	
    protected GameStrategy strategy;
    

    @Before
    public void setUp() throws Exception {
        strategy = new MyGameStrategy();
    }

    @Test
    public void testL1() throws Exception {
        Game game = new Game("W__X__8");
        test(game, 200);
    }

    @Test
    public void testL1ChangeDirection() throws Exception {
        Game game = new Game("8 XW");
        test(game, 200);
    }

    @Test
    public void testL2() throws Exception {
        Game game = new Game("W X U  U 8");
        test(game, 200);
    }


//    @Test
    public void testL2UG() throws Exception {
        Game game = new Game("W X U  U 8");
        test(game, 200);
    }


    @Test
    public void testL2WithChangeDirection() throws Exception {
        Game game = new Game("8 X U U W");
        test(game, 200);
    }

    @Test
    public void testL3WithChangeDirection() throws Exception {
        Game game = new Game("8 XY");
        test(game, 200);
    }

    @Test
    public void testL3WithSG() throws Exception {
        Game game = new Game("W I X Y8");
        test(game, 200);
    }

    @Test
    public void testL3_2() throws Exception {
        Game game = new Game("WI X Y Y 8");
        test(game, 200);
    }

//    @Test
    public void testImpossible() throws Exception {
        Game game = new Game("W8     X      W");
        Gate gate = (Gate) game.getAllGameObjects().stream().filter(go -> go.getType().equals("princess")).findFirst().get();
        Portcullis port5 = new Portcullis(5);
        port5.close();
        Portcullis port10 = new Portcullis(10);
        port10.close();
        Tile tile3 = new Tile(3);
        tile3.setOnStep(() -> port10.open());
        Tile tile4 = new Tile(4);
        tile4.setOnStep(() -> gate.close());
        Tile tile6 = new Tile(6);
        tile6.setOnStep(() -> port5.close());
        Tile tile8 = new Tile(8);
        tile8.setOnStep(() -> port5.open());
        Tile tile9 = new Tile(9);
        tile9.setOnStep(() -> port10.close());
        Tile tile12 = new Tile(12);
        tile12.setOnStep(() -> gate.open());

        game.getAllGameObjects().add(port5);
        game.getAllGameObjects().add(port10);
        game.getAllGameObjects().add(tile3);
        game.getAllGameObjects().add(tile4);
        game.getAllGameObjects().add(tile6);
        game.getAllGameObjects().add(tile8);
        game.getAllGameObjects().add(tile9);
        game.getAllGameObjects().add(tile12);
        test(game, 800);

    }

    @Test
    public void testBottlesHell() throws Exception {
        Game game = new Game("W  XI Y8W");
        Bottle bottle = new Bottle(1);
        bottle.getProperties().put(Bottle.VOLUME, "5");
        game.getAllGameObjects().add(bottle);
        Guard guard = (Guard) game.getAllGameObjects().stream().filter(go -> go.getType().equals(new Guard(0).getType())).findFirst().get();
        guard.getProperties().put(LiveGo.HEALTH, "8");
        test(game, 200);
    }

    @Test
    public void testChopper() throws Exception {
        Game game = new Game("WX| 8  W");
        test(game, 200);
    }


    @Test
    public void testChopper2() throws Exception {
        Game game = new Game("WX | 8W");
        test(game, 200);
    }

    @Test
    public void testChopperR() throws Exception {
        Game game = new Game("W8 |XW");
        test(game, 200);
    }

    @Test
    public void testPitWall() throws Exception {
        Game game = new Game("W 8   X UW");
        test(game, 200);
    }


    @Test
    public void testPitPit() throws Exception {
        Game game = new Game("W 8    X    UU");
        test(game, 200);
    }

    @Test
    public void testPitChopper() throws Exception {
        Game game = new Game("W 8   X  U|");
        test(game, 200);
    }


    @Test
    public void testChopperPorticullis() throws Exception {
        Game game = new Game("W    X _   |#8W");
        Portcullis p = (Portcullis) game.getAllGameObjects().stream().filter(go -> go.getType().equals("portcullis")).findFirst().get();
        p.close();
        Tile t1 = (Tile) game.getAllGameObjects().stream().filter(go -> go.getType().equals("tile")).findFirst().get();
        t1.setOnStep(() -> {
            System.out.println("aaa" + p.getProperty("opened"));
            p.change();
            System.out.println("bbb" + p.getProperty("opened"));
        });
        test(game, 200);
    }

    @Test
    public void testChopperPorticullis2() throws Exception {
        Game game = new Game("W    X    |#8W");
        Portcullis p = (Portcullis) game.getAllGameObjects().stream().filter(go -> go.getType().equals("portcullis")).findFirst().get();
        p.open();
        test(game, 200);
    }

    @Test
    public void testChopper2R() throws Exception {
        Game game = new Game("W8 | XW");
        test(game, 200);
    }

    private void test(Game game, int maxSteps) {
        while (game.getStatus().equals(GameStatus.CONTINUE)) {
            System.out.println(game);
            Action action = strategy.step(game);
            System.out.println(action.getClass().getSimpleName() + "  " + maxSteps);
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
