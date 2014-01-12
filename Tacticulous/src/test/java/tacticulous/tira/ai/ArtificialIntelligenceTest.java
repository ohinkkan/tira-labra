package tacticulous.tira.ai;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;

/**
 *
 * @author O
 */
public class ArtificialIntelligenceTest {

    @Test
    public void atLeastDoesNotCrashOrRebel() {

        for (int j = 0; j < 25; j++) {
            Game game = new Game();
            game.setCommand(new GameCommand(game));
            Die die = new DieRoller(100);
            game.setMap(new BattleMap((die.roll() / 5) + 3, (die.roll() / 20) + 1));
            game.getPlayers().add(new Player("Player 1", null));
            game.getPlayers().add(new Player("Player 2", null));
            game.getPlayers().get(0).quickStartUnits(Math.min(game.getMap().size() / 3, 3));
            game.getPlayers().get(1).quickStartUnits(Math.min(game.getMap().size() / 3, 3));
            game.getPlayers().get(0).setAI(new ArtificialIntelligence(game, game.getPlayers().get(0),
                    1, die.roll(), die.roll(), die.roll()));
            game.getPlayers().get(1).setAI(new ArtificialIntelligence(game, game.getPlayers().get(1),
                    1, die.roll(), die.roll(), die.roll()));
            game.placeUnits(game.getPlayers().get(0).getUnits());
            game.placeUnits(game.getPlayers().get(1).getUnits());
            game.command().rollForInitiative();
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 40; i++) {
                if (game.command().checkIfGameOver()) {
                    break;
                }
                game.command().getCurrentPlayer().getAi().takeTurn();
                long stopTime = System.currentTimeMillis();
                long length = stopTime - startTime;
                if (length > 10000) {
                    throw new AssertionError("Random game test took too long, "+length+"ms.", null);
                }
            }
        }
    }

    @Test
    public void multiTurnAINoCrash() {
        Game game = new Game();
        game.setCommand(new GameCommand(game));
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).quickStartUnits(1);
        game.getPlayers().get(1).quickStartUnits(1);
        game.getPlayers().get(0).setAI(new ArtificialIntelligence(game, game.getPlayers().get(0),
                2, 1, 1, 1));
        game.getPlayers().get(1).setAI(new ArtificialIntelligence(game, game.getPlayers().get(1),
                2, 1, 1, 1));
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.command().rollForInitiative();
        for (int i = 0; i < 1; i++) {
            if (game.command().checkIfGameOver()) {
                break;
            }
            game.command().getCurrentPlayer().getAi().takeTurn();
        }
    }

    @Test
    public void pickUnitsWorks() {
        ArtificialIntelligence ai = new ArtificialIntelligence(null, null, 1, 1, 1, 1);
        int[] test = ai.pickUnits(5, 100);
        int sum = 0;
        for (int i = 0; i < test.length; i++) {
            assertTrue(test[i] > -1);
            assertTrue(test[i] < 101);
            sum = sum + test[i];
        }
        assertEquals(100, sum);
    }
}
