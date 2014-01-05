package tacticulous.game.commands;

import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;

/**
 * Contains methods used for attacking and moving units.
 *
 * @author O
 */
public abstract class UnitCommand {

    /**
     * Checks if unit can move to target tile.
     *
     * @param map
     * @param unit
     * @param toX
     * @param toY
     * @param costs movement cost map from unit's current location.
     * @return false if target tile is outside map bounds, occupied by another
     * unit, or unit is too slow.
     */
    public static boolean checkMove(BattleMap map, Unit unit, int toX, int toY, int[][] costs) {
        if (!map.legit(toX, toY)) {
            return false;
        }
        if (map.getTile(toX, toY).getUnit() != null) {
            return false;
        }
        if (unit.getSpeed() < costs[toX][toY]) {
            return false;
        }
        return true;
    }

    /**
     * Moves unit to target tile.
     *
     * @param map
     * @param unit
     * @param tile
     */
    public static void move(BattleMap map, Unit unit, Tile tile) {
        map.getTile(unit.getX(), unit.getY()).setUnit(null);
        tile.setUnit(unit);
        unit.moves();
    }

    /**
     * Checks if unit can attack target tile.
     *
     * @param map
     * @param unit
     * @param toX
     * @param toY
     * @return false if unit tries to attack itself or target tile is outside
     * map bounds, not occupied by any unit, or not in attack range.
     */
    public static boolean checkAttack(BattleMap map, Unit unit, int toX, int toY) {
        if (!map.legit(toX, toY)) {
            return false;
        }
        if (map.getTile(toX, toY).getUnit() == null) {
            return false;
        }
        if (map.getTile(toX, toY).getUnit() == unit) {
            return false;
        }
        return !checkNotInRange(unit, toX, toY);
    }

    /**
     * Checks if unit is in attack range of target tile.
     *
     * @param unit
     * @param toX
     * @param toY
     * @return true if not in attack range.
     */
    public static boolean checkNotInRange(Unit unit, int toX, int toY) {
        return  checkDistance(unit, toX, toY) > unit.getRange();
    }

    public static int checkDistance(Unit unit, int toX, int toY) {
        return Math.max(Math.abs(unit.getX() - toX), Math.abs(unit.getY() - toY));
    }

    /**
     * Attacks unit in target tile.
     *
     * @param die used for attack roll
     * @param attacker
     * @param target
     * @return -1 if attack roll misses, 0 if it hits, 1 if it kills target
     */
    public static int attack(Die die, Unit attacker, Tile target) {
        Unit defender = target.getUnit();
        int result = attackRoll(attacker, defender, die.roll());
        if (result == 1) {
            defender.getPlayer().kill(defender);
            target.setUnit(null);
        }
        attacker.attacks();
        return result;
    }

    /**
     * Determines if attack hits or kills target.
     *
     * @param attacker
     * @param defender
     * @param dieRoll attack roll
     * @return -1 if attack roll misses, 0 if it hits, 1 if it kills target
     */
    public static int attackRoll(Unit attacker, Unit defender, int dieRoll) {
        int result = attacker.getAttack() + dieRoll - defender.getDefense()
                -checkDistance(attacker, defender.getX(), defender.getY());
        if (result > 0) {
            if (defender.isHitAndDies()) {
                return 1;
            }
            return 0;
        }
        return -1;
    }

    /**
     * Calculates the chance of attacking unit hitting the target unit.
     *
     * @param attacker
     * @param defender
     * @return percent chance of hitting
     */
    public static int chanceToHit(Unit attacker, Unit defender) {
        int result = 100 + (10 * (attacker.getAttack() - defender.getDefense()
                -checkDistance(attacker, defender.getX(), defender.getY())));
        if (result < 0) {
            return 0;
        } else if (result > 100) {
            return 100;
        } else {
            return result;
        }
    }

}
