package tacticulous.tira.ai;

import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;

/**
 * Has some additional AI-specific methods and parameters.
 *
 * @author O
 */
public class AIUnit extends Unit {

    private int attackedCount;

    /**
     * @param speed how fast unit can move.
     * @param defense defense modifier
     * @param attack attack modifier
     * @param range attack range
     * @param hitPoints how much damage unit can survive
     * @param name name of the unit
     * @param player owner of unit
     * @param attackedCount this is used for target value evaluation
     * @param x
     * @param y
     * @param notMoved
     * @param notAttacked
     * @param notDelayed
     * @param isLeader
     */
    public AIUnit(int speed, int defense, int attack, int range, int hitPoints,
            String name, Player player, int x, int y, boolean notMoved,
            boolean notAttacked, boolean notDelayed, int attackedCount, boolean isLeader) {
        super(speed, defense, attack, range, hitPoints, name, player);
        super.notAttacked = notAttacked;
        super.notMoved = notMoved;
        super.notDelayed = notDelayed;
        super.leader = isLeader;
        this.setXY(x, y);
        this.attackedCount = attackedCount;
    }

    /**
     * Used for target value evaluation.
     *
     * @author O
     * @return calculated from the sum of speed, attack, defensa and range,
     * divided by hit points
     * @see tacticulous.tira.ai.ArtificialIntelligence
     */
    public int targetValue() {
        int leaderMultiplier = 1;
        if (leader) {
            leaderMultiplier = 12;
        }
        return leaderMultiplier * (speed + attack + defense + range) / (hitPoints + attackedCount);
    }

    public void undoMove() {
        notMoved = true;
    }

    public void undoAttack() {
        notAttacked = true;
    }

    public void undoDelay() {
        notDelayed = true;
    }

    public void attacked() {
        attackedCount++;
    }

    public void undoAttacked() {
        attackedCount--;
    }

    @Override
    public String toString() {
        return "" + attackedCount;
    }

    public int getAttackedCount() {
        return attackedCount;
    }

}
