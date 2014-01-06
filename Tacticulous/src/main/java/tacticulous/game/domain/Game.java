package tacticulous.game.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.graphicalui.ActionController;
import tacticulous.game.graphicalui.GameText;
import tacticulous.game.graphicalui.StartupUI;
import tacticulous.game.graphicalui.UiView;
import tacticulous.game.utility.*;
import tacticulous.tira.ai.ArtificialIntelligence;
import tacticulous.tira.algorithms.GameUsage;

/**
 * The Supreme Ruler class; contains and provides access to most elements used
 * by the game.
 *
 * @author O
 */
public class Game {

    private BattleMap map;
    private ArrayList<Player> players;
    private Die die;
    private int currentPlayerIndex;
    private Unit activeUnit;
    private Tile targetTile;
    private int[][] moveCosts;
    private boolean gameOver;
    private JPanel commandList;
    private ActionController actions;
    private int round;
    private String gameLog;
    private GameCommand command;

    public ActionController getActions() {
        return actions;
    }

    public GameCommand command() {
        return command;
    }

    public void setActions(ActionController commands) {
        this.actions = commands;
    }

    public JPanel getCommandList() {
        return commandList;
    }

    public void setCommandList(JPanel commandList) {
        this.commandList = commandList;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    /**
     * Sets active unit and calculates movement costs if unit is not null.
     *
     * @param activeUnit
     */
    public void setActiveUnit(Unit activeUnit) {
        this.activeUnit = activeUnit;
        if (activeUnit != null) {
            moveCosts = GameUsage.speedRange(activeUnit, map);
        }
    }

    public int[][] getMoveCosts() {
        return moveCosts;
    }

    public Unit getActiveUnit() {
        return activeUnit;
    }

    /**
     * Basic constructor.
     */
    public Game() {
        gameOver = false;
        this.die = new DieRoller(10);
        players = new ArrayList();
        round = 0;
        gameLog = "";
    }

    public int getRound() {
        return round;
    }

    public void updateLog(String update) {
        gameLog = gameLog + "\n" + update;
    }

    /**
     * Initializes the game.
     *
     */
    public void run() {
        useGraphicalStartupUI();

    }

    public void runMore() {
        useGraphicalInterface();
        rollForInitiative();
        actions.updateUI();
    }

    public void setCommand(GameCommand command) {
        this.command = command;
    }

    /**
     * Starts new round, if game is not over. Chooses randomly which player goes
     * first.
     *
     */
    public void rollForInitiative() {
        if (gameOver) {
            return;
        }
        round++;
        Collections.shuffle(players);
        currentPlayerIndex = 0;
        for (Player player : players) {
            player.newRoundUnitReset();
        }
        if (getCurrentPlayer().getUnitsWithActions().isEmpty()) {
            nextPlayer();
            return;
        }
        this.setActiveUnit(getCurrentPlayer().activeUnit());
        updateLog(GameText.newRound(round));
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Cycles through players.
     */
    public void nextPlayer() {
        if (endRoundCheck()) {
            rollForInitiative();
            return;
        }
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
        if (getCurrentPlayer().getUnitsWithActions().isEmpty()) {
            nextPlayer();
            return;
        }
        updateLog(GameText.nextPlayer(getCurrentPlayer().getName()));
        setActiveUnit(getCurrentPlayer().activeUnit());
    }

    /**
     *
     */
    public void nextTurn() {
        nextPlayer();
        command.aiCheck();
        updateUI();
    }

    public void updateUI() {
        if (!(actions == null)) {
            getActions().updateUI();
        }
    }

    private void useGraphicalInterface() {
        UiView graphicalInterface = new UiView();
        graphicalInterface.spawn(this);
    }

    private void useGraphicalStartupUI() {
        StartupUI startup = new StartupUI();
        startup.spawn(this);
    }

    /**
     * Checks if all units for all players have ended their turns and if so,
     * begins a new round.
     *
     * @return true if all units have ended their turns.
     */
    public boolean endRoundCheck() {
        for (Player player : players) {
            for (Unit unit : player.getUnits()) {
                if (!unit.doneForTheRound()) {
                    return false;
                }
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
        for (Player player : players) {
            if (player.getUnits().isEmpty()) {
                gameOver = true;
                return true;
            }
        }
        return false;
    }

    public String getGameLog() {
        return gameLog;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Die getDie() {
        return die;
    }

    public BattleMap getMap() {
        return map;
    }

    /**
     * Placeholder until game startup customization interface is implemented.
     */
    public void startup() {
        players.add(new Player("Player 1", Color.BLUE));
        players.add(new Player("Player 2", Color.RED));
        players.get(0).testUnits();
        players.get(1).testUnits();
        players.get(0).setGame(this);
        players.get(1).setGame(this);
        players.get(0).testUnits2(2);
        players.get(1).testUnits2(2);
//        players.get(0).setAI(new ArtificialIntelligence(this, players.get(0), 1, 5, 10, 1));
        players.get(1).setAI(new ArtificialIntelligence(this, players.get(1), 1, 5, 10, 1));
        this.map = new BattleMap(12, 4);
        placeUnits(players.get(0).getUnits());
        placeUnits(players.get(1).getUnits());
    }

    /**
     * Placeholder until game startup customization interface is implemented.
     */

    public void startup2() {
        players.add(new Player("Player 1", Color.BLUE));
        players.add(new Player("Player 2", Color.RED));
        players.get(0).testUnits();
        players.get(1).testUnits();
        players.get(0).setGame(this);
        players.get(1).setGame(this);
        players.get(0).testUnits2(2);
        players.get(1).testUnits2(2);
        players.get(0).setAI(new ArtificialIntelligence(this, players.get(0), 1, 10, 10, 10));
        players.get(1).setAI(new ArtificialIntelligence(this, players.get(1), 1, 5, 12, 1));
        this.map = new BattleMap(16, 4);
        placeUnits(players.get(0).getUnits());
        placeUnits(players.get(1).getUnits());
    }

    public void setMap(BattleMap map) {
        this.map = map;
    }

    /**
     * Places units to battlemap.
     *
     * @param units units to be placed.
     * @return false if too many units for the map.
     */
    public boolean placeUnits(ArrayList<Unit> units) {
        if (units.size() > map.size()) {
            return false;
        }
        int border = 0;
        if (map.size() > 10) {
            border = map.size() / 10;
        }
        int y = (map.size() - units.size()) / 2;
        int x = map.size() - 1 - border;
        if (map.getTile(border, y).getUnit() == null) {
            x = 0 + border;
        }
        for (Unit unit : units) {
            map.getTile(x, y).setUnit(unit);
            y++;
        }
        return true;
    }

//    this text interface is obsolete and not up to date.
//
//    private void useTextInterface() {
//        boolean gameNotOver = true;
//        ArrayList<UserInterface> playerUIs = new ArrayList();
//        for (Player player : players) {
//            if (player.isAi()) {
//                playerUIs.add(new CpuPlayer());
//            } else {
//                playerUIs.add(new HumanPlayer(player, this));
//            }
//        }
//        while (gameNotOver) {
//            for (UserInterface current : playerUIs) {
//                if (!current.getPlayer().getUnitsWithActions().isEmpty()) {
//                    current.takeAITurn();
//                } else {
//                    System.out.println("game ends, " + current.getPlayer().getName() + " has no units left");
//                    gameNotOver = false;
//                    break;
//                }
//            }
//        }
//    }
}
