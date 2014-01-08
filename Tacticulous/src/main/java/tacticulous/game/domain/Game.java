package tacticulous.game.domain;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.graphicalui.ActionController;
import tacticulous.game.graphicalui.StartupUI;
import tacticulous.game.graphicalui.GameUI;
import tacticulous.game.utility.*;
import tacticulous.tira.ai.ArtificialIntelligence;

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
    private ActionController actions;
    private int round;
    private String gameLog;
    private GameCommand command;
    private boolean killLeader = false;

    /**
     * Basic constructor.
     */
    public Game() {
        this.die = new DieRoller(10);
        players = new ArrayList();
        round = 0;
        gameLog = "";
    }

    /**
     * Starts the game startup interface.
     */
    public void run() {
        StartupUI startup = new StartupUI();
        startup.spawn(this);
    }

    /**
     * Statrs the main game interface
     */
    public void runUI() {
        GameUI graphicalInterface = new GameUI();
        graphicalInterface.spawn(this);
        if (command.getCurrentPlayer().isAi()) {
            actions.aiCommands();
        } else {
            actions.playerCommands();
        }
        command.rollForInitiative();
        actions.updateUI();
    }

    /**
     * updates all main gaim ui elements if ui is initialized, otherwise does
     * nothing.
     *
     * @see tacticulous.game.graphicalui.ActionController#updateUI()
     */
    public void updateUI() {
        if (!(actions == null)) {
            actions.updateUI();
        }
    }

    public boolean isKillLeader() {
        return killLeader;
    }

    public void setKillLeader(boolean killLeader) {
        this.killLeader = killLeader;
    }

    /**
     * Adds provided string to the game log.
     *
     * @param update added at the bottom of the game log.
     */
    public void updateLog(String update) {
        gameLog = gameLog + "\n" + update;
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

    public ActionController getActions() {
        return actions;
    }

    /**
     * Used to access game command logic.
     *
     * @return GameCommand
     *
     * @see tacticulous.game.commands.GameCommand
     */
    public GameCommand command() {
        return command;
    }

    public void setActions(ActionController commands) {
        this.actions = commands;
    }

    public void setCommand(GameCommand command) {
        this.command = command;
    }

    public String getGameLog() {
        return gameLog;
    }

    public int getRound() {
        return round;
    }

    /**
     * Increases round counter
     */
    public void increaseRoundCounter() {
        round++;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Die getDie() {
        return die;
    }

    public void setMap(BattleMap map) {
        this.map = map;
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

    public void startup3() {
        players.add(new Player("Player 1", Color.BLUE));
        players.add(new Player("Player 2", Color.RED));
        this.map = new BattleMap(10, 4);
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

}
