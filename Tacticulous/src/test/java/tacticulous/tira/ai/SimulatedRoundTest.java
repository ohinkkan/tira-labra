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
import tacticulous.tira.datastructure.TacList;
import static org.mockito.Mockito.*;
import tacticulous.game.domain.Tile;

/**
 *
 * @author O
 */
public class SimulatedRoundTest {

    private SimulatedRound round;
    private BattleMap map;
    private Game game;
    private GameCommand command;
    private ArtificialIntelligence ai;

    @Before
    public void setUp() {
        game = new Game();
        command = new GameCommand(game);
        game.setMap(new BattleMap(5, 4));
        game.setCommand(command);
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        for (Player player : game.getPlayers()) {
            player.quickStartUnits(1);
            player.setGame(game);
            game.placeUnits(player.getUnits());
        }
        command.rollForInitiative();
        command.nextTurn();
        map = game.getMap().copy();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
    }

    @Test
    public void targetsInRangeTest() {
        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);
        round = new SimulatedRound(friends, foes, ai, map);
        assertEquals(0, round.getTargetsInRange(friends.get(2), foes).size());
        assertEquals(3, round.getTargetsInRange(friends.get(1), foes).size());
    }

    @Test
    public void targetsInRangeTesta() {
        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);
        round = new SimulatedRound(friends, foes, ai, map);
        assertTrue(!round.checkIfSimulatedRoundOver());
        for (AIUnit unit : friends) {
            unit.attacks();
            unit.moves();
        }
        assertTrue(!round.checkIfSimulatedRoundOver());
        for (AIUnit unit : foes) {
            unit.attacks();
            unit.moves();
        }
        assertTrue(round.checkIfSimulatedRoundOver());
    }

    @Test
    public void returnsActionTest() {
        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);
        round = new SimulatedRound(friends, foes, ai, map);
        assertTrue(round.simulateTurn() != null);
    }

    @Test
    public void allAttacksAndDelayTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);
        when(ai.getValueLogic().delayValue((AIUnit) any(), (SimulatedRound) any())).thenReturn(400);

        round = new SimulatedRound(friends, foes, ai, map);

        Action optimal = new Action(Integer.MIN_VALUE / 2);

        Action result = round.allAttacksAndDelay(friends.get(1), optimal);
        Action result2 = round.allAttacksAndDelay(friends.get(0), optimal);
        assertEquals(600, result.getValue());
        assertEquals(ActionType.ATTACKANDDELAY, result.getType());
        assertEquals(Integer.MIN_VALUE / 2, result2.getValue());
    }

    @Test
    public void allAttacksAndEndTurnTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);

        round = new SimulatedRound(friends, foes, ai, map);

        Action optimal = new Action(Integer.MIN_VALUE / 2);

        Action result = round.allAttacksAndEndTurn(friends.get(1), optimal);

        assertEquals(200, result.getValue());
        assertEquals(ActionType.ATTACKANDENDTURN, result.getType());
    }

    @Test
    public void allAttacksAndMoves() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);
        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);
        Action optimal = new Action(Integer.MIN_VALUE / 2);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.allAttacksAndMoves(friends.get(1), optimal);

        assertEquals(ActionType.ATTACKANDMOVE, result.getType());
        assertEquals(500, result.getValue());

        Action result2 = round.allAttacksAndMoves(friends.get(0), optimal);

        assertEquals(Integer.MIN_VALUE / 2, result2.getValue());

    }

    @Test
    public void allMovesAndDelayTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);
        when(ai.getValueLogic().delayValue((AIUnit) any(), (SimulatedRound) any())).thenReturn(400);

        round = new SimulatedRound(friends, foes, ai, map);

        Action optimal = new Action(Integer.MIN_VALUE / 2);

        Action result = round.allMovesAndDelay(friends.get(1), optimal);

        assertEquals(700, result.getValue());
        assertEquals(ActionType.MOVEANDDELAY, result.getType());
    }

    @Test
    public void allMovesAndEndTurnTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);

        round = new SimulatedRound(friends, foes, ai, map);

        Action optimal = new Action(Integer.MIN_VALUE / 2);

        Action result = round.allMovesAndEndTurn(friends.get(1), optimal);

        assertEquals(300, result.getValue());
        assertEquals(ActionType.MOVEANDENDTURN, result.getType());
    }

    @Test
    public void allMovesAndAttacksTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);
        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);
        Action optimal = new Action(Integer.MIN_VALUE / 2);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.allMovesAndAttacks(friends.get(0), optimal);

        assertEquals(ActionType.MOVEANDATTACK, result.getType());
        assertEquals(300, result.getValue());

    }

    @Test
    public void onlyDelayTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().delayValue((AIUnit) any(), (SimulatedRound) any())).thenReturn(400);

        round = new SimulatedRound(friends, foes, ai, map);
        Action optimal = new Action(Integer.MIN_VALUE / 2);

        Action result = round.onlyDelay(friends.get(0), optimal);

        assertEquals(ActionType.DELAY, result.getType());
        assertEquals(400, result.getValue());
        assertEquals(friends.get(0).getX(), result.getUnitX());
        assertEquals(friends.get(0).getY(), result.getUnitY());
    }

    @Test
    public void endTurnTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        round = new SimulatedRound(friends, foes, ai, map);
        Action optimal = new Action(Integer.MIN_VALUE/2);

        Action result = round.endTurn(friends.get(0), optimal);

        assertEquals(ActionType.ENDTURN, result.getType());
        assertEquals(Integer.MIN_VALUE / 2, result.getValue());
        assertEquals(friends.get(0).getX(), result.getUnitX());
        assertEquals(friends.get(0).getY(), result.getUnitY());
    }

    @Test
    public void singleMoveAndAllAttacksTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);
        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.singleMoveAndAllAttacks(friends.get(0), map.getTile(1, 1));

        assertEquals(300, result.getValue());

    }

    @Test
    public void singleMoveAndDelayTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);
        when(ai.getValueLogic().delayValue((AIUnit) any(), (SimulatedRound) any())).thenReturn(400);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.singleMoveAndDelay(friends.get(0), map.getTile(1, 1));

        assertEquals(700, result.getValue());
    }

    @Test
    public void singleMoveAndEndTurnTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.singleMoveAndEndTurn(friends.get(0), map.getTile(1, 1));

        assertEquals(300, result.getValue());
    }

    @Test
    public void singleMoveTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().movementValue((AIUnit) any(), (Tile) any(), (SimulatedRound) any())).thenReturn(300);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.singleMove(friends.get(0), map.getTile(1, 2));

        assertEquals(300, result.getValue());
        assertEquals(1, result.getMovementX());
        assertEquals(2, result.getMovementY());
    }

    @Test
    public void singleAttackTest() {
        ValueLogic mockValue = mock(ValueLogic.class);
        ai.setValueLogic(mockValue);

        TacList<AIUnit> friends = ai.enterTheMatrix(game.getPlayers().get(1), map);
        TacList<AIUnit> foes = ai.enterTheMatrix(game.getPlayers().get(0), map);

        when(ai.getValueLogic().attackValue((AIUnit) any(), (AIUnit) any(), (SimulatedRound) any())).thenReturn(200);

        round = new SimulatedRound(friends, foes, ai, map);

        Action result = round.singleAttack(friends.get(1), foes.get(1));

        assertEquals(200, result.getValue());
        assertEquals(foes.get(1).getX(), result.getAttackX());
        assertEquals(foes.get(1).getY(), result.getAttackY());
    }

}
