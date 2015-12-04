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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cz.yellen.xpg.common.action.Direction.*;
import static vm.ForwardBackwardUtil.*;

/**
 * @author Martin Vaclavik <martin.vaclavik@teliasonera.com>
 */
public class VmStrategy implements GameStrategy {

    Direction direction = FORWARD;

    public Action step(GameSituation gs) {
        try {
            GameObject prince = getSingle(gs, "prince");
            GameObject gate = getSingle(gs, "gate");
            GameObject wall = getSingle(gs, "wall");
            List<GameObject> pits = gs.getGameObjects().stream().filter(typeFilter("pit")).collect(Collectors.toList());

            if (gate != null) {
                if (prince.getPosition() == gate.getPosition()) {
                    return new Enter(gate);
                }
                if (gate.getPosition() > prince.getPosition()) {
                    return new Move(FORWARD);
                }
                if (gate.getPosition() < prince.getPosition()) {
                    return new Move(BACKWARD);
                }
            }

            for (GameObject pit : pits) {
                if ((pit.getPosition() - prince.getPosition()) * getsign(direction) > 0) {
                    return new Jump(direction);
                }
            }
            if (wall != null && (wall.getPosition() - prince.getPosition()) * getsign(direction) > 0) {
                direction = changeDirection(direction);
                return step(gs);
            }
            return new Move(direction);
        } catch (Throwable th) {
            th.printStackTrace();
            return new Wait();
        }
    }

    private GameObject getSingle(GameSituation gs, String type) {
        Optional<GameObject> first = gs.getGameObjects().stream().filter(typeFilter(type)).findFirst();
        //TODO bad practise but dont know java 8 perfectly
        if (first.isPresent()) {
            return first.get();
        } else {
            return null;
        }
    }

    private Predicate<GameObject> typeFilter(String type) {
        return go -> go.getType().equals(type);
    }


}
