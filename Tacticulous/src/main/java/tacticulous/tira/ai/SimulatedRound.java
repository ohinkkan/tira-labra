package tacticulous.tira.ai;

//import java.util.ArrayList;
import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.tira.algorithms.GameUsage;
import tacticulous.tira.datastructure.Node;
import tacticulous.tira.datastructure.TacList;

/**
 *
 * Main control class for simulated actions during a simulated round
 *
 * @author O
 */
public class SimulatedRound {

    private TacList<AIUnit> activeUnits;
    private TacList<AIUnit> hostiles;
    private final ArtificialIntelligence superiorIntellect;
    private final BattleMap theMatrix;

    public SimulatedRound(TacList<AIUnit> activeUnits, TacList<AIUnit> hostiles,
            ArtificialIntelligence mind, BattleMap theMatrix) {
        this.activeUnits = activeUnits;
        this.hostiles = hostiles;
        this.superiorIntellect = mind;
        this.theMatrix = theMatrix;
    }

    /**
     * Goes through all the possible action combinations for all units in
     * current game state. May recursively call itself to simulater multiple
     * successive turns.
     *
     * @return null if all units done for the round, otherwise the bestest of
     * all the actions in the whole world (that the AI knows about...
     * hopefully.)
     */
    public Action simulateTurn() {
        Action optimal = new Action(Integer.MIN_VALUE / 2);
        for (AIUnit unit : this.getActiveUnits()) {
            if (!unit.doneForTheRound()) {
                if (unit.hasNotMoved() && unit.hasNotAttacked()) {
                    optimal = allMovesAndAttacks(unit, optimal);
                }
                if (unit.hasNotMoved() && unit.hasNotAttacked()) {
                    optimal = allAttacksAndMoves(unit, optimal);
                }
                if (unit.hasNotMoved() && unit.hasNotDelayed()) {
                    optimal = allMovesAndDelay(unit, optimal);
                }
                if (unit.hasNotAttacked() && unit.hasNotDelayed()) {
                    optimal = allAttacksAndDelay(unit, optimal);
                }
                if (unit.hasNotMoved() && !unit.hasNotDelayed()) {
                    optimal = allMovesAndEndTurn(unit, optimal);
                }
                if (unit.hasNotAttacked() && !unit.hasNotDelayed()) {
                    optimal = allAttacksAndEndTurn(unit, optimal);
                }
                if (unit.hasNotDelayed()) {
                    optimal = onlyDelay(unit, optimal);
                }
                if (!unit.hasNotDelayed()) {
                    optimal = endTurn(unit, optimal);
                }
            }
        }
        return optimal;
    }

    /**
     * Checks which enemiee are in unit's range
     *
     * @param terminator
     * @param potentials
     * @return list of all enemiee in unit's range
     */
    public TacList<AIUnit> getTargetsInRange(Unit terminator, TacList<AIUnit> potentials) {
        TacList<AIUnit> targets = new TacList(potentials.size());
        for (AIUnit unit : potentials) {
            if (!UnitCommand.checkNotInRange(terminator, unit.getX(), unit.getY())) {
                targets.add(unit);
            }
        }
        return targets;
    }

