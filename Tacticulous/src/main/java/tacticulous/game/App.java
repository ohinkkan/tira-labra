package tacticulous.game;

import tacticulous.game.domain.Game;
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
     * @param args Arghs.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
//        Performance.comparisons();
//            Performance.listComparisons();
    }
}
