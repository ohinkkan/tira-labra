package tacticulous.tira.ai;

import tacticulous.game.domain.Game;
import tacticulous.game.domain.Unit;

/**
 * Stores coordinates for the unit, target tile and movement in addition to the
 * value and type of one specific action.
 *
 * @author O
 */
public class Action {

    private int value;
    private ActionType type;
    private int unitX;
    private int unitY;
    private int attackX;
    private int attackY;
    private int movementX;
    private int movementY;

    public Action(int value, AIUnit unit) {
        this.value = value;
        unitX = unit.getX();
        unitY = unit.getY();
    }

    public Action(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Used for increasing or decreasing the value of an action from another
     * action.
     *
     * @param action
     */
    public void updateValue(Action action) {
        value = value + action.getValue();
    }

    /**
     * Reduces action's value to Integer.MIN_VALUE / 2.
     */
    public void negate() {
        value = Integer.MIN_VALUE / 2;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    /**
     * Finds the action with highest value from several actions.
     *
     * @param actions one or more actions to compere.
     * @return The action with highest value.
     */
    public static Action getBetter(Action... actions) {
        Action best = new Action(Integer.MIN_VALUE / 2);
        for (Action action : actions) {
            if (action.getValue() > best.getValue()) {
                best = action;
            }
        }
        return best;
    }

    /**
     * Changes the value of an action to its additive inverse.
     */
    public void swapValue() {
        value = -value;
    }

    @Override
    public String toString() {
        return "value=" + value + "\ntype=" + type //                + "\nunitX="
                //                + unitX + "\nunitY=" + unitY + "\nattackX="
                //                + attackX + "\nattackY=" + attackY + "\nmovementX="
                //                + movementX + "\nmovementY=" + movementY
                ;
    }

    void setMovementCoordinates(int x, int y) {
        movementX = x;
        movementY = y;
    }

    void setAttackCoordinates(int x, int y) {
        attackX = x;
        attackY = y;
    }

    /**
     * Copies attack coordinates from another action.
     *
     * @param action copied from this action
     */
    void copyAttackCoordinates(Action action) {
        this.attackX = action.attackX;
        this.attackY = action.attackY;
    }

    /**
     * Copies movement coordinates from another action.
     *
     * @param action copied from this action
     */
    void copyMovemenCoordinates(Action action) {
        this.movementX = action.movementX;
        this.movementY = action.movementY;
    }

    /**
     * The AI actually (finally) takes this action in real game (irg).
     *
     * @param game
     */
    public void takeAction(Game game) {
        if (type == null) {
            game.rollForInitiative();
            return;
        }
        Unit unit = game.getMap().getTile(unitX, unitY).getUnit();
        game.setActiveUnit(unit);
        if (type == ActionType.ATTACKANDDELAY) {
            game.setTargetTile(game.getMap().getTile(attackX, attackY));
            game.command().attack();
            game.command().delay();
        } else if (type == ActionType.ATTACKANDENDTURN) {
            game.setTargetTile(game.getMap().getTile(attackX, attackY));
            game.command().attack();
        } else if (type == ActionType.ATTACKANDMOVE) {
            game.setTargetTile(game.getMap().getTile(attackX, attackY));
            game.command().attack();
            game.setTargetTile(game.getMap().getTile(movementX, movementY));
            game.command().move();
        } else if (type == ActionType.MOVEANDATTACK) {
            game.setTargetTile(game.getMap().getTile(movementX, movementY));
            game.command().move();
            game.setTargetTile(game.getMap().getTile(attackX, attackY));
            game.command().attack();
        } else if (type == ActionType.MOVEANDDELAY) {
            game.setTargetTile(game.getMap().getTile(movementX, movementY));
            game.command().move();
            game.command().delay();
        } else if (type == ActionType.MOVEANDENDTURN) {
            game.setTargetTile(game.getMap().getTile(movementX, movementY));
            game.command().move();
        } else if (type == ActionType.DELAY) {
            game.command().delay();
        } else if (type == ActionType.ENDTURN) {
            game.command().endTurn();
        }
    }

}
