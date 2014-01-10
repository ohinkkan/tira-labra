package tacticulous.game.domain;

import java.awt.Color;
import tacticulous.tira.ai.ArtificialIntelligence;
import tacticulous.tira.datastructure.TacList;

/**
 * Contains player-specific data, unit lists in particular.
 *
 * @author O
 */
public class Player {

    private boolean isAi;
    private TacList<Unit> units;
    private String name;
    private ArtificialIntelligence ai;
    private Game game;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public ArtificialIntelligence getAi() {
        return ai;
    }

    /**
     * New round initialization. Resets all unit actions.
     *
     * @see tacticulous.game.domain.Unit#newRound()
     */
    public void newRoundUnitReset() {
        for (Unit unit : units) {
            unit.newRound();
        }
    }

    /**
     * Basic constructor.
     *
     * @param name name of the player.
     * @param color color of player units on map
     */
    public Player(String name, Color color) {
        this.name = name;
        this.isAi = false;
        units = new TacList(10);
        this.color = color;
    }

    /**
     * Goes through all the player's units and checks if any have not moved or
     * attacked.
     *
     * @return true if no units have move or attack actions remaining, false
     * otherwise.
     */
    public boolean isDoneForTheRound() {
        for (Unit unit : units) {
            if (!unit.doneForTheRound()) {
                return false;
            }
        }
        return true;
    }

    public TacList<Unit> getUnits() {
        return units;
    }

    /**
     * Finds the first unit in the unit list with unused move or attack action.
     *
     * @return null if no units have actions remaining.
     */
    public Unit getFirstUnitWithActions() {
        for (Unit unit : units) {
            if (!unit.doneForTheRound()) {
                return unit;
            }
        }
        return null;
    }

    /**
     * Checks if player is controlled by an AI.
     *
     * @return true if player is AI controlled.
     */
    public boolean isAi() {
        return isAi;
    }

    /**
     * ROBOT APOCALYPSE (apocalypse not yet fully implemented). Makes the player
     * AI-controlled.
     *
     * @param ai Artifical Intelligence of ROBOT APOCALYPSE. Null will toggle
     * the ai off.
     *
     */
    public void setAI(ArtificialIntelligence ai) {
        isAi = ai != null;
        this.ai = ai;
    }

    /**
     * Adds a set of three units to the player's unit list for quickstart games
     * and some tests.
     *
     * @param multiplier how many sets of three units are created
     */
    public void quickStartUnits(int multiplier) {
        for (int i = 0; i < multiplier; i++) {
            units.add(new Unit(6, 2, 3, 3, 2, "Sharpshooter", this));
            units.add(new Unit(4, 0, 0, 5, 2, "Artillery", this));
            units.add(new Unit(9, 4, 0, 2, 1, "Scout", this));
        }
    }

    /**
     * Adds unit to unit list.
     *
     * @param unit
     */
    public void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Removes a destroyed unit from unit lists.
     *
     * @param unit
     */
    public void kill(Unit unit) {
        units.remove(unit);
        if (game != null) {
            game.getMap().getTile(unit.getX(), unit.getY()).putCorpse(unit);
        }
    }

    /**
     * Checks if player's leader unit is dead (not in unit list.)
     *
     * @return false if leader is in unit list.
     */
    public boolean leaderIsDead() {
        for (Unit unit : units) {
            if (unit.isLeader()) {
                return false;
            }
        }
        return true;
    }
}
