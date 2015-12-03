package vm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package vma;
import cz.yellen.xpg.common.GameStrategy;
import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Direction;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.Wait;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;

/**
 *
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class VmStrategy implements GameStrategy {

    Move strategy = new Move(Direction.FORWARD);
    Game game = new Game();

    public Action step(GameSituation gs) {
        try {
            GameObject prince = null;
            GameObject gate = null;
            GameObject wall = null;
            GameObject pit = null;
            //init
            for (GameObject go : gs.getGameObjects()) {
                if (go.getType().equalsIgnoreCase("prince")) {
                    prince = go;
                }

                if (go.getType().equalsIgnoreCase("gate")) {
                    gate = go;
                }

                if (go.getType().equalsIgnoreCase("wall")) {
                    wall = go;
                }

                if (go.getType().equalsIgnoreCase("pit")) {
                    pit = go;
                }
            }

            if (gate != null) {
                if (prince.getPosition() == gate.getPosition()) {
                    return new Enter(gate);
                }
                if (gate.getPosition() > prince.getPosition()) {
                    return moveForward();
                }
                if (gate.getPosition() < prince.getPosition()) {
                    return moveBack();
                }
            } else if (pit != null) {
                game.getMap().put(game.getPosition()+pit.getPosition(), pit);
                if ( prince.getPosition() < pit.getPosition() && strategy.getDirection().equals(Direction.FORWARD)) {
                    return new Jump((Direction.FORWARD));
                }
                if ( prince.getPosition() > pit.getPosition() && strategy.getDirection().equals(Direction.BACKWARD)){
                    return new Jump((Direction.BACKWARD));
                }

            } else if (wall != null && wall.getPosition() > prince.getPosition()) {
                changeStrategy();
                return step(gs);
            }
            return strategy;
        } catch (Throwable th) {
            th.printStackTrace();
            return new Wait();
        }
    }

    void changeStrategy() {
        if (((Move) strategy).getDirection() == Direction.FORWARD) {
            strategy = new Move(Direction.BACKWARD);
        }

    }

    Move moveBack(){
        game.setPosition(game.getPosition()-1);
        return new Move(Direction.BACKWARD);
    }

    Move moveForward(){
        game.setPosition(game.getPosition()+1);
        return new Move(Direction.FORWARD);
    }

    Jump jumpBack(){
        game.setPosition(game.getPosition()-2);
        return new Jump(Direction.BACKWARD);
    }

    Jump jumpForward(){
        game.setPosition(game.getPosition()+2);
        return new Jump(Direction.FORWARD);
    }

}
