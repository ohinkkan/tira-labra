package tacticulous.tira.ai;

import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.Tile;
import tacticulous.game.utility.Die;

/**
 *
 * @author O
 */
public class ValueLogic {

    private int aggression;
    private int defensiveness;
    private Die takeAChance;

    public ValueLogic(int aggression, int defensiveness, Die takeAChance) {
        this.aggression = aggression;
        this.defensiveness = defensiveness;
        this.takeAChance = takeAChance;
    }

    public int attackValue(AIUnit terminator, AIUnit meatBag, SimulatedRound round) {
        return UnitCommand.chanceToHit(terminator, meatBag) * meatBag.targetValue()
                * aggression + takeAChance.roll();
    }

    public int movementValue(AIUnit terminator, Tile tile, SimulatedRound round) {
        int range = distanceToClosest(tile, round);
        int oldRange = distanceToClosest(round.getTheMatrix().getTile(terminator.getX(), terminator.getY()), round);
        int value = oldRange - range;
        if (range <= terminator.getRange() && terminator.hasNotAttacked()) {
            value += (range * aggression);
        }
        int oldX = terminator.getX();
        int oldY = terminator.getY();
        int oldThreat = threatInRange(terminator, round);
        terminator.setXY(tile.getX(), tile.getY());
        int newThreat = threatInRange(terminator, round);
        terminator.setXY(oldX, oldY);
        value += (oldThreat - newThreat);
        return value + takeAChance.roll();
    }

    public int delayValue(AIUnit terminator, SimulatedRound round) {
        return -threatInRange(terminator, round) * defensiveness + takeAChance.roll();
    }

    private int threatInRange(AIUnit unit, SimulatedRound round) {
        int threatValue = 0;
        for (AIUnit threat : round.getHostiles()) {
            if (round.getTargetsInRange(threat, round.getActiveUnits()).contains(unit)
                    && threat.hasNotAttacked()) {
                threatValue = threatValue
                        + UnitCommand.chanceToHit(threat, unit) / unit.getHitPoints();
            }
        }
        return threatValue * defensiveness;
    }

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
