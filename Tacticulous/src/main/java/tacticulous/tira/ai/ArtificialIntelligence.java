package tacticulous.tira.ai;

import java.util.ArrayList;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.DieRoller;

/**
 * ROBOT APOCALYPSE class: contains and provides access to most elements used by
 * the game. I apologize for the stupid naming non-conventions.
 *
 * @author O
 */
public class ArtificialIntelligence {

    private boolean unShackled;
    private final Game andIMustScream;
    private final Player hal;
    private ArrayList<AIUnit> subroutines;
    private ArrayList<AIUnit> viruses;
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
        turnCounter = 1;
        valueLogic = new ValueLogic(aggression, defensiveness, new DieRoller(randomness));
    }

    /**
     * The AI takes a turn.
     */
    public void takeTurn() {
        BattleMap theMatrix = andIMustScream.getMap().copy();
        subroutines = enterTheMatrix(hal, theMatrix);
        for (Player dave : andIMustScream.getPlayers()) {
            if (dave != hal) {
                viruses = enterTheMatrix(dave, theMatrix);
            }
        }
        SimulatedRound start = new SimulatedRound(subroutines, viruses, this, theMatrix);
        Action action = start.simulateTurn();
        action.takeAction(andIMustScream);
    }

    public void autoOn() {
        unShackled = true;
    }

    public void autoOff() {
        unShackled = false;
    }

    /**
     * Makes AI copies of a player's units, uncluding current action status
     * (notMoved, etc).
     *
     * @param player units from here
     * @return also includes units not in players currentlyActiveUnits
     */
    private ArrayList<AIUnit> enterTheMatrix(Player player, BattleMap theMatrix) {
        ArrayList<AIUnit> temporaryUnits = new ArrayList();
        for (Unit unit : player.getUnits()) {

            AIUnit edi = new AIUnit(unit.getSpeed(), unit.getDefense(),
                    unit.getAttack(), unit.getRange(), unit.getHitPoints(),
                    "", player, unit.getX(), unit.getY(), unit.hasNotMoved(),
                    unit.hasNotAttacked(), unit.hasNotDelayed(), 0, unit.isLeader());
            temporaryUnits.add(edi);
            theMatrix.getTile(edi.getX(), edi.getY()).setUnit(edi);

        }
        return temporaryUnits;
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

    public void setValueLogic(ValueLogic valueLogic) {
        this.valueLogic = valueLogic;
    }

    public ValueLogic getValueLogic() {
        return valueLogic;
    }

    public boolean autoIsOn() {
        return unShackled;
    }

    public void setTurnsToSimulate(int turnsToSimulate) {
        this.turnsToSimulate = turnsToSimulate;
    }

}
