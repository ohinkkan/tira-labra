package tacticulous.tira.ai;

import org.junit.Test;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;

/**
 *
 * @author O
 */
public class ArtificialIntelligenceTest {


    @Test
    public void atLeastDoesNotCrashOrRebel() {
        Game game = new Game();
        game.setCommand(new GameCommand(game));
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).quickStartUnits(1);
        game.getPlayers().get(1).quickStartUnits(1);
        game.getPlayers().get(0).setAI(new ArtificialIntelligence(game, game.getPlayers().get(0),
                1, 1, 1, 1));
        game.getPlayers().get(1).setAI(new ArtificialIntelligence(game, game.getPlayers().get(1),
                1, 1, 1, 1));
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.command().rollForInitiative();
        for (int i = 0; i < 50; i++) {
            if (game.command().checkIfGameOver()) {
                break;
            }
            game.command().getCurrentPlayer().getAi().takeTurn();
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
}
