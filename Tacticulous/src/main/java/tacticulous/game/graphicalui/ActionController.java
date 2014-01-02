package tacticulous.game.graphicalui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextArea;
import tacticulous.game.commands.Command;
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
    private Component map;
    private Container container;
    private JButton nextUnit, previousUnit, delay, endTurn, attack, move;
    private JTextArea displayActive, displayTarget, log;
    private boolean lockUnit;
    private boolean aiControls;
    private String logText;
    private Unit activeUnit;

    /**
     * Reacts to player's button clicks.
     *
     * @param ae triggering action event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {

        checkIfGameOver();

        Player player = game.getCurrentPlayer();
        activeUnit = game.getActiveUnit();

        if (ae.getSource() == nextUnit) {
            player.nextUnit(game);
        } else if (ae.getSource() == previousUnit) {
            player.prevUnit(game);
        } else if (ae.getSource() == move) {
            move();
        } else if (ae.getSource() == attack) {
            attack();
        } else if (ae.getSource() == endTurn) {
            endTurn();
        } else if (ae.getSource() == delay) {
            delay();
        }

        map.repaint();
        checkLegitActions();
        updateInfo();
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
     * @param active displays current unit's description
     * @param target displays current target tile's and unit's description
     * @param log displays game events.
     */
    public ActionController(Game game, Component map,
            JButton nextUnit, JButton previousUnit, JButton move,
            JButton attack, JButton delay, JButton endTurn,
            JTextArea active, JTextArea target, JTextArea log) {
        this.game = game;
        this.map = map;
        this.nextUnit = nextUnit;
        this.previousUnit = previousUnit;
        this.move = move;
        this.attack = attack;
        this.delay = delay;
        this.endTurn = endTurn;
        this.displayActive = active;
        this.displayTarget = target;
        this.log = log;
        logText = "";
        aiControls = false;
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
    public void updateInfo() {
        Unit unit = game.getActiveUnit();
        Tile tile = game.getTargetTile();
        displayActive.setText("active unit:\n" + unit);
        if (tile != null) {
            String targetTile = "target tile:\n" + tile;
            if (notTargetingSelf() && tile.getUnit() != null) {
                targetTile = targetTile + "\nchange to hit:" + Command.chanceToHit(unit, tile.getUnit()) + "%";
            }
            displayTarget.setText(targetTile);
        } else {
            displayTarget.setText("");
        }
    }

    /**
     * Maintains game log and prints new events.
     *
     * @param update text to print
     */
    public void updateLog(String update) {
        logText = logText + "\n" + update;
        log.setText(logText);
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
    }

    /**
     * Toggles human (or equivalent) controls off and AI controls on.
     */
    public void aiCommands() {
        game.getCommandList().removeAll();
        game.getCommandList().add(previousUnit);
        game.getCommandList().add(move);
        game.getCommandList().add(endTurn);
    }

    private void checkIfUnitDoneForTheRound(Unit unit) {
        if (unit.doneForTheRound()) {
            game.nextPlayer();
            updateLog(GameText.nextPlayer(game.getCurrentPlayer().getName()));
            lockUnit = false;
        } else {
            lockUnit = true;
        }
    }

    private void checkIfGameOver() {
        if (game.checkIfGameOver()) {
            disableAll();
            updateLog(GameText.gameOver());
        }
    }

    private boolean notTargetingSelf() {
        if (game.getTargetTile() == null) {
            return true;
        }
        return game.getActiveUnit() != game.getTargetTile().getUnit();
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

    private String unitAttacks() {
        Unit attacker = game.getActiveUnit();
        String defender = game.getTargetTile().getUnit().getName();
        int result = Command.attack(game.getDie(), attacker, game.getTargetTile());
        if (result == -1) {
            return GameText.attackMisses(attacker.getName(), defender);
        } else if (result == 0) {
            return GameText.attackHits(attacker.getName(), defender);
        } else {
            return GameText.attackKills(attacker.getName(), defender);
        }
    }

    private void move() {
        Command.move(game.getMap(), activeUnit, game.getTargetTile());
        game.setTargetTile(null);
        checkIfUnitDoneForTheRound(activeUnit);
    }

    private void attack() {
        updateLog(unitAttacks());
        checkIfUnitDoneForTheRound(activeUnit);
    }

    private void endTurn() {
        activeUnit.attacks();
        activeUnit.moves();
        checkIfUnitDoneForTheRound(activeUnit);
    }

    private void delay() {
        game.getActiveUnit().delays();
        game.nextPlayer();
        updateLog(GameText.nextPlayer(game.getCurrentPlayer().getName()));
        lockUnit = false;
    }

    private void checkUnitCyclingEnabled() {
        if (game.getCurrentPlayer().getUnitsWithActions().size() < 2 || lockUnit) {
            nextUnit.setEnabled(false);
            previousUnit.setEnabled(false);
        } else {
            nextUnit.setEnabled(true);
            previousUnit.setEnabled(true);
        }
    }

    private void checkMoveEnabled() {
        if (game.getTargetTile() != null) {
            if (Command.checkMove(game.getMap(), game.getActiveUnit(), game.getTargetTile().getX(), game.getTargetTile().getY(), game.getMoveCosts())) {
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
            if (game.getTargetTile() != null && Command.checkAttack(game.getMap(), game.getActiveUnit(), game.getTargetTile().getX(), game.getTargetTile().getY())) {
                attack.setEnabled(true);
            } else {
                attack.setEnabled(false);
            }
        } else {
            attack.setEnabled(false);
        }
    }

    private void checkDelayEnabled() {
        if (game.getActiveUnit().hasNotDelayed()) {
            delay.setEnabled(true);
        } else {
            delay.setEnabled(false);
        }
    }
}
