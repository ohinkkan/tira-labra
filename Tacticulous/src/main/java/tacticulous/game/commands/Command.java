/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.commands;

import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;

/**
 *
 * @author O
 */
public class Command {

    public static boolean move(BattleMap map, int fromX, int fromY, int toX, int toY) {
        if (!map.legit(fromX, fromY, toX, toY)) {
            return false;
        }
        if (map.getTile(toX, toY).getUnit() != null || map.getTile(fromX, fromY).getUnit() == null) {
            return false;
        }
//        if (!AstarSearch.fastEnough(map.getTile(fromX, fromY).getUnit().getSpeed(), fromX, fromY, toX, toY, map)) {
//            return false;
//        }
        map.getTile(toX, toY).setUnit(map.getTile(fromX, fromY).getUnit());
        map.getTile(fromX, fromY).setUnit(null);
        return true;
    }

    public static boolean attack(BattleMap map, int fromX, int fromY, int toX, int toY) {
        if (!map.legit(fromX, fromY, toX, toY)) {
            return false;
        }
        if (map.getTile(toX, toY).getUnit() == null || map.getTile(fromX, fromY).getUnit() == null) {
            return false;
        }
        int result = attackRoll(map.getTile(fromX, fromY).getUnit(), map.getTile(toX, toY).getUnit(), map.getDie().roll());
        if (result == 1) {
            map.getTile(toX, toY).setUnit(null);
        }
        return true;
    }

    // returns -1 if attackRoll misses, 0 if it hits, 1 if it kills target
    public static int attackRoll(Unit attacker, Unit target, int dieRoll) {
        int result = attacker.getAttack() + dieRoll - target.getDefense();
        if (result > 0) {
            if (target.isHitAndDies()) {
                return 1;
            }
            return 0;
        }
        return -1;
    }
}
