package tacticulous.game.graphicalui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;

/**
 * Main control and logic class for graphical user interface.
 *
 * @author O
 * @see tacticulous.game.graphicalui.MouseController
 */
public class ActionController implements ActionListener {

    private final Game game;
    private final GameCommand command;
    private final JComponent mapDisplay;
    private final JButton nextUnit, previousUnit, delay, endTurn, attack, move, takeTurn, autoTurn;
    private final JTextArea activeUnitDisplay, targetTileDisplay, gameLog;
    private final ArrayList<Unit> unitsWithActions;
    private JPanel commandList;
    private int activeUnitIndex;
    private boolean aiControls;

    /**
     * Reacts to player's button clicks.
     *
     * @param ae triggering action event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == nextUnit) {
            nextUnit(game);
        } else if (ae.getSource() == previousUnit) {
            prevUnit(game);
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
     * constructs game command buttons
     *
     * @param game access to game data
     * @param mapDisplay access to map display
     * @param activeUnitDisplay displays current unit's description
     * @param targetTileDisplay displays current target tile's and unit's
     * description
     * @param gameLog displays game events.
     */
    public ActionController(Container container, Game game, JComponent mapDisplay,
            JTextArea activeUnitDisplay, JTextArea targetTileDisplay,
            JTextArea gameLog) {
        this.game = game;
        this.command = game.command();
        this.mapDisplay = mapDisplay;
        this.nextUnit = new JButton("Next unit");
        this.previousUnit = new JButton("Previous unit");
        this.move = new JButton("Move");
        this.attack = new JButton("Attack");
        this.delay = new JButton("Delay turn");
        this.endTurn = new JButton("End turn");
        this.takeTurn = new JButton("Take turn");
        this.autoTurn = new JButton("Auto turn");
        this.activeUnitDisplay = activeUnitDisplay;
        this.targetTileDisplay = targetTileDisplay;
        this.gameLog = gameLog;
        commandList = new JPanel();
        container.add(commandList);
        aiControls = false;
        unitsWithActions = new ArrayList();
    }

    /**
     * Adds action listeners to all buttons.
     */
    public void addButtonListeners() {
        nextUnit.addActionListener(this);
        previousUnit.addActionListener(this);
        move.addActionListener(this);
        attack.addActionListener(this);
        delay.addActionListener(this);
        endTurn.addActionListener(this);
        takeTurn.addActionListener(this);
        autoTurn.addActionListener(this);
    }

    /**
     * Updates all UI Screens.
     */
    public void updateUI() {
        mapDisplay.paintImmediately(0, 0, mapDisplay.getHeight(), mapDisplay.getWidth());
        checkLegitActions();
        updateUnitDisplays();
        updateLogDisplay();
        if (game.command().isGameOver()) {
            disableAll();
        }
    }

    /**
     * Cycles unit list forwards.
     *
     * @param game provides access to necessary data
     */
    public void nextUnit(Game game) {
        activeUnitIndex++;
        checkActiveUnitIndex();
        game.command().setActiveUnit(activeUnit());
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
        game.command().setActiveUnit(activeUnit());
    }

