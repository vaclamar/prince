package vm.test;

import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;
import cz.yellen.xpg.common.stuff.GameStatus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static vm.ForwardBackwardUtil.getsign;

/**
 * Created with IntelliJ IDEA.
 * User: visy00
 * Date: 03.12.15
 * Time: 15:43
 * To change this template use File | Settings | File Templates.
 */
public class Game implements GameSituation {


    private int stepNr = 0;
    private boolean gameOver = false;
    private int princePosition;
    private Set<GameObjectImpl> gameObjects = new HashSet<>();
    private GameStatus status=GameStatus.CONTINUE;


    public Game(String situation) {
        for (int i = 0; i < situation.length(); i++) {
            switch (situation.getBytes()[i]) {
                case 'W':
                case 'w':
                    gameObjects.add(new Wall(i));
                    break;
                case 'X':
                case 'x':
                    gameObjects.add(new Prince(i));
                    princePosition = i;
                    break;
                case 'U':
                case 'u':
                    gameObjects.add(new Pit(i));
                    break;
                case 'g':
                case 'G':
                    gameObjects.add(new Gate(i));
                    break;
            }
        }
    }


    @Override
    public String toString() {
        Map<Integer, Character> situationMap = new HashMap<>();
        for (GameObjectImpl gameObject : gameObjects) {
            situationMap.put(gameObject.getAbsolutePossition(), mapToChar(gameObject.getType()));
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; !situationMap.isEmpty(); i++) {
            if (situationMap.containsKey(i)) {
                sb.append((char) situationMap.get(i));
                situationMap.remove(i);
            } else {
                sb.append('_');
            }

        }
        return sb.toString();
    }

    private Character mapToChar(String type) {
        switch (type) {
            case "gate":
                return 'G';
            case "prince":
                return 'X';
            case "pit":
                return 'U';
            case "wall":
                return 'W';

        }
        return '_';  //To change body of created methods use File | Settings | File Templates.
    }

    public void doAction(Action a) {
        stepNr++;
        if (a instanceof Move) {
            Move move = (Move) a;
            int sign = getsign(move.getDirection());
            princePosition += sign;
            if(gameObjects.stream().filter(go->go.getAbsolutePossition()==princePosition&&go.getType()=="wall").findFirst().isPresent()){
                princePosition -= sign;
            }
        }
        if (a instanceof Jump) {
            Jump jump = (Jump) a;
            int sign = getsign(jump.getDirection());
            Set<GameObjectImpl> walls = gameObjects.stream().filter(go -> go.getType().equals("wall"))
                    .filter(go -> go.getAbsolutePossition() == princePosition + sign || go.getAbsolutePossition() == princePosition + 2 * sign)
                    .collect(Collectors.toSet());
            if (walls.isEmpty()) {
                princePosition += 2 * sign;
            } else if (walls.iterator().next().getAbsolutePossition() == princePosition + 2 * sign && walls.size() == 1) {
                princePosition += sign;
            }
        }
        if (a instanceof Enter) {
            Enter enter = (Enter) a;
            if (gameObjects.stream().filter(go -> go.getType().equals("gate") && go.getAbsolutePossition() == princePosition).findFirst().isPresent()) {
                setStatus(GameStatus.VICTORY);
            }
        }

        gameObjects.stream().filter(go -> go.getType().equals("prince")).findFirst().get().setAbsolutePossition(princePosition);
        if (gameObjects.stream().filter(go->go.getType().equals("pit")&&go.getAbsolutePossition()==princePosition).findFirst().isPresent()){
            setStatus(GameStatus.PRINCE_DEAD);
        }
    }

    @Override
    public int getStepNr() {
        return stepNr;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isGameOver() {
        return gameOver;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public int getGameId() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Set<GameObject> getGameObjects() {
        return gameObjects.stream()
                .map(gameObject -> {
                    gameObject.setPrincePosition(princePosition);
                    return gameObject;
                })
                .filter(go -> Math.abs(go.getPosition()) <= 1).collect(Collectors.toSet());
    }
}
