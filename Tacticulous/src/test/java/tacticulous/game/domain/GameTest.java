package tacticulous.game.domain;

//import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import tacticulous.game.commands.GameCommand;
import tacticulous.tira.datastructure.TacList;

/**
 *
 * @author O
 */
public class GameTest {

    Game game;
    GameCommand command;

    @Before
    public void setUp() {
        game = new Game();
        command = new GameCommand(game);
        game.setMap(new BattleMap(12, 4));
        game.setCommand(command);
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        for (Player player : game.getPlayers()) {
            player.quickStartUnits(2);
            player.setGame(game);
            game.placeUnits(player.getUnits());
        }

    }
    @Test
    public void newGameNoNullAssets() {
        assertNotNull(game.getDie());
        assertNotNull(game.getMap());
        assertNotNull(game.getPlayers());
    }

    @Test
    public void unitPlacementWorks() {
        game = new Game();
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).quickStartUnits(1);
        game.getPlayers().get(1).quickStartUnits(1);
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        assertNotNull(game.getMap().getTile(0, game.getMap().size() / 2 - 1).getUnit());
        assertNotNull(game.getMap().getTile(game.getMap().size() - 1, game.getMap().size() / 2 - 1).getUnit());
    }

    @Test
    public void unitPlacementReturnsFalseIfTooManyUnits() {
        TacList<Unit> units = game.getPlayers().get(0).getUnits();
        for (int i = 0; i < game.getMap().size(); i++) {
            units.add(new Unit(1, 1, 1, 1, "A"));
        }
        assertEquals(false, game.placeUnits(units));
    }

}
