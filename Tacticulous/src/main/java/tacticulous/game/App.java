package tacticulous.game;

import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.tira.testing.Performance;

/**
 * Starts the game.
 *
 * @author O
 */
public class App {

    /**
     * Starts the game.
     *
     * @param args
     */
    public static void main(String[] args) {
        Game game = new Game();
//        game.startup();
//        Player player = game.getPlayers().get(1);
//        for (Player player2 : game.getPlayers()) {
//            player2.newRound();
//        }
//        player.getAi().takeTurn();
//
        game.run();
//        Performance.comparisons();
    }
}
