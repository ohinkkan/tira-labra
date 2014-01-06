package tacticulous.tira.ai;

import java.util.ArrayList;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.DieRoller;
import tacticulous.tira.algorithms.GameUsage;
import tacticulous.tira.algorithms.Node;

/**
 * ROBOT APOCALYPSE class: contains and provides access to most elements used by
 * the game. I apologize for the stupid naming non-conventions.
 *
 * @author O
 */
public class ArtificialIntelligence {

    private boolean unShackled;
    private Game andIMustScream;
    private Player hal;
    private ArrayList<AIUnit> subroutines;
    private ArrayList<AIUnit> viruses;
    private BattleMap theMatrix;
    private int turnsToSimulate;
    private int turnCounter;
    private ValueLogic valueLogic;

    /**
     *
     * @param gateway provides access to necessary data
     * @param amIAlive the player who this AI is.
     * @param turnsToSimulate how many successive turns the AI simulates. Note
     * that values above 2 (or even 1) are at the moment horribly slow.
     * Exponential growth, etc.
     * @param randomness additive; randomly modifies the value of actions, to
     * increase unpredictability.
     * @param aggression multiplier; increases the value of offensive actions.
     * @param defensiveness multiplier; increases the value of defensive
     * actions.
     */
    public ArtificialIntelligence(Game gateway, Player amIAlive, int turnsToSimulate,
            int randomness, int aggression, int defensiveness) {
        this.andIMustScream = gateway;
        this.hal = amIAlive;
        unShackled = false;
        this.turnsToSimulate = turnsToSimulate;
        turnCounter = 0;
        valueLogic = new ValueLogic(aggression, defensiveness, new DieRoller(randomness));
    }

    /**
     * The AI takes a turn.
     */
    public void takeTurn() {
        theMatrix = andIMustScream.getMap().copy();
        subroutines = enterTheMatrix(hal);
        for (Player dave : andIMustScream.getPlayers()) {
            if (dave != hal) {
                viruses = enterTheMatrix(dave);
            }
        }
        SimulatedRound start = new SimulatedRound(subroutines, viruses, this, theMatrix);
        Action action = simulateTurn(start);
        action.takeAction(andIMustScream);
    }

    /**
     * Goes through all the possible action combinations for all units in
     * current game state. May recursively call itself to simulater multiple
     * successive turns.
     *
     * @param round does most of the heavy lifting
     * @return the bestest of all the actions in the whole world (that the AI
     * knows about... hopefully.)
     */
    public Action simulateTurn(SimulatedRound round) {
        Action optimal = new Action(Integer.MIN_VALUE / 2);
        for (AIUnit unit : round.getActiveUnits()) {

            if (!unit.doneForTheRound()) {
                if (unit.hasNotMoved() && unit.hasNotAttacked()) {
                    optimal = moveAndAttack(unit, round, optimal);
                }
                if (unit.hasNotMoved() && unit.hasNotAttacked()) {
                    optimal = attackAndMove(unit, round, optimal);
                }
                if (unit.hasNotMoved() && unit.hasNotDelayed()) {
                    optimal = moveAndDelay(unit, round, optimal);
                }
                if (unit.hasNotAttacked() && unit.hasNotDelayed()) {
                    optimal = attackAndDelay(unit, round, optimal);
                }
                if (unit.hasNotMoved() && !unit.hasNotDelayed()) {
                    optimal = moveAndEndTurn(unit, round, optimal);
                }
                if (unit.hasNotAttacked() && !unit.hasNotDelayed()) {
                    optimal = attackAndEndTurn(unit, round, optimal);
                }
                if (unit.hasNotDelayed()) {
                    optimal = delay(unit, round, optimal);
                }
                if (!unit.hasNotDelayed()) {
                    optimal = endTurn(unit, optimal);

                }

            }

        }
        return optimal;
    }

    public void autoOn() {
        unShackled = true;
    }

    public void autoOff() {
        unShackled = false;
    }

    /**
     * Simulates both moving and attacking during the same turn
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action moveAndAttack(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, andIMustScream.getMap())) {
            currentAction = round.moveAndAttack(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDATTACK);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates both attacking and moving during the same turn
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action attackAndMove(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, andIMustScream.getMap())) {
            if (!round.getTargetsInRange(unit, round.getHostiles()).isEmpty()) {
                currentAction = round.attackAndMove(unit, nodeToTile(node));
                currentAction.setType(ActionType.ATTACKANDMOVE);
                optimal = Action.getBetter(optimal, currentAction);
            }
        }
        return optimal;
    }

    /**
     * Simulates only moving and then delaying
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action moveAndDelay(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, andIMustScream.getMap())) {
            currentAction = round.moveAndDelay(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDDELAY);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates only attacking and then delaying
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action attackAndDelay(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        if (!round.getTargetsInRange(unit, round.getHostiles()).isEmpty()) {
            currentAction = round.attackAndDelay(unit);
            currentAction.setType(ActionType.ATTACKANDDELAY);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates moving and then ending turn
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action moveAndEndTurn(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        for (Node node : GameUsage.getTilesToMoveTo(unit, andIMustScream.getMap())) {
            currentAction = round.moveAndEndTurn(unit, nodeToTile(node));
            currentAction.setType(ActionType.MOVEANDENDTURN);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates attacking and then ending turn
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action attackAndEndTurn(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        if (!round.getTargetsInRange(unit, round.getHostiles()).isEmpty()) {
            currentAction = round.attackAndEndTurn(unit);
            currentAction.setType(ActionType.ATTACKANDENDTURN);
            optimal = Action.getBetter(optimal, currentAction);
        }
        return optimal;
    }

    /**
     * Simulates only delaying this turn
     *
     * @param unit unit taking the simulated action
     * @param round contains necessary methods and data
     * @param optimal highest value
     * @return
     */
    private Action delay(AIUnit unit, SimulatedRound round, Action optimal) {
        Action currentAction;
        currentAction = round.delay(unit);
        currentAction.setType(ActionType.DELAY);
        optimal = Action.getBetter(optimal, currentAction);
        return optimal;
    }

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
     * Makes AI copies of a player's units, uncluding current action status
     * (notMoved, etc).
     *
     * @param player units from here
     * @return also includes units not in players currentlyActiveUnits
     */
    private ArrayList<AIUnit> enterTheMatrix(Player player) {
        ArrayList<AIUnit> temporaryUnits = new ArrayList();
        for (Unit unit : player.getUnits()) {

            AIUnit edi = new AIUnit(unit.getSpeed(), unit.getDefense(),
                    unit.getAttack(), unit.getRange(), unit.getHitPoints(),
                    "", player, unit.getX(), unit.getY(), unit.hasNotMoved(),
                    unit.hasNotAttacked(), unit.hasNotDelayed(), 0);
            temporaryUnits.add(edi);
            theMatrix.getTile(edi.getX(), edi.getY()).setUnit(edi);

        }
        return temporaryUnits;
    }

    public BattleMap connectToTheMatrix() {
        return theMatrix;
    }

    public void diveDeeper() {
        turnCounter++;
    }

    public void unPlug() {
        turnCounter--;
    }

    public int layer() {
        return turnCounter;
    }

    public int depth() {
        return turnsToSimulate;
    }

    public ValueLogic getValueLogic() {
        return valueLogic;
    }

    public boolean autoIsOn() {
        return unShackled;
    }

}
