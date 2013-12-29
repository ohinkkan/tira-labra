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

    public static boolean move(BattleMap map, Unit unit, int toX, int toY, int[][] costs) {
        if (!map.legit(toX, toY)) {
            System.out.println("target out of map boundaries");
            return false;
        }
        if (map.getTile(toX, toY).getUnit() != null) {
            System.out.println("target tile occupied");
            return false;
        }
        if (unit.getSpeed() < costs[toX][toY]) {
            System.out.println("unit too slow");
            return false;
        }
        map.getTile(unit.getX(), unit.getY()).setUnit(null);
        map.getTile(toX, toY).setUnit(unit);
        unit.moves();
        return true;
    }

    // returns -2 if attack is not legal, -1 if attackRoll misses, 0 if it hits, 1 if it kills target
    public static int attack(BattleMap map, Unit unit, int toX, int toY) {
        if (!map.legit(toX, toY)) {
            System.out.println("target out of map boundaries");
            return -2;
        }
        if (map.getTile(toX, toY).getUnit() == null) {
            System.out.println("no target");
            return -2;
        }
        if (Math.abs(unit.getX() - toX) > unit.getRange() || Math.abs(unit.getY() - toY) > unit.getRange()) {
            System.out.println("target out of range");
            return -2;
        }
        Unit target =  map.getTile(toX, toY).getUnit();
        int result = attackRoll(unit, target, map.getDie().roll());
        if (result == 1) {
            target.getPlayer().kill(target);
            map.getTile(toX, toY).setUnit(null);
            
        }
        unit.attacks();
        return result;
    }

    // returns -1 if attackRoll misses, 0 if it hits, 1 if it kills target
    public static int attackRoll(Unit attacker, Unit target, int dieRoll) {
        int result = attacker.getAttack() + dieRoll - target.getDefense();
        if (result > 0) {
            if (target.isHitAndDies()) {
                System.out.println("kills");
                return 1;
            }
            System.out.println("hits");
            return 0;
        }
        System.out.println("misses");
        return -1;
    }
}
