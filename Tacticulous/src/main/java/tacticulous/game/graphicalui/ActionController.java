package tacticulous.game.graphicalui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;

/**
 * Main control and logic class for graphical user interface.
 *
 * @author O
 * @see tacticulous.game.graphicalui.MouseController
 */
public class ActionController implements ActionListener {

    private Game game;
    private JComponent map;
    private JButton nextUnit, previousUnit, delay, endTurn, attack, move, takeTurn, autoTurn;
    private JTextArea displayActive, displayTarget, log;

    private boolean aiControls;

    /**
     * Reacts to player's button clicks.
     *
     * @param ae triggering action event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

        if (game.command().checkIfGameOver()) {
            disableAll();
            return;
        }

        Player player = game.getCurrentPlayer();

        if (ae.getSource() == nextUnit) {
            player.nextUnit(game);
        } else if (ae.getSource() == previousUnit) {
            player.prevUnit(game);
        } else if (ae.getSource() == move) {
            game.command().move();
        } else if (ae.getSource() == attack) {
            game.command().attack();
        } else if (ae.getSource() == endTurn) {
            game.command().endTurn();
        } else if (ae.getSource() == delay) {
            game.command().delay();
        } else if (ae.getSource() == takeTurn) {
            game.command().takeAITurn();
        } else if (ae.getSource() == autoTurn) {
            game.command().autoTurn();
        }

        updateUI();
    }

    /**
     *
     * @param game provides access to necessary data
     * @param map provides access to map display
     * @param nextUnit select next unit button
     * @param previousUnit select previous unit button
     * @param move move unit button
     * @param attack attack target button
     * @param delay delay acting for now button
     * @param endTurn end current unit's turn button
     * @param takeTurn AI player takes turn
     * @param autoTurn AI player will forevermore take turn automatically
     * @param active displays current unit's description
     * @param target displays current target tile's and unit's description
     * @param log displays game events.
     */
    public ActionController(Game game, JComponent map,
            JButton nextUnit, JButton previousUnit, JButton move,
            JButton attack, JButton delay, JButton endTurn,
            JButton takeTurn, JButton autoTurn,
            JTextArea active, JTextArea target, JTextArea log) {
        this.game = game;
        this.map = map;
        this.nextUnit = nextUnit;
        this.previousUnit = previousUnit;
        this.move = move;
        this.attack = attack;
        this.delay = delay;
        this.endTurn = endTurn;
        this.takeTurn = takeTurn;
        this.autoTurn = autoTurn;
        this.displayActive = active;
        this.displayTarget = target;
        this.log = log;
        aiControls = false;
    }

    public void updateUI() {
        map.paintImmediately(0,0,map.getHeight(),map.getWidth());
        checkLegitActions();
        updateUnitDisplays();
        updateLogDisplay();
    }

    /**
     * Checks which commands are valid for the currently active player's
     * currently selected unit and current target tile
     */
    public void checkLegitActions() {
        checkIfPlayerIsAi();
        checkUnitCyclingEnabled();
        checkMoveEnabled();
        checkAttackEnabled();
        checkDelayEnabled();
    }

    /**
     * Updates active unit and target tile displays.
     */
    public void updateUnitDisplays() {
        Unit unit = game.getActiveUnit();
        Tile tile = game.getTargetTile();
        displayActive.setText("active unit:\n" + unit);
        if (tile != null) {
            String targetTile = "target tile:\n" + tile;
            if (notTargetingSelf() && tile.getUnit() != null) {
                targetTile = targetTile + "\nchange to hit:" + UnitCommand.chanceToHit(unit, tile.getUnit()) + "%";
            }
            displayTarget.setText(targetTile);
        } else {
            displayTarget.setText("");
        }
    }

    /**
     * Updates game log display, printing new events.
     */
    public void updateLogDisplay() {
        log.setText(game.getGameLog());
    }

    /**
     * Disables all controls.
     */
    public void disableAll() {
        nextUnit.setEnabled(false);
        previousUnit.setEnabled(false);
        delay.setEnabled(false);
        endTurn.setEnabled(false);
        attack.setEnabled(false);
        move.setEnabled(false);
    }

    /**
     * Toggles human (or equivalent) controls on and AI controls off.
     */
    public void playerCommands() {
        game.getCommandList().removeAll();
        game.getCommandList().add(previousUnit);
        game.getCommandList().add(nextUnit);
        game.getCommandList().add(move);
        game.getCommandList().add(attack);
        game.getCommandList().add(delay);
        game.getCommandList().add(endTurn);
        game.getCommandList().repaint();
    }

    /**
     * Toggles human (or equivalent) controls off and AI controls on.
     */
    public void aiCommands() {
        game.getCommandList().removeAll();
        game.getCommandList().add(takeTurn);
//        game.getCommandList().add(autoTurn);
        game.getCommandList().repaint();
    }

    public boolean canAttack() {
        return attack.isEnabled();
    }

    public boolean canMove() {
        return move.isEnabled();
    }

    private void checkIfPlayerIsAi() {
        if (aiControls && !game.getCurrentPlayer().isAi()) {
            playerCommands();
            aiControls = false;
        }
        if (!aiControls && game.getCurrentPlayer().isAi()) {
            aiCommands();
            aiControls = true;
        }
    }

    private void checkUnitCyclingEnabled() {
        if (game.getCurrentPlayer().getUnitsWithActions().size() < 2 || game.command().unitLocked()) {
            nextUnit.setEnabled(false);
            previousUnit.setEnabled(false);
        } else {
            nextUnit.setEnabled(true);
            previousUnit.setEnabled(true);
        }
    }

    private void checkMoveEnabled() {
        if (game.getTargetTile() != null) {
            if (UnitCommand.checkMove(game.getMap(), game.getActiveUnit(),
                    game.getTargetTile().getX(), game.getTargetTile().getY(),
                    game.getMoveCosts())) {
                move.setEnabled(true);
            } else {
                move.setEnabled(false);
            }
        } else {
            move.setEnabled(false);
        }
    }

    private void checkAttackEnabled() {
        if (game.getActiveUnit().hasNotAttacked()) {
            if (game.getTargetTile() != null
                    && UnitCommand.checkAttack(game.getMap(), game.getActiveUnit(),
                            game.getTargetTile().getX(), game.getTargetTile().getY())) {
                attack.setEnabled(true);
            } else {
                attack.setEnabled(false);
            }
        } else {
            attack.setEnabled(false);
        }
    }

    private void checkDelayEnabled() {
        delay.setEnabled(game.getActiveUnit().hasNotDelayed());
    }

    private boolean notTargetingSelf() {
        if (game.getTargetTile() == null) {
            return true;
        }
        return game.getActiveUnit() != game.getTargetTile().getUnit();
    }
}
