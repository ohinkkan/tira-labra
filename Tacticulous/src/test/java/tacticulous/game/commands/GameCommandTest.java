package tacticulous.game.commands;

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
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;
import tacticulous.game.graphicalui.GameText;
import tacticulous.tira.ai.ArtificialIntelligence;

/**
 *
 * @author O
 */
public class GameCommandTest {

    private Game game;
    private GameCommand command;

    @Before
    public void setUp() {
        game = new Game();
        command = new GameCommand(game);
        game.setMap(new BattleMap(12, 4));
        game.setCommand(command);
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        for (Player player : game.getPlayers()) {
            player.testUnits();
            player.setGame(game);
            player.testUnits();
            game.placeUnits(player.getUnits());
        }

    }

    private void unitsEndTurn(Player player) {
        for (Unit unit : player.getUnits()) {
            unit.attacks();
            unit.moves();
        }
    }

    @Test
    public void rollForInitiativeWorks() {
        for (int i = 1; i <= 3; i++) {
            command.rollForInitiative();
            assertNotNull(command.getCurrentPlayer());
            assertNotNull(command.getActiveUnit());
            assertEquals(i, game.getRound());
        }
    }

    @Test
    public void nextPlayerWorks() {
        command.rollForInitiative();
        Player player1 = command.getCurrentPlayer();
        command.nextTurn();
        assertNotSame(player1, command.getCurrentPlayer());
        command.nextTurn();
        assertEquals(player1, command.getCurrentPlayer());
    }

    @Test
    public void nextPlayerDetectsGameOver() {
        command.rollForInitiative();
        game.getPlayers().get(1).getUnits().clear();
        command.nextPlayerTurn();
        assertTrue(game.command().isGameOver());
    }

    @Test
    public void nextPlayerDetectsRoundOver() {
        command.rollForInitiative();
        for (Player player : game.getPlayers()) {
            unitsEndTurn(player);
        }
        command.nextPlayerTurn();
        assertEquals(2, game.getRound());
    }

    @Test
    public void nextPlayerSkipsPlayerWithNoUnitsWithActionsLeft() {
        command.rollForInitiative();
        Player player = command.getCurrentPlayer();
        for (Unit unit : game.getPlayers().get(1).getUnits()) {
            unit.attacks();
            unit.moves();
        }
        command.nextPlayerTurn();
        assertEquals(player, command.getCurrentPlayer());
    }

    @Test
    public void newRoundWorks() {
        command.rollForInitiative();
        assertEquals(1, game.getRound());
        command.rollForInitiative();
        assertEquals(2, game.getRound());
    }

    @Test
    public void endRoundCheckWorks() {
        command.rollForInitiative();
        assertTrue(!command.checkIfRoundOver());
        for (Player player : game.getPlayers()) {
            unitsEndTurn(player);
        }
        assertTrue(command.checkIfRoundOver());
    }

    @Test
    public void gameOverWorks() {
        command.rollForInitiative();
        assertTrue(!command.checkIfGameOver());
        command.getCurrentPlayer().getUnits().clear();
        assertTrue(command.checkIfGameOver());
    }

//    @Test
//    public void aiAutoTurnCheckWorks() {
//        command.rollForInitiative();
//        Player player = command.getCurrentPlayer();
//        command.aiAutoTurnCheck();
//        assertEquals(player, command.getCurrentPlayer());
//        player.setAI(new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 1, 1, 1));
//        command.aiAutoTurnCheck();
//        assertEquals(player, command.getCurrentPlayer());
//        player.getAi().autoOn();
//        command.aiAutoTurnCheck();
//        assertNotSame(player, command.getCurrentPlayer());
//    }

    @Test
    public void nextTurnWorks() {
        command.rollForInitiative();
        Player player = command.getCurrentPlayer();
        command.nextTurn();
        assertNotSame(player, command.getCurrentPlayer());

    }

//    @Test
//    public void autoTurnWorks() {
//        command.rollForInitiative();
//        Player player = game.getPlayers().get(1);
//        player.setAI(new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 1, 1, 1));
//        command.nextTurn();
//        assertEquals(player, command.getCurrentPlayer());
//    }

    @Test
    public void attackCommandWorks() {
        command.rollForInitiative();
        Tile tile = new Tile(1, 1, 1);
        Unit unit = new Unit(1, 1, 100, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 2, "B");
        command.setActiveUnit(unit);
        command.setTargetTile(tile);
        tile.setUnit(unit2);
        command.attack();
        assertTrue(command.unitSelectionDisabled());
        assertTrue(!unit.hasNotAttacked());
        assertEquals(unit, command.getActiveUnit());
    }

    @Test
    public void attackCommandWorks2() {
        command.rollForInitiative();
        Tile tile = new Tile(1, 1, 1);
        Unit unit = new Unit(1, 1, 100, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 2, "B");
        command.setActiveUnit(unit);
        command.setTargetTile(tile);
        tile.setUnit(unit2);
        unit.moves();
        command.attack();
        assertTrue(!command.unitSelectionDisabled());
        assertTrue(!unit.hasNotAttacked());
    }

    @Test
    public void endTurnCommandWorks() {
        command.rollForInitiative();
        Player player = command.getCurrentPlayer();
        Unit unit = command.getActiveUnit();
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotMoved());
        command.endTurn();
        assertTrue(unit.doneForTheRound());
        assertTrue(player != command.getCurrentPlayer());
    }

    @Test
    public void delayTurnCommandWorks() {
        command.rollForInitiative();
        Player player = command.getCurrentPlayer();
        Unit unit = command.getActiveUnit();
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotMoved());
        command.delay();
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotMoved());
        assertTrue(player != command.getCurrentPlayer());
    }

    @Test
    public void moveCommandWorks() {
        command.rollForInitiative();
        Unit unit = new Unit(1, 1, 100, 1, "A");
        game.getMap().getTile(5, 5).setUnit(unit);
        command.setActiveUnit(unit);
        command.setTargetTile(game.getMap().getTile(4, 4));
        command.move();
        assertTrue(command.getTargetTile() == null);
        assertTrue(game.getMap().getTile(4, 4).getUnit() == unit);
        assertTrue(!unit.hasNotMoved());
        assertTrue(!unit.doneForTheRound());
    }

//    @Test
//    public void autoTurnToggleWorks() {
//       command.rollForInitiative();
//       command.getCurrentPlayer().setAI(new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 1, 1, 1));
//       command.autoTurn();
//       assertTrue(game.getPlayers().get(0).getAi().autoIsOn());
//       assertEquals(command.getCurrentPlayer(),game.getPlayers().get(1));
//       command.nextTurn();
//       assertEquals(command.getCurrentPlayer(),game.getPlayers().get(1));
//    }

    @Test
    public void attackCommandWorks3() {
        command.rollForInitiative();
        Tile tile = new Tile(1, 1, 1);
        Unit unit = new Unit(1, 1, 100, 1, "A");
        Unit unit2 = new Unit(1, 1, 1, 2, "B");
        command.setActiveUnit(unit);
        command.setTargetTile(tile);
        tile.setUnit(unit2);
        Unit unit3 = new Unit(1, 1, 1, 1, 1, "B", command.getCurrentPlayer());
        command.getCurrentPlayer().getUnits().add(unit3);
        Unit unit4 = new Unit(1, 200, 1, 2, "B");

        assertEquals(GameText.attackHits("A", "B"), command.unitAttacks());

        tile.setUnit(unit3);
        assertEquals(GameText.attackKills("A", "B"), command.unitAttacks());

        tile.setUnit(unit4);
        assertEquals(GameText.attackMisses("A", "B"), command.unitAttacks());
    }
}
