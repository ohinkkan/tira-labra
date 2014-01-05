/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.ai;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;

/**
 *
 * @author O
 */
public class ArtificialIntelligenceTest {

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void atLeastDoesNotCrashOrRebel() {
        Game game = new Game();
        game.setCommand(new GameCommand(game));
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).testUnits();
        game.getPlayers().get(1).testUnits();
        game.getPlayers().get(0).setAI(new ArtificialIntelligence(game, game.getPlayers().get(0),
                1, 1, 1, 1));
        game.getPlayers().get(1).setAI(new ArtificialIntelligence(game, game.getPlayers().get(1),
                1, 1, 1, 1));
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.rollForInitiative();
        for (int i = 0; i < 100; i++) {
            if (game.checkIfGameOver()) {
                break;
            }
            game.getCurrentPlayer().getAi().takeTurn();
        }
    }

    @Test
    public void multiTurnAINoCrash() {
        Game game = new Game();
        game.setCommand(new GameCommand(game));
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).testUnits();
        game.getPlayers().get(1).testUnits();
        game.getPlayers().get(0).setAI(new ArtificialIntelligence(game, game.getPlayers().get(0),
                2, 1, 1, 1));
        game.getPlayers().get(1).setAI(new ArtificialIntelligence(game, game.getPlayers().get(1),
                2, 1, 1, 1));
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.rollForInitiative();
        for (int i = 0; i < 2; i++) {
            if (game.checkIfGameOver()) {
                break;
            }
            game.getCurrentPlayer().getAi().takeTurn();
        }
    }
}