    /**
     * Simulates both moving and attacking during the same turn
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allMovesAndAttacks(AIUnit unit, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, theMatrix)) {
            currentAction = singleMoveAndAllAttacks(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDATTACK);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     *
     * @param unit
     * @param tile
     * @return
     */
    public Action singleMoveAndAllAttacks(AIUnit unit, Tile tile) {
        Action action = new Action(0, unit);
        Tile undoMovement = tileFromUnit(unit);
        if (tile.getUnit() == null) {
            action = singleMove(unit, tile);
            if (!getTargetsInRange(unit, hostiles).isEmpty()) {
                Action bestAttack = new Action(Integer.MIN_VALUE / 2);
                for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                    bestAttack = Action.getBetter(bestAttack, singleAttack(unit, target));
                    bestAttack.updateValue(checkIfShouldSimulateNextTurn());
                    target.undoAttacked();
                    unit.undoAttack();
                }
                action.copyAttackCoordinates(bestAttack);
            } else {
                action.negate();
            }
            singleMove(unit, undoMovement);
            unit.undoMove();
        } else {
            action.negate();
        }
        return action;
    }

    /**
     * Simulates both attacking and moving during the same turn
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allAttacksAndMoves(AIUnit unit, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, theMatrix)) {
            if (!getTargetsInRange(unit, getHostiles()).isEmpty()) {
                currentAction = allAttacksAndSingleMove(unit, nodeToTile(node));
                currentAction.setType(ActionType.ATTACKANDMOVE);
                optimal = Action.getBetter(optimal, currentAction);
            }
        }
        return optimal;
    }

    /**
     *
     * @param unit
     * @param tile
     * @return
     */
    public Action allAttacksAndSingleMove(AIUnit unit, Tile tile) {
        Tile undoMovement = tileFromUnit(unit);
        Action currentOptimal = new Action(Integer.MIN_VALUE / 2);
        if (tile.getUnit() == null) {
            for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                Action action = singleAttack(unit, target);
                action.updateValue(singleMove(unit, tile));
                action.updateValue(checkIfShouldSimulateNextTurn());
                action.setMovementCoordinates(tile.getX(), tile.getY());
                target.undoAttacked();
                unit.undoAttack();
                singleMove(unit, undoMovement);
                unit.undoMove();
                currentOptimal = Action.getBetter(currentOptimal, action);
            }
        } else {
            currentOptimal.negate();
        }
        return currentOptimal;
    }

    /**
     * Simulates only moving and then delaying
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allMovesAndDelay(AIUnit unit, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, theMatrix)) {
            currentAction = singleMoveAndDelay(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDDELAY);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     *
     * @param unit
     * @param tile
     * @return
     */
    public Action singleMoveAndDelay(AIUnit unit, Tile tile) {
        Tile undo = tileFromUnit(unit);
        Action currentAction = new Action(0, unit);
        if (tile.getUnit() == null) {
            currentAction = singleMove(unit, tile);
            currentAction.updateValue(delay(unit));
            currentAction.updateValue(checkIfShouldSimulateNextTurn());
            singleMove(unit, undo);
            unit.undoMove();
            unit.undoDelay();
        } else {
            currentAction.negate();
        }
        return currentAction;
    }

    /**
     * Simulates moving and then ending turn
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allMovesAndEndTurn(AIUnit unit, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, theMatrix)) {
            currentAction = singleMoveAndEndTurn(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDENDTURN);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    public Action singleMoveAndEndTurn(AIUnit unit, Tile tile) {
        Tile undoMovement = tileFromUnit(unit);
        Action action = new Action(0, unit);
        if (tile.getUnit() == null) {
            action = (singleMove(unit, tile));
            checkIfShouldSimulateNextTurn();
            singleMove(unit, undoMovement);
            unit.undoMove();
        } else {
            action.negate();
        }
        return action;
    }

    /**
     * Simulates only attacking and then delaying
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allAttacksAndDelay(AIUnit unit, Action optimal) {
        if (!getTargetsInRange(unit, hostiles).isEmpty()) {
            Action currentAction = new Action(Integer.MIN_VALUE / 2);
            for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                Action action = singleAttack(unit, target);
                action.updateValue(delay(unit));
                action.updateValue(checkIfShouldSimulateNextTurn());
                target.undoAttacked();
                unit.undoAttack();
                unit.undoDelay();
                currentAction = Action.getBetter(currentAction, action);
            }
            currentAction.setType(ActionType.ATTACKANDDELAY);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates attacking and then ending turn
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action allAttacksAndEndTurn(AIUnit unit, Action optimal) {
        if (!getTargetsInRange(unit, hostiles).isEmpty()) {
            Action currentAction = new Action(Integer.MIN_VALUE / 2);
            for (AIUnit target : getTargetsInRange(unit, hostiles)) {
                Action action = (singleAttack(unit, target));
                action.updateValue(checkIfShouldSimulateNextTurn());
                target.undoAttacked();
                unit.undoAttack();
                currentAction = Action.getBetter(currentAction, action);
            }
            currentAction.setType(ActionType.ATTACKANDENDTURN);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates only delaying this turn
     *
     * @param unit unit taking the simulated action
     * @param current optimal action
     * @return the action with highest value, possibly original optimal action
     */
    private Action onlyDelay(AIUnit unit, Action optimal) {
        Action currentAction;
        currentAction = delay(unit);
        currentAction.updateValue(checkIfShouldSimulateNextTurn());
        unit.undoDelay();
        currentAction.setType(ActionType.DELAY);
        optimal = Action.getBetter(optimal, currentAction);
        return optimal;
    }

    /**
     *
     * @param unit
     * @return
     */
    public Action delay(AIUnit unit) {
        Action action = new Action(superiorIntellect.getValueLogic().delayValue(unit, this), unit);
        unit.delays();
        return action;
    }

    /**
     * performs the actual simulated move
     */
    public Action singleMove(AIUnit unit, Tile tile) {
        Action action = new Action(superiorIntellect.getValueLogic().movementValue(unit, tile, this), unit);
        UnitCommand.move(theMatrix, unit, tile);
        action.setMovementCoordinates(unit.getX(), unit.getY());
        return action;
    }

    /**
     * performs the actual simulated attack
     */
    public Action singleAttack(AIUnit unit, AIUnit target) {
        Action action = new Action(pseudoAttack(unit, target), unit);
        action.setAttackCoordinates(target.getX(), target.getY());
        return action;
    }

    /**
     * ends current turn, only if no other action is available.
     */
    private Action endTurn(AIUnit unit, Action optimal) {
        Action currentAction = new Action(Integer.MIN_VALUE / 2, unit);
        currentAction.setType(ActionType.ENDTURN);
        if (optimal.getType() == null) {
            return currentAction;
        }
        optimal = Action.getBetter(optimal, currentAction);
        return optimal;
    }

    /**
     * simulated end of turn; may recursively call for a new simulated turn or
     * even new round
     *
     * @return Action with a flipped value of next turn's optimal action, value
     * = 0 if not simulated
     */
    public Action checkIfShouldSimulateNextTurn() {
        if (superiorIntellect.layer() < superiorIntellect.depth()) {
            superiorIntellect.diveDeeper();
            Action opponentAction;
            swapSides();
            if (checkIfSimulatedRoundOver()) {
                SimulatedRound next = newRound();
                opponentAction = next.simulateTurn();
            } else {
                opponentAction = this.simulateTurn();
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
     *
     * @return true if no active or hostile units have actions remaining
     */
    public boolean checkIfSimulatedRoundOver() {
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
        return new SimulatedRound(resetedCopy(activeUnits, newMatrix),
                resetedCopy(hostiles, newMatrix), superiorIntellect, newMatrix);
    }

    /**
     *
     * @param unit
     * @return
     */
    private Tile tileFromUnit(AIUnit unit) {
        return theMatrix.getTile(unit.getX(), unit.getY());
    }

    /**
     * simulated attack roll
     */
    private int pseudoAttack(AIUnit attacker, AIUnit defender) {
        int value = superiorIntellect.getValueLogic().attackValue(attacker, defender, this);
        attacker.attacks();
        defender.attacked();
        return value;
    }

    /**
     * Converts the nodes from the pathfinding algorithm to tiles
     *
     * @param node coordinates from here
     * @return tile with the same X and Y coordinates as the node.
     */
    private Tile nodeToTile(Node node) {
        Tile tile = theMatrix.getTile(node.getX(), node.getY());
        return tile;
    }

    /**
     * swaps active and hostile unit lists if the next turn is also simulated
     */
    private void swapSides() {
        TacList temp = hostiles;
        hostiles = activeUnits;
        activeUnits = temp;
    }

    public TacList<AIUnit> getActiveUnits() {
        return activeUnits;
    }

    public TacList<AIUnit> getHostiles() {
        return hostiles;
    }

    public BattleMap getTheMatrix() {
        return theMatrix;
    }

    /**
     * resets unit acionts if a completely new round is called by
     * checkIfShouldSimulateNextTurn()
     */
    private TacList<AIUnit> resetedCopy(TacList<AIUnit> units, BattleMap map) {
        TacList<AIUnit> copy = new TacList(units.size());
        for (AIUnit unit : units) {
            AIUnit edi = new AIUnit(unit.getSpeed(), unit.getDefense(),
                    unit.getAttack(), unit.getRange(), unit.getHitPoints(),
                    "", null, unit.getX(), unit.getY(), true, true, true,
                    unit.getAttackedCount(), unit.isLeader());
            copy.add(edi);
            map.getTile(edi.getX(), edi.getY()).setUnit(edi);
        }
        return copy;
    }
}
