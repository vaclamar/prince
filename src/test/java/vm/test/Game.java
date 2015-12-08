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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static vm.test.LiveGo.DEAD;

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
        wall('W', () -> new Wall(0)),
        bottle('b', () -> new Bottle(0));

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
            Optional<GameObjectImpl> notMoveableObject = getOnPositionByPredicate(princePosition, go -> !go.isMoveAble());
            if (notMoveableObject.isPresent()) {
                GameObjectImpl go = notMoveableObject.get();
                fail("try to move to not moveable object " + go);
            }
        }
        if (a instanceof Jump) {
            Jump jump = (Jump) a;
            int sign = getsign(jump.getDirection());
            Optional<GameObjectImpl> notMoveable = getOnPositionByPredicate(princePosition + sign * 2, go -> !go.isMoveAble());
            Optional<GameObjectImpl> notJumpable = getOnPositionByPredicate(princePosition + sign, go -> !go.isJumpable());


            if (notJumpable.isPresent()) {
                fail("object " + notJumpable + " is not jumpable");
            } else if (notMoveable.isPresent()) {
                fail("object " + notMoveable + " is not moveable");
            }
            princePosition += sign * 2;
        }
        if (a instanceof Enter) {
            if (gameObjects.stream().filter(go -> go.getType().equals("gate") && go.getAbsolutePossition() == princePosition).findFirst().isPresent()) {
                setStatus(GameStatus.VICTORY);
            }
        }
        if (a instanceof PickUp) {
            PickUp pickUp = (PickUp) a;
            GameObjectImpl go = (GameObjectImpl) (pickUp.getGameObject());
            if (go.isPickable()
                    && go.getAbsolutePossition() == prince.getAbsolutePossition()
                    ) {
                prince.getStuff().add(go);
                gameObjects.remove(pickUp.getGameObject());
            }

        }
        if (a instanceof Use) {
            final Use use = (Use) a;
            if (!prince.getStuff().contains(use.getInstrument())) {
                fail("cannoc use object which is not in prince stuff");
            }

            if (use.getInstrument() instanceof Sword) {
                if (use.getTarget() instanceof Guard) {
                    Guard guard = (Guard) use.getTarget();
                    if (Math.abs(guard.getAbsolutePossition() - princePosition) != 1) {
                        fail("cannot kill guard which is not next to you " + guard);
                    }
                    guard.hit(1);
                } else {
                    fail("cannot be used sword on " + use.getInstrument());
                }
            }

            if (use.getInstrument() instanceof Bottle) {
                Bottle bottle = (Bottle) use.getInstrument();
                if (prince != use.getTarget()) {
                    fail("bottle can be used only on prince not on " + use.getTarget());
                }
                prince.hit(-bottle.getLiveAmount());
            }


            if (use.getTarget().getType().equals("guard") &&
                    use.getInstrument().getType().equals("sword") &&
                    distance(use.getTarget(), use.getInstrument()) == 1 &&
                    prince.getStuff().stream().filter(go -> go.getId() == use.getInstrument().getId()).findFirst().isPresent()
                    ) {
                ((Guard) use.getTarget()).hit(1);
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

    private Optional<GameObjectImpl> getOnPositionByPredicate(int position, Predicate<GameObjectImpl> filter) {
        return gameObjects.stream().filter(go -> go.getAbsolutePossition() == position).filter(filter).findFirst();
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
                .filter(go -> Math.abs(go.getPosition()) <= 3).collect(Collectors.toSet());
    }
}
