package tacticulous.tira.ai;

import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.Tile;
import tacticulous.game.utility.Die;

/**
 * Handles the main 'logic' for evaluating individual actions.
 *
 * @author O
 */
public class ValueLogic {

    private int aggression;
    private int defensiveness;
    private Die takeAChance;

    /**
     * Basic constructor.
     *
     * @param aggression increases the value of aggressive actions
     * @param defensiveness increases the value of survival-oriented stuff
     * @param takeAChance increaes randomness
     */
    public ValueLogic(int aggression, int defensiveness, Die takeAChance) {
        this.aggression = aggression;
        this.defensiveness = defensiveness;
        this.takeAChance = takeAChance;
    }

    /**
     * Value for a single attack. Takes into account target value and hit accuracy.
     *
     * @param terminator attacking unit, used for chance to hit calculation
     * @param meatBag target unit
     * @param round for future data access
     * @return value of attack
     */
    public int attackValue(AIUnit terminator, AIUnit meatBag, SimulatedRound round) {
        return UnitCommand.chanceToHit(terminator, meatBag) * meatBag.targetValue()
                * aggression + takeAChance.roll();
    }

    /**
     * Value for a single move action. Takes into account hostile units with
     * unused attacks, distance to enemies and if they are in range and compares
     * these to the starting square.
     *
     * @param terminator moving unit
     * @param tile target tile
     * @param round for data access
     * @return value of movement.
     */
    public int movementValue(AIUnit terminator, Tile tile, SimulatedRound round) {
        int range = distanceToClosest(tile, round);
        int oldRange = distanceToClosest(round.getTheMatrix().getTile(terminator.getX(), terminator.getY()), round);
        int value = (oldRange - range) * aggression;
        if (range <= terminator.getRange() && terminator.hasNotAttacked()) {
            value = value + (range * aggression);
        }
        int oldX = terminator.getX();
        int oldY = terminator.getY();
        int oldThreat = threatInRange(terminator, round);
        terminator.setXY(tile.getX(), tile.getY());
        int newThreat = threatInRange(terminator, round);
        terminator.setXY(oldX, oldY);
        value = value + (oldThreat - newThreat);
        return value + takeAChance.roll();
    }


    public int getAggression() {
        return aggression;
    }


    public int getDefensiveness() {
        return defensiveness;
    }


    public int takeAChance() {
        return takeAChance.roll();
    }

    /**
     * Value for a single delay action. Takes into account hostile units with
     * unused attacks.
     *
     * @param terminator delayer.
     * @param round data access.
     * @return value of delaying.
     */
    public int delayValue(AIUnit terminator, SimulatedRound round) {
        return -threatInRange(terminator, round) * defensiveness + takeAChance.roll();
    }

    /**
     * Calculates the treat rating of hostile units. Considers all enemies who
     * are in attack range and have unused attack action. Also considers their
     * accuracy and if own unit is the leader.
     *
     * @param unit unit being threatened.
     * @param round data access.
     * @return threat value, typically deducted from other values.
     */
    private int threatInRange(AIUnit unit, SimulatedRound round) {
        int threatValue = 0;
        for (AIUnit threat : round.getHostiles()) {
            if (round.getTargetsInRange(threat, round.getActiveUnits()).contains(unit)
                    && threat.hasNotAttacked()) {
                threatValue = threatValue
                        + UnitCommand.chanceToHit(threat, unit) / unit.getHitPoints();
            }
        }
        int leaderMultiplier = 1;
        if (unit.isLeader()) {
            leaderMultiplier = 20;
        }
        return leaderMultiplier * threatValue * defensiveness;
    }

    /**
     * Finds the Chebychev distance to closest hostile unit.
     *
     * @param tile distance is calculated from this tile.
     * @param round data access.
     * @return Chebychev distance
     */
    private int distanceToClosest(Tile tile, SimulatedRound round) {
        int distance = -1;
        for (AIUnit unit : round.getHostiles()) {
            int temp = tile.distanceTo(unit);
            if (temp < distance || distance < 0) {
                distance = temp;
            }
        }
        return distance;
    }
}
