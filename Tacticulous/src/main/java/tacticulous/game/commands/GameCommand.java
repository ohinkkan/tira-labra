package tacticulous.game.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;
import tacticulous.game.graphicalui.GameText;

/**
 *
 * @author O
 */
public class GameCommand {

    private Game game;
    private boolean lockUnit = false;

    public GameCommand(Game game) {
        this.game = game;
    }

    public boolean unitLocked() {
        return lockUnit;
    }

    public void setLockUnit(boolean lockUnit) {
        this.lockUnit = lockUnit;
    }

    public void move() {
        UnitCommand.move(game.getMap(), game.getActiveUnit(), game.getTargetTile());
        game.setTargetTile(null);
        checkIfUnitDoneForTheRound(game.getActiveUnit());
    }

    public void attack() {
        game.updateLog(unitAttacks());
        checkIfUnitDoneForTheRound(game.getActiveUnit());
    }

    public void endTurn() {
        game.getActiveUnit().attacks();
        game.getActiveUnit().moves();
        checkIfUnitDoneForTheRound(game.getActiveUnit());
    }

    public void delay() {
        game.getActiveUnit().delays();
        game.nextPlayer();
        lockUnit = false;
    }

    public void takeAITurn() {
         System.out.println(game.getCurrentPlayer().getName());
        game.getCurrentPlayer().getAi().takeTurn();

    }

    public void autoTurn() {
        game.getCurrentPlayer().getAi().autoOn();
        if (!allAICheck()) {
            takeAITurn();
        } else {
            autoRun();
        }
    }

    private void autoRun() {
        while (!checkIfGameOver()) {
            takeAITurn();
            game.getActions().updateUI();
        }
    }

    private boolean allAICheck() {
        for (Player player : game.getPlayers()) {
            if (!player.isAi()) {
                return false;
            }
        }
        for (Player player : game.getPlayers()) {
            if (!player.getAi().autoIsOn()) {
                return false;
            }
        }
        return true;
    }

    private void checkIfUnitDoneForTheRound(Unit unit) {
        if (unit.doneForTheRound()) {
            game.getCurrentPlayer().getUnitsWithActions().remove(unit);
            game.nextPlayer();
            lockUnit = false;
        } else {
            lockUnit = true;
        }
    }

    public boolean checkIfGameOver() {
        if (game.checkIfGameOver()) {
            game.updateLog(GameText.gameOver());
            return true;
        }
        return false;
    }

    private String unitAttacks() {
        Unit attacker = game.getActiveUnit();
        String defender = game.getTargetTile().getUnit().getName();
        int result = UnitCommand.attack(game.getDie(), attacker, game.getTargetTile());
        if (result == -1) {
            return GameText.attackMisses(attacker.getName(), defender);
        } else if (result == 0) {
            return GameText.attackHits(attacker.getName(), defender);
        } else {
            return GameText.attackKills(attacker.getName(), defender);
        }
    }
}
