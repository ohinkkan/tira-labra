package tacticulous.game.domain;

import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.commands.GameCommand;

/**
 *
 * @author O
 */
public class PlayerTest {

    Player player;
    Game game;

    public void initGame() {
        game = new Game();
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        game.getPlayers().get(0).quickStartUnits(1);
        game.getPlayers().get(1).quickStartUnits(1);
        game.setMap(new BattleMap(10, 1));
        game.placeUnits(game.getPlayers().get(0).getUnits());
        game.placeUnits(game.getPlayers().get(1).getUnits());
        game.setCommand(new GameCommand(game));
    }

    @Test
    public void doneForTheRoundAndNewRoundWork() {
        player = new Player("A", null);
        player.quickStartUnits(1);
        for (Unit unit : player.getUnits()) {
            unit.attacks();
            unit.moves();
        }
        assertTrue(player.isDoneForTheRound());
        player.newRoundUnitReset();
        assertTrue(!player.isDoneForTheRound());
    }

    @Test
    public void putCorpseWorks() {
        initGame();
        game.command().rollForInitiative();
        int x = game.command().getActiveUnit().getX();
        int y = game.command().getActiveUnit().getY();
        game.command().getCurrentPlayer().setGame(game);
        game.command().getCurrentPlayer().kill(game.command().getActiveUnit());
        assertEquals(1, game.getMap().getTile(x, y).getCorpses().size());
    }

    @Test
    public void getFirstUnitWithActionsWorks() {
        initGame();
        game.command().rollForInitiative();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        Unit unit2 = game.getPlayers().get(0).getUnits().get(1);
        Unit unit3 = game.getPlayers().get(0).getUnits().get(2);
        assertEquals(unit, game.getPlayers().get(0).getFirstUnitWithActions());
        unit.moves();
        unit.attacks();
        unit2.attacks();
        assertEquals(unit2, game.getPlayers().get(0).getFirstUnitWithActions());
        unit3.moves();
        unit2.moves();
        assertEquals(unit3, game.getPlayers().get(0).getFirstUnitWithActions());
        unit3.attacks();
        assertEquals(null, game.getPlayers().get(0).getFirstUnitWithActions());


    }

    @Test
    public void detectDeadLeaderWorks() {
        initGame();
        Unit leader = new Unit(null);
        game.getPlayers().get(0).getUnits().add(leader);
        assertTrue(!game.getPlayers().get(0).leaderIsDead());
        game.getPlayers().get(0).kill(leader);
        assertTrue(game.getPlayers().get(0).leaderIsDead());
    }
}
