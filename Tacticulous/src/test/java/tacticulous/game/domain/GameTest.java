/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;

/**
 *
 * @author O
 */
public class GameTest {

    Game game;

    @Test
    public void newGameNoNullAssets() {
        game = new Game();
        game.startup();
        assertNotNull(game.getDie());
        assertNotNull(game.getMap());
        assertNotNull(game.getPlayers());
    }

    @Test
    public void unitPlacementWorks() {
        game = new Game();
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).testUnits();
        game.getPlayers().get(1).testUnits();
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        assertNotNull(game.getMap().getTile(0, game.getMap().size() / 2 - 1).getUnit());
        assertNotNull(game.getMap().getTile(game.getMap().size() - 1, game.getMap().size() / 2 - 1).getUnit());
    }

    @Test
    public void unitPlacementReturnsFalseIfTooManyUnits() {
        game = new Game();
        game.startup();
        ArrayList<Unit> units = game.getPlayers().get(0).getUnits();
        for (int i = 0; i < game.getMap().size(); i++) {
            units.add(new Unit(1, 1, 1, 1, "A"));
        }
        assertEquals(false, game.placeUnits(units));
    }

    @Test
    public void rollForInitiativeWorks() {
        game = new Game();
        game.startup();
        game.rollForInitiative();
        assertNotNull(game.getCurrentPlayer());
        assertNotNull(game.getActiveUnit());
    }

    @Test
    public void nextPlayerWorks() {
        game = new Game();
        game.startup();
        game.rollForInitiative();
        Player player1 = game.getCurrentPlayer();
        game.nextPlayer();
        assertNotSame(player1, game.getCurrentPlayer());
        game.nextPlayer();
        assertEquals(player1, game.getCurrentPlayer());
    }

    @Test
    public void newRoundWorks() {
        game = new Game();
        game.startup();
        game.rollForInitiative();
        game.getCurrentPlayer().getUnitsWithActions().clear();
        game.nextPlayer();
        game.getCurrentPlayer().getUnitsWithActions().clear();
        assertEquals(1, game.getRound());
        game.nextPlayer();
        assertEquals(2, game.getRound());
    }

    @Test
    public void endRoundCheckWorks() {
        game = new Game();
        game.startup();
        assertTrue(game.endRoundCheck());
    }

    @Test
    public void gameOverWorks() {
        game = new Game();
        game.startup();
        game.rollForInitiative();
        assertTrue(!game.checkIfGameOver());
        game.getCurrentPlayer().getUnits().clear();
        assertTrue(game.checkIfGameOver());
    }

}
