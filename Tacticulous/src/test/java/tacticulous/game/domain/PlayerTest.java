package tacticulous.game.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author O
 */
public class PlayerTest {

    Player player;

    @Test
    public void newPlayerHasCorrectUnitList() {
        player = new Player("A");
        player.testUnits();
        assertTrue(player.getUnitsWithActions().isEmpty());
        assertTrue(!player.getUnits().isEmpty());
    }

    @Test
    public void newRoundWorks() {
        player = new Player("A");
        player.testUnits();
        assertTrue(player.getUnitsWithActions().isEmpty());
        player.newRound();
        assertTrue(!player.getUnitsWithActions().isEmpty());
    }

    @Test
    public void activeUnitWorks() {
        player = new Player("A");
        player.testUnits();
        assertNull(player.activeUnit());
        player.newRound();
        assertEquals(player.getUnits().get(0), player.activeUnit());
    }

    @Test
    public void cycleUnitsForwardWorks() {
        Game game = new Game();
        game.startup();
        game.rollForInitiative();
        player = game.getCurrentPlayer();
        assertEquals(player.getUnits().get(0), player.activeUnit());
        player.nextUnit(game);
        assertEquals(player.getUnits().get(1), player.activeUnit());
        player.nextUnit(game);
        player.nextUnit(game);
        assertEquals(player.getUnits().get(0), player.activeUnit());
    }

    @Test
    public void cycleUnitsBackwardsWorks() {
        Game game = new Game();
        game.startup();
        game.rollForInitiative();
        player = game.getCurrentPlayer();
        player.prevUnit(game);
        assertEquals(player.getUnits().get(2), player.activeUnit());
        player.prevUnit(game);
        player.prevUnit(game);
        assertEquals(player.getUnits().get(0), player.activeUnit());
    }
}
