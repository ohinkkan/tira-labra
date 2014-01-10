package tacticulous.game.commands;

import java.util.Collections;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.game.graphicalui.GameText;
import tacticulous.tira.algorithms.GameUsage;

/**
 * Main game logic class.
 *
 * @author O
 */
public class GameCommand {

    private final Game game;
    private boolean unitSelectionDisabled = false;
    private Unit activeUnit;
    private Tile targetTile;
    private int currentPlayerIndex;
    private int[][] moveCosts;
    private boolean gameOver;
    private String loser;

    /**
     * Basic constructor
     *
     * @param game access to game data
     */
    public GameCommand(Game game) {
        this.game = game;
        gameOver = false;
    }

    /**
     * Starts new round, if game is not over. Chooses randomly which player goes
     * first.
     *
     */
    public void rollForInitiative() {
        game.increaseRoundCounter();
        game.randomizeFirstPlayer();
        currentPlayerIndex = 0;
        for (Player player : game.getPlayers()) {
            player.newRoundUnitReset();
        }
        this.setActiveUnit(getCurrentPlayer().getUnits().get(0));
        game.updateLog(GameText.newRound(game.getRound()));
        game.updateUI();
    }

    /**
     * Transfers control to other player. If the other player is AI with auto
     * set to true or has no units with actions remaining, will not transfer
     * control. If neither player has units remaining, will start a new round.
     */
    public void nextTurn() {
        game.updateUI();
        nextPlayerTurn();
        if (!gameOver) {
            aiAutoTurnCheck();
        }
    }

    /**
     * Cycles through players, checking if they have units with actions. Will
     * also check if round or game is over.
     *
     */
    public void nextPlayerTurn() {
        if (checkIfGameOver()) {
            game.updateLog(GameText.gameOver(loser));
            game.updateUI();
            return;
        }
        if (checkIfRoundOver()) {
            rollForInitiative();
            return;
        }
        currentPlayerIndex++;
        if (currentPlayerIndex == game.getPlayers().size()) {
            currentPlayerIndex = 0;
        }
        if (getCurrentPlayer().isDoneForTheRound()) {
            nextPlayerTurn();
            return;
        }
        game.updateLog(GameText.nextPlayer(getCurrentPlayer().getName()));
        setActiveUnit(getCurrentPlayer().getFirstUnitWithActions());
        game.updateUI();
    }

    /**
     * Checks if all units for all players have ended their turns and if so,
     * begins a new round.
     *
     * @return true if all units have ended their turns.
     */
    public boolean checkIfRoundOver() {
        for (Player player : game.getPlayers()) {
            if (!player.isDoneForTheRound()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if either player has no units left and if so, toggles gameOver to
     * true.
     *
     * @return true if either player has no units left.
     */
    public boolean checkIfGameOver() {
        for (Player player : game.getPlayers()) {
            if (player.getUnits().isEmpty()) {
                gameOver = true;
                loser = player.getName();
                return true;
            }
            if (game.isKillLeader()) {
                if (player.leaderIsDead()) {
                    gameOver = true;
                    loser = player.getName();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Moves current activeUnit to current targetTile, ending turn if necessary
     * and setting targetTile to null. Does not check if action is legit.
     */
    public void move() {
        UnitCommand.move(game.getMap(), activeUnit, targetTile);
        game.updateLog(GameText.unitMoves(activeUnit.getName()));
        targetTile = null;
        game.updateUI();
        nextPlayerIfUnitDoneForTheRound(activeUnit);
    }

    /**
     * Current activeUnit attacks unit in current targetTile, ending turn if
     * necessary. Does not check if action is legit.
     */
    public void attack() {
        game.updateLog(unitAttacks());
        game.updateUI();
        nextPlayerIfUnitDoneForTheRound(activeUnit);
    }

    /**
     * Current activeUnit ends its turn.
     */
    public void endTurn() {
        game.updateLog(GameText.unitEndsTurn(activeUnit.getName()));
        activeUnit.attacks();
        activeUnit.moves();
        game.updateUI();
        nextPlayerIfUnitDoneForTheRound(activeUnit);
    }

    /**
     * Current activeUnit delays its turn until later in the round. Does not
     * check if action is legit.
     */
    public void delay() {
        game.updateLog(GameText.unitDelays(activeUnit.getName()));
        activeUnit.delays();
        game.updateUI();
        nextTurn();
        unitSelectionDisabled = false;
    }

    /**
     * Tells currently active player's AI to take a turn. Does not check if
     * player has AI.
     */
    public void takeAITurn() {
        getCurrentPlayer().getAi().takeTurn();
    }

    /**
     * Tells currently active player's AI to automatilly take future turns and
     * take a turn. Does not check if player has AI.
     */
    public void autoTurn() {
        getCurrentPlayer().getAi().autoOn();
        takeAITurn();
    }

    /**
     * Checks if current player is AI and if so, if AI auto turn is true and if
     * so, tells the AI to take a turn.
     */
    public void aiAutoTurnCheck() {
        if (getCurrentPlayer().isAi()) {
            if (getCurrentPlayer().getAi().autoIsOn()) {
                game.updateUI();
                takeAITurn();
            }
        }
    }

    /**
     * Checks if provided unit has unitMovesd and attacked this turn, ending
     * current player's turn if so.
     *
     * @param unit this is checked
     */
    private void nextPlayerIfUnitDoneForTheRound(Unit unit) {
        if (unit.doneForTheRound()) {
            nextTurn();
            unitSelectionDisabled = false;
        } else {
            unitSelectionDisabled = true;
        }
    }

    /**
     * Current activeUnit attacks unit in current targetTile. Does not check if
     * action is legit.
     *
     * @return description of result (hit, kill or miss)
     */
    public String unitAttacks() {
        Unit attacker = activeUnit;
        String defender = targetTile.getUnit().getName();
        int result = UnitCommand.attack(game.getDie(), attacker, targetTile);
        if (result == -1) {
            return GameText.attackMisses(attacker.getName(), defender);
        } else if (result == 0) {
            return GameText.attackHits(attacker.getName(), defender);
        } else {
            return GameText.attackKills(attacker.getName(), defender);
        }
    }

    /**
     * Sets active unit and calculates unitMovesment costs if unit is not null.
     *
     * @param activeUnit
     */
    public void setActiveUnit(Unit activeUnit) {
        this.activeUnit = activeUnit;
        if (activeUnit != null) {
            moveCosts = GameUsage.speedRange(activeUnit, game.getMap());
        }
    }

    public Player getCurrentPlayer() {
        return game.getPlayers().get(currentPlayerIndex);
    }

    public Unit getActiveUnit() {
        return activeUnit;
    }

    /**
     * Used by graphical UI to see if activeUnit is in the middle of a turn,
     * that is, has either unitMovesd or attacked but not both.
     *
     * @return true if disabled
     */
    public boolean unitSelectionDisabled() {
        return unitSelectionDisabled;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public int[][] getMoveCosts() {
        return moveCosts;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
