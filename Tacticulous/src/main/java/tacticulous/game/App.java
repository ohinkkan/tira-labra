package tacticulous.game;

import tacticulous.game.domain.Game;

/**
 * Starts the game.
 *
 * @author O
 */
public class App 
{

    /**
     * Starts the game.
     * 
     * @param args
     */
    public static void main( String[] args )
    {
        Game game = new Game();
        game.run();
    }
}
