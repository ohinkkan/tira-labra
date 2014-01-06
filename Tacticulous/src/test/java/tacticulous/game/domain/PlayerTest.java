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
        player = new Player("A", null);
        player.testUnits();
        assertTrue(player.getUnitsWithActions().isEmpty());
        assertTrue(!player.getUnits().isEmpty());
    }

    @Test
    public void newRoundWorks() {
        player = new Player("A", null);
        player.testUnits();
        assertTrue(player.getUnitsWithActions().isEmpty());
        player.newRoundUnitReset();
        assertTrue(!player.getUnitsWithActions().isEmpty());
    }

    @Test
    public void activeUnitWorks() {
        player = new Player("A", null);
        player.testUnits();
        assertNull(player.activeUnit());
        player.newRoundUnitReset();
        assertEquals(player.getUnits().get(0), player.activeUnit());
    }

    @Test
    public void cycleUnitsForwardWorks() {
        Game game = new Game();
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).testUnits();
        game.getPlayers().get(1).testUnits();
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
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
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).testUnits();
        game.getPlayers().get(1).testUnits();
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.rollForInitiative();
        player = game.getCurrentPlayer();
        player.prevUnit(game);
        assertEquals(player.getUnits().get(2), player.activeUnit());
        player.prevUnit(game);
        player.prevUnit(game);
        assertEquals(player.getUnits().get(0), player.activeUnit());
    }
}