    /**
     * Resets active unit index to 0 if necessary.
     */
    private void checkActiveUnitIndex() {
        if (activeUnitIndex >= unitsWithActions.size()) {
            activeUnitIndex = 0;
        }
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
     * Checks which commands are valid for the currently active player's
     * currently selected unit and current target tile
     */
    public void checkLegitActions() {
        updateUnitsWithActions();
        checkWhichControls();
        checkIfUnitCyclingEnabled();
        checkIfMoveEnabled();
        checkIfAttackEnabled();
        checkIfDelayEnabled();
    }

    /**
     * Updates the units with actions list to match the cruel reality.
     */
    private void updateUnitsWithActions() {
        unitsWithActions.clear();
        for (Unit unit : game.command().getCurrentPlayer().getUnits()) {
            if (!unit.doneForTheRound()) {
                unitsWithActions.add(unit);
            }
        }
    }

    /**
     * Updates active unit and target tile displays.
     */
    public void updateUnitDisplays() {
        Unit unit = command.getActiveUnit();
        Tile tile = command.getTargetTile();
        activeUnitDisplay.setText("active unit:\n" + unit);
        if (tile != null) {
            String targetTile = "target tile:\n" + tile;
            if (notTargetingSelf() && tile.getUnit() != null) {
                targetTile = targetTile + "\nchange to hit:" + UnitCommand.chanceToHit(unit, tile.getUnit()) + "%";
            }
            targetTileDisplay.setText(targetTile);
        } else {
            targetTileDisplay.setText("");
        }
    }

    /**
     * Updates game log display, printing new events.
     */
    public void updateLogDisplay() {
        gameLog.setText(game.getGameLog());
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
        takeTurn.setEnabled(false);
        autoTurn.setEnabled(false);
    }

    /**
     * Toggles human (or equivalent) controls on and AI controls off.
     */
    public void playerCommands() {
        commandList.removeAll();
        commandList.add(previousUnit);
        commandList.add(nextUnit);
        commandList.add(move);
        commandList.add(attack);
        commandList.add(delay);
        commandList.add(endTurn);
        commandList.repaint();
    }

    /**
     * Toggles human (or equivalent) controls off and AI controls on.
     */
    public void aiCommands() {
        commandList.removeAll();
        commandList.add(takeTurn);
        commandList.add(autoTurn);
        commandList.repaint();
    }

    /**
     * Used by the MouseController.
     *
     * @return true if attack button is enabled.
     */
    public boolean canAttack() {
        return attack.isEnabled();
    }

    /**
     * Used by the MouseController.
     *
     * @return true if move button is enabled.
     */
    public boolean canMove() {
        return move.isEnabled();
    }

    /**
     * Checks if current player is AI-controlled or not and chooses controls
     * accordingly.
     */
    private void checkWhichControls() {
        if (aiControls && !command.getCurrentPlayer().isAi()) {
            playerCommands();
            aiControls = false;
        }
        if (!aiControls && command.getCurrentPlayer().isAi()) {
            aiCommands();
            aiControls = true;
        }
    }

    /**
     * Checks if current unit is in the middle of an action or the only unit
     * with actions remaining this round and if so, disables the next/previous
     * unit buttons.
     */
    private void checkIfUnitCyclingEnabled() {
        if (unitsWithActions.size() < 2 || game.command().unitSelectionDisabled()) {
            nextUnit.setEnabled(false);
            previousUnit.setEnabled(false);
        } else {
            nextUnit.setEnabled(true);
            previousUnit.setEnabled(true);
        }
    }

    /**
     * Checks if active unit has move action remaining and if target square is
     * withing movement range. Otherwise move button is disabled.
     */
    private void checkIfMoveEnabled() {
        if (command.getTargetTile() != null) {
            if (UnitCommand.checkMove(game.getMap(), command.getActiveUnit(),
                    command.getTargetTile().getX(), command.getTargetTile().getY(),
                    command.getMoveCosts())) {
                move.setEnabled(true);
            } else {
                move.setEnabled(false);
            }
        } else {
            move.setEnabled(false);
        }
    }

    /**
     * Checks if active unit has attack action remaining and if target square is
     * withing attack range and contains another unit. Otherwise attack button
     * is disabled.
     */
    private void checkIfAttackEnabled() {
        if (command.getActiveUnit().hasNotAttacked()) {
            if (command.getTargetTile() != null
                    && UnitCommand.checkAttack(game.getMap(), command.getActiveUnit(),
                            command.getTargetTile().getX(), command.getTargetTile().getY())) {
                attack.setEnabled(true);
            } else {
                attack.setEnabled(false);
            }
        } else {
            attack.setEnabled(false);
        }
    }

    /**
     * Checks if active unit has delay action remaining. Otherwise attack button
     * is disabled.
     */
    private void checkIfDelayEnabled() {
        delay.setEnabled(command.getActiveUnit().hasNotDelayed());
    }

    /**
     * Checks if targeted tile is the same as active unit's tile.
     *
     * @return true if not same or targetTile is null.
     */
    private boolean notTargetingSelf() {
        if (command.getTargetTile() == null) {
            return true;
        }
        return command.getActiveUnit() != command.getTargetTile().getUnit();
    }
}
