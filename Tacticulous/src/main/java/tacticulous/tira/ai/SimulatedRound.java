package tacticulous.tira.ai;

import java.util.ArrayList;
import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;

/**
 *
 * Main control class for simulated actions during a simulated round
 *
 * @author O
 */
public class SimulatedRound {

    private ArrayList<AIUnit> activeUnits;
    private ArrayList<AIUnit> hostiles;
    private ArtificialIntelligence superiorIntellect;
    private BattleMap theMatrix;

    public SimulatedRound(ArrayList<AIUnit> activeUnits, ArrayList<AIUnit> hostiles,
            ArtificialIntelligence mind, BattleMap theMatrix) {
        this.activeUnits = activeUnits;
        this.hostiles = hostiles;
        this.superiorIntellect = mind;
        this.theMatrix = theMatrix;
    }

    private SimulatedRound(ArtificialIntelligence mind, BattleMap theMatrix) {
        this.superiorIntellect = mind;
        this.theMatrix = theMatrix;
    }

    /**
     * returns all enemiee in unit's range
     *
     * @param terminator
     * @param potentials
     * @return
     */
    public ArrayList<AIUnit> getTargetsInRange(Unit terminator, ArrayList<AIUnit> potentials) {
        ArrayList<AIUnit> targets = new ArrayList();
        for (AIUnit unit : potentials) {
            if (!UnitCommand.checkNotInRange(terminator, unit.getX(), unit.getY())) {
                targets.add(unit);
            }
        }
        return targets;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#moveAndAttack(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action moveAndAttack(AIUnit unit, Tile tile) {
        Action action = new Action(0, unit);
        Tile undo = tileFromUnit(unit);
        if (tile.getUnit() == null) {
            action = move(unit, tile, false);
            if (!getTargetsInRange(unit, hostiles).isEmpty()) {
                Action bestAttack = new Action(Integer.MIN_VALUE / 2);
                for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                    bestAttack = Action.getBetter(bestAttack, attack(unit, target, true));
                    target.undoAttacked();
                    unit.undoAttack();
                }
                action.copyAttackCoordinates(bestAttack);
            } else {
                action.negate();
            }
            move(unit, undo, false);
        } else {
            action.negate();
        }
        return action;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#attackAndMove(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action attackAndMove(AIUnit unit, Tile tile) {
        Tile undo = tileFromUnit(unit);
        Action best = new Action(Integer.MIN_VALUE / 2);
        if (tile.getUnit() == null) {
            for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                Action action = attack(unit, target, false);
                action.updateValue(move(unit, tile, true));
                action.setMovementCoordinates(tile.getX(), tile.getY());
                target.undoAttacked();
                unit.undoAttack();
                move(unit, undo, false);
                best = Action.getBetter(best, action);
            }
        } else {
            best.negate();
        }
        return best;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#moveAndDelay(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action moveAndDelay(AIUnit unit, Tile tile) {
        Tile undo = tileFromUnit(unit);
        Action action = new Action(0, unit);
        if (tile.getUnit() == null) {
            action = move(unit, tile, false);
            action.updateValue(delay(unit));
            move(unit, undo, false);
            unit.undoDelay();
        } else {
            action.negate();
        }
        return action;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#moveAndEndTurn(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action moveAndEndTurn(AIUnit unit, Tile tile) {
        Tile undo = tileFromUnit(unit);
        Action action = new Action(0, unit);
        if (tile.getUnit() == null) {
            action = (move(unit, tile, false));
            endTurn();
            move(unit, undo, false);
        } else {
            action.negate();
        }
        return action;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#attackAndDelay(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action attackAndDelay(AIUnit unit) {
        Action best = new Action(Integer.MIN_VALUE / 2);
        for (AIUnit target : getTargetsInRange(unit, hostiles)) {
            Action action = attack(unit, target, false);
            action.updateValue(delay(unit));
            target.undoAttacked();
            unit.undoAttack();
            unit.undoDelay();
            best = Action.getBetter(best, action);
        }
        return best;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#attackAndEndTurn(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action attackAndEndTurn(AIUnit unit) {
        Action best = new Action(Integer.MIN_VALUE / 2);
        for (AIUnit target : getTargetsInRange(unit, hostiles)) {
            Action action = (attack(unit, target, false));
            action.updateValue(endTurn());
            target.undoAttacked();
            unit.undoAttack();
            best = Action.getBetter(best, action);
        }
        return best;
    }

    /**
     * @see
     * tacticulous.tira.ai.ArtificialIntelligence#delay(tacticulous.tira.ai.AIUnit,
     * tacticulous.tira.ai.SimulatedRound, tacticulous.tira.ai.Action)
     */
    public Action delay(AIUnit unit) {
        Action action = new Action(superiorIntellect.getValueLogic().delayValue(unit, this), unit);
        unit.delays();
        return action;
    }

    /**
     * performs the actual simulated move
     */
    public Action move(AIUnit unit, Tile tile, boolean turnEnds) {
        Action action = new Action(superiorIntellect.getValueLogic().movementValue(unit, tile, this), unit);
        UnitCommand.move(superiorIntellect.connectToTheMatrix(), unit, tile);
        action.setMovementCoordinates(unit.getX(), unit.getY());
        if (turnEnds) {
            action.updateValue(endTurn());
        }
        return action;
    }

    /**
     * performs the actual simulated attack
     */
    public Action attack(AIUnit unit, AIUnit target, boolean turnEnds) {
        Action action = new Action(pseudoAttack(unit, target), unit);
        action.setAttackCoordinates(target.getX(), target.getY());
        if (turnEnds) {
            action.updateValue(endTurn());
        }
        return action;
    }

    /**
     * simulated end of turn; may recursively call for a new simulated turn or
     * even new round
     */
    public Action endTurn() {
        if (superiorIntellect.layer() < superiorIntellect.depth()) {
            superiorIntellect.diveDeeper();
            Action opponentAction;
            swapSides();
            if (checkIfRoundOver()) {
                opponentAction = superiorIntellect.simulateTurn(newRound());
            } else {
                opponentAction = superiorIntellect.simulateTurn(this);
            }
            opponentAction.swapValue();
            swapSides();
            superiorIntellect.unPlug();
            return opponentAction;
        }
        return new Action(0);
    }

    /**
     * checks if simulated round is over
     */
    public boolean checkIfRoundOver() {
        for (Unit unit : activeUnits) {
            if (!unit.doneForTheRound()) {
                return false;
            }
        }
        for (Unit unit : hostiles) {
            if (!unit.doneForTheRound()) {
                return false;
            }
        }
        return true;
    }

    /**
     * begins new simulated round
     */
    private SimulatedRound newRound() {
        BattleMap newMatrix = theMatrix.copy();
        SimulatedRound round = new SimulatedRound(superiorIntellect, newMatrix);
        round.activeUnits = resetedCopy(activeUnits, newMatrix);
        round.hostiles = resetedCopy(hostiles, newMatrix);
        return round;
    }

    private Tile tileFromUnit(Unit unit) {
        return superiorIntellect.connectToTheMatrix().getTile(unit.getX(), unit.getY());
    }

    /**
     *
     * simulated attack roll
     */
    private int pseudoAttack(AIUnit attacker, AIUnit defender) {
        int value = superiorIntellect.getValueLogic().attackValue(attacker, defender, this);
        attacker.attacks();
        defender.attacked();
        return value;
    }

    /**
     * this is performed if the next turn is also simulated
     */
    private void swapSides() {
        ArrayList temp = hostiles;
        hostiles = activeUnits;
        activeUnits = temp;
    }

    public ArrayList<AIUnit> getActiveUnits() {
        return activeUnits;
    }

    public ArrayList<AIUnit> getHostiles() {
        return hostiles;
    }

    public BattleMap getTheMatrix() {
        return theMatrix;
    }

    /**
     *  used if a completely new round is called by endTurn()
     */
    private ArrayList<AIUnit> resetedCopy(ArrayList<AIUnit> units, BattleMap map) {
        ArrayList<AIUnit> copy = new ArrayList();
        for (AIUnit unit : units) {
            AIUnit edi = new AIUnit(unit.getSpeed(), unit.getDefense(),
                    unit.getAttack(), unit.getRange(), unit.getHitPoints(),
                    "", null, unit.getX(), unit.getY(), true, true, true, unit.getAttackedCount());
            copy.add(edi);
            map.getTile(edi.getX(), edi.getY()).setUnit(edi);
        }
        return copy;
    }
}
