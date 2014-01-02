package tacticulous.game.domain;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;
import tacticulous.game.graphicalui.ActionController;
import tacticulous.game.graphicalui.GameText;
import tacticulous.game.graphicalui.UiView;
import tacticulous.game.utility.*;
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

    public ActionController getActions() {
        return actions;
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
    }

    public int getRound() {
        return round;
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
     * Initializes the game.
     *
     */
    public void run() {

        startup();

        useGraphicalInterface();

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
            player.newRound();
        }
        this.setActiveUnit(getCurrentPlayer().activeUnit());
        if (actions != null) {
            actions.updateLog(GameText.newRound(round));
        }
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
            return;
        }
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
        if (getCurrentPlayer().getUnitsWithActions().isEmpty()) {
            nextPlayer();
        }
        setActiveUnit(getCurrentPlayer().activeUnit());
    }

    private void useGraphicalInterface() {
        UiView graphicalInterface = new UiView();
        graphicalInterface.spawn(this);
    }

    /**
     * Checks if all units for all players have ended their turns and if so,
     * begins a new round.
     *
     * @return true if all units have ended their turns.
     */
    public boolean endRoundCheck() {
        for (Player player : players) {
            if (!player.getUnitsWithActions().isEmpty()) {
                return false;
            }
        }
        rollForInitiative();
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

    /**
     * Placeholder until game startup customization interface is implemented.
     */
    public void startup() {
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
        players.get(0).testUnits();
        players.get(1).testUnits();
//        players.get(0).testUnits2(30);
//        players.get(1).testUnits2(30);
//        players.get(1).setAI(null);
        this.map = new BattleMap(20, 4);
        placeUnits(players.get(0).getUnits());
        placeUnits(players.get(1).getUnits());
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
        int y = (map.size() - units.size()) / 2;
        int x = map.size() - 1;
        if (map.getTile(0, y).getUnit() == null) {
            x = 0;
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
//                    current.takeTurn();
//                } else {
//                    System.out.println("game ends, " + current.getPlayer().getName() + " has no units left");
//                    gameNotOver = false;
//                    break;
//                }
//            }
//        }
//    }
}
