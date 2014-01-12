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
import tacticulous.game.domain.Unit;
import tacticulous.tira.datastructure.TacList;

/**
 *
 * @author O
 */
public class ActionTest {

    private Game game;
    private GameCommand command;
    private ArtificialIntelligence ai;

    @Before
    public void setUp() {
        game = new Game();
        command = new GameCommand(game);
        game.setMap(new BattleMap(3, 4));
        game.setCommand(command);
        game.getPlayers().add(new Player("Player 1", null));
        game.getPlayers().add(new Player("Player 2", null));
        for (Player player : game.getPlayers()) {
            player.quickStartUnits(1);
            player.setGame(game);
            game.placeUnits(player.getUnits());
        }
    }

    @Test
    public void delayWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.DELAY);
        assertTrue(unit.hasNotDelayed());
        assertTrue(friend.get(0).hasNotDelayed());
        action.takeAction(game);
        assertTrue(!unit.hasNotDelayed());
    }

    @Test
    public void moveAndDelayWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.MOVEANDDELAY);
        action.setMovementCoordinates(1, 1);
        assertTrue(friend.get(0).hasNotMoved());
        assertTrue(unit.hasNotMoved());
        action.takeAction(game);
        assertTrue(!unit.hasNotMoved());
        assertEquals(unit, game.getMap().getTile(1, 1).getUnit());
        assertTrue(!unit.hasNotDelayed());
    }

    @Test
    public void moveAndEndTurnWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        Player compare = game.command().getCurrentPlayer();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        unit.attacks();
        action.setType(ActionType.MOVEANDENDTURN);
        action.setMovementCoordinates(1, 1);
        action.takeAction(game);
        assertEquals(unit, game.getMap().getTile(1, 1).getUnit());
        assertTrue(game.command().getCurrentPlayer() != compare);
    }

    @Test
    public void moveAndAttackWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.MOVEANDATTACK);
        action.setAttackCoordinates(2, 2);
        action.setMovementCoordinates(1, 1);
        assertTrue(unit.hasNotAttacked());
        action.takeAction(game);
        assertEquals(unit, game.getMap().getTile(1, 1).getUnit());
        assertTrue(!unit.hasNotAttacked());
        assertTrue(!unit.hasNotMoved());
    }

    @Test
    public void attackAndMoveWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.ATTACKANDMOVE);
        action.setAttackCoordinates(2, 2);
        action.setMovementCoordinates(1, 1);
        assertTrue(unit.hasNotAttacked());
        action.takeAction(game);
        assertEquals(unit, game.getMap().getTile(1, 1).getUnit());
        assertTrue(!unit.hasNotAttacked());
        assertTrue(!unit.hasNotMoved());
    }

    @Test
    public void attackAndDelayWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.ATTACKANDDELAY);
        action.setAttackCoordinates(2, 2);
        assertTrue(unit.hasNotDelayed());
        assertTrue(unit.hasNotAttacked());
        action.takeAction(game);
        assertTrue(!unit.hasNotDelayed());
        assertTrue(!unit.hasNotAttacked());
    }

    @Test(expected = NullPointerException.class)
    public void attackDoesNotWorksIfIllegalTarget() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.ATTACKANDDELAY);
        action.setAttackCoordinates(1, 1);
        action.takeAction(game);
    }

    @Test
    public void attackAndEndTurnWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Player compare = game.command().getCurrentPlayer();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        unit.moves();
        action.setType(ActionType.ATTACKANDENDTURN);
        action.setAttackCoordinates(2, 2);
        assertTrue(unit.hasNotAttacked());
        action.takeAction(game);
        assertTrue(!unit.hasNotAttacked());
        assertTrue(game.command().getCurrentPlayer() != compare);
    }

    @Test
    public void endTurnWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Player compare = game.command().getCurrentPlayer();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        action.setType(ActionType.ENDTURN);
        action.takeAction(game);
        assertTrue(!unit.hasNotAttacked());
        assertTrue(!unit.hasNotMoved());
        assertTrue(game.command().getCurrentPlayer() != compare);
    }

    @Test
    public void nullEndsTurn() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Player compare = game.command().getCurrentPlayer();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        unit.moves();
        action.setType(null);
        action.takeAction(game);
        assertTrue(game.command().getCurrentPlayer() != compare);

    }

    @Test
    public void actionCopyingWorks() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        Unit unit = game.getPlayers().get(0).getUnits().get(0);
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        Action action = new Action(0, friend.get(0));
        Action action2 = new Action(0, friend.get(0));
        action.setAttackCoordinates(2, 2);
        action.setMovementCoordinates(1, 1);
        action2.copyAttackCoordinates(action);
        action2.copyMovemenCoordinates(action);
        action2.setType(ActionType.MOVEANDATTACK);
        assertTrue(unit.hasNotAttacked());
        action2.takeAction(game);
        assertEquals(unit, game.getMap().getTile(1, 1).getUnit());
        assertTrue(!unit.hasNotAttacked());
        assertTrue(!unit.hasNotMoved());
    }

    @Test
    public void valueThingysWork() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        Action action = new Action(-10);
        Action action2 = new Action(100);
        action.updateValue(action2);
        assertEquals(90, action.getValue());
        assertEquals(action2, Action.getBetter(action, action2));
        action.swapValue();
        assertEquals(-90, action.getValue());
        action.negate();
        assertEquals(Integer.MIN_VALUE / 2, action.getValue());
        assertEquals(action, Action.getBetter(action));
    }
}
