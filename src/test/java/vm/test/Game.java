package vm.test;

import cz.yellen.xpg.common.action.Action;
import cz.yellen.xpg.common.action.Direction;
import cz.yellen.xpg.common.action.Enter;
import cz.yellen.xpg.common.action.Jump;
import cz.yellen.xpg.common.action.Move;
import cz.yellen.xpg.common.action.PickUp;
import cz.yellen.xpg.common.action.Use;
import cz.yellen.xpg.common.stuff.GameObject;
import cz.yellen.xpg.common.stuff.GameSituation;
import cz.yellen.xpg.common.stuff.GameStatus;
import javafx.util.Builder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static vm.test.LiveGo.DEAD;

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
    private GameStatus status = GameStatus.CONTINUE;

    enum KnownGameObject {
        gate('G', () -> new Gate(0)),
        guard('Y', () -> new Guard(0)),
        pit('U', () -> new Pit(0)),
        prince('X', () -> new Prince(0)),
        sword('I', () -> new Sword(0)),
        wall('W', () -> new Wall(0));
        private final Builder<GameObjectImpl> builder;

        private char alias = ' ';

        KnownGameObject(char alias, Builder<GameObjectImpl> builder) {
            this.alias = alias;
            this.builder = builder;
        }

        char getAlias() {
            return alias;
        }

        Builder<GameObjectImpl> getBuilder() {
            return builder;
        }

        public static KnownGameObject getByAlias(char alias) {
            Optional<KnownGameObject> first = Arrays.stream(KnownGameObject.values()).filter(go -> go.getAlias() == alias).findFirst();
            if (first.isPresent()) {
                return first.get();
            } else {
                return null;
            }
        }
    }

    public Game(String situation) {
        for (int i = 0; i < situation.length(); i++) {
            KnownGameObject knownGameObject = KnownGameObject.getByAlias((char) situation.getBytes()[i]);
            if (knownGameObject != null) {
                if (knownGameObject == KnownGameObject.prince) {
                    princePosition = i;
                }
                GameObjectImpl go = knownGameObject.getBuilder().build();
                go.setAbsolutePossition(i);
                gameObjects.add(go);

            }
        }
    }


    @Override
    public String toString() {
        Map<Integer, Character> situationMap = new HashMap<>();
        for (GameObjectImpl gameObject : gameObjects) {
            situationMap.put(gameObject.getAbsolutePossition(), KnownGameObject.valueOf(gameObject.getType()).getAlias());
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

    public void doAction(Action a) {
        stepNr++;
        Prince prince = (Prince) gameObjects.stream().filter(go -> go.getType().equals("prince")).findFirst().get();
        if (a instanceof Move) {
            Move move = (Move) a;
            int sign = getsign(move.getDirection());
            princePosition += sign;
            if (gameObjects.stream().filter(go -> go.getAbsolutePossition() == princePosition && go.getType() == "wall").findFirst().isPresent()) {
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
        if (a instanceof PickUp) {
            PickUp pickUp = (PickUp) a;
            gameObjects.stream().filter(go -> go.getType().equals("prince")).findFirst().get().getStuff().add(pickUp.getGameObject());
            gameObjects.remove(pickUp.getGameObject());
        }
        if (a instanceof Use) {
            final Use use = (Use) a;
            if (use.getTarget().getType().equals("guard") &&
                    use.getInstrument().getType().equals("sword") &&
                    distance(use.getTarget(), use.getInstrument()) == 1 &&
                    prince.getStuff().stream().filter(go->go.getId()==use.getInstrument().getId()).findFirst().isPresent()
                    ) {
                ((Guard)use.getTarget()).hit(1);
            }
        }

        prince.setAbsolutePossition(princePosition);
        if (gameObjects.stream().filter(go -> (go.getType().equals("pit") || (go.getType().equals("guard") && go.getProperty(DEAD).equals("false")) || (go.getType().equals("chopper") && go.getProperty("closing").equals("true"))) && go.getAbsolutePossition() == princePosition).findFirst().isPresent()) {
            setStatus(GameStatus.PRINCE_DEAD);
        }

        //hit by all surroundings guards
        prince.hit((int) gameObjects.stream().filter(go -> go.getType().equals("guard") && go.getProperty(DEAD).equals("false") && Math.abs(go.getPosition()) == 1).count());
        if (prince.getProperty(DEAD).equals("true")) {
            setStatus(GameStatus.PRINCE_DEAD);
        }
    }

    private int distance(GameObject go1, GameObject go2) {
        return Math.abs(go1.getPosition() - go2.getPosition());
    }

    private int getsign(Direction direction) {
        switch (direction) {
            case FORWARD:
                return +1;
            case BACKWARD:
                return -1;
        }
        System.err.println("undefined direction " + direction);
        return 0;
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
                .map(go -> {
                    go.setPrincePosition(princePosition);
                    return go;
                })
                .filter(go -> Math.abs(go.getPosition()) <= 1).collect(Collectors.toSet());
    }
}
