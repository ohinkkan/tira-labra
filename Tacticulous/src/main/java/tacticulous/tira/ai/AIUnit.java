package tacticulous.tira.ai;

import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;

/**
 * Has some additional AI-specific methods and parameters.
 *
 * @author O
 */
public class AIUnit extends Unit {

    private int speed;
    private int defense;
    private int attack;
    private int range;
    private int hitPoints;
    private int attackedCount;
    private boolean notAttacked;
    private boolean notMoved;
    private boolean notDelayed;

    /**
     * @param attackedCount this is used for target value evaluation
     */
    public AIUnit(int speed, int defense, int attack, int range, int hitPoints,
            String name, Player player, int x, int y, boolean notMoved,
            boolean notAttacked, boolean notDelayed, int attackedCount) {
        super(speed, defense, attack, range, hitPoints, name, player);
        this.setXY(x, y);
        this.notAttacked = notAttacked;
        this.notDelayed = notDelayed;
        this.notMoved = notMoved;
        this.attackedCount = attackedCount;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.range = range;
        this.hitPoints = hitPoints;
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
        return (speed + attack + defense + range) / (hitPoints + attackedCount);
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
    public boolean doneForTheRound() {
        return !(this.notAttacked && this.notMoved);
    }

    @Override
    public boolean hasNotMoved() {
        return notMoved;
    }

    @Override
    public boolean hasNotAttacked() {
        return notAttacked;
    }

    @Override
    public boolean hasNotDelayed() {
        return notDelayed;
    }

    @Override
    public String toString() {
        return "" + attackedCount;
    }

    public int getAttackedCount() {
        return attackedCount;
    }

}
