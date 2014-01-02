package tacticulous.game.domain;

import java.util.ArrayList;
import java.util.Collections;
import tacticulous.tira.ai.ArtificialIntelligence;

/**
 * Contains player-specific data, unit lists in particular.
 *
 * @author O
 */
public class Player {

    private boolean isAi;
    private int activeUnitIndex;
    private ArrayList<Unit> unitsWithActions;
    private ArrayList<Unit> units;
    private String name;
    private ArtificialIntelligence ai;

    public String getName() {
        return name;
    }

    /**
     * New round initialization. Adds all units to unitsWithActions.
     *
     * @see tacticulous.game.domain.Unit#newRound()
     */
    public void newRound() {
        unitsWithActions.clear();
        unitsWithActions.addAll(units);
        for (Unit unit : units) {
            unit.newRound();
        }
    }

    /**
     * Basic constructor.
     *
     * @param name name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.isAi = false;
        units = new ArrayList();
        unitsWithActions = new ArrayList();
        Collections.copy(units, unitsWithActions);
        activeUnitIndex = 0;
    }

    public ArrayList<Unit> getUnits() {
        return units;
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
     * ROBOT APOCALYPSE (not yet implemented.)
     *
     * @param ai type of ROBOT APOCALYPSE.
     */
    public void setAI(ArtificialIntelligence ai) {
        isAi = true;
        this.ai = ai;
    }

    public ArrayList<Unit> getUnitsWithActions() {
        return unitsWithActions;
    }

    /**
     * Cycles unit list forwards.
     *
     * @param game provides access to necessary data
     */
    public void nextUnit(Game game) {
        activeUnitIndex++;
        checkActiveUnitIndex();
        game.setActiveUnit(activeUnit());
    }

    /**
     * Cycles unit list backwards.
     *
     * @param game provides access to necessary data
     */
    public void prevUnit(Game game) {
        activeUnitIndex--;
        if (activeUnitIndex < 0) {
            activeUnitIndex = unitsWithActions.size() - 1;
        }
        game.setActiveUnit(activeUnit());
    }

    /**
     * Returns currently selected unit.
     *
     * @return null if no units with actions remain.
     */
    public Unit activeUnit() {
        if (unitsWithActions.isEmpty()) {
            return null;
        }
        checkActiveUnitIndex();
        return unitsWithActions.get(activeUnitIndex);
    }

    /**
     * Creates a few units for testing purposes.
     */
    public void testUnits() {
        units.add(new Unit(6, 5, 3, 3, 2, "Sharpshooter", this));
        units.add(new Unit(4, 3, 0, 5, 2, "Artillery", this));
        units.add(new Unit(9, 7, 0, 2, 1, "Fast", this));
    }

    /**
     * Creates a lot of units for testing purposes.
     */
    public void testUnits2(int multiplier) {
        for (int i = 0; i < multiplier; i++) {
            units.add(new Unit(6, 5, 3, 3, 2, "Sharpshooter", this));
            units.add(new Unit(4, 3, 0, 5, 2, "Artillery", this));
            units.add(new Unit(9, 7, 0, 2, 1, "Fast", this));
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
        unitsWithActions.remove(unit);
    }

    private void checkActiveUnitIndex() {
        if (activeUnitIndex >= unitsWithActions.size()) {
            activeUnitIndex = 0;
        }
    }

//    this was used by the old text UI.    
//    private UserInterface textUi;
}
