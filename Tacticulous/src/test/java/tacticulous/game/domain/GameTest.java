package tacticulous.game.domain;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.commands.GameCommand;

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
        game.startup2();
        assertNotNull(game.getDie());
        assertNotNull(game.getMap());
        assertNotNull(game.getPlayers());
    }

    @Test
    public void newGameNoNullAssets2() {
        game = new Game();
        game.startup2();
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

}
