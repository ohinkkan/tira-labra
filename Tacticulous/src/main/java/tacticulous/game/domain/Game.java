/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import java.util.ArrayList;
import tacticulous.game.utility.*;
import tacticulous.game.ui.HumanPlayer;
import tacticulous.game.ui.UserInterface;

/**
 *
 * @author O
 */
public class Game {

    private BattleMap map;
    private ArrayList<UserInterface> playerUIs;
    private Die die;

    public Game() {
        this.die = new DieRoller(10);
        playerUIs = new ArrayList();
        playerUIs.add(new HumanPlayer(new Player("Player 1"), this));
        playerUIs.add(new HumanPlayer(new Player("Player 2"), this));
        playerUIs.get(0).getPlayer().testUnits();
        playerUIs.get(1).getPlayer().testUnits();
        this.map = new BattleMap(10, die, 3);
        placeUnits(playerUIs.get(0).getPlayer().getUnits());
        placeUnits(playerUIs.get(1).getPlayer().getUnits());
    }

    public ArrayList<UserInterface> getPlayers() {
        return playerUIs;
    }

    public Die getDie() {
        return die;
    }

    public BattleMap getMap() {
        return map;
    }

    public void run() {
        while (true) {
            map.drawMap();
            for (UserInterface current : playerUIs) {
                current.takeTurn();
            }
            
        }
    }

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
}
