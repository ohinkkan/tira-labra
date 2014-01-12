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

/**
 *
 * @author O
 */
public class ValueLogicTest {

    private Game game;
    private GameCommand command;
    private ArtificialIntelligence ai;
    private SimulatedRound round;

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
    public void attackValue1() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 1, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(321, ai.getValueLogic().attackValue(friend.get(2), enemy.get(1), round));
    }

    @Test
    public void attackValueAggressionIncreasesValue() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 5);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(801, ai.getValueLogic().attackValue(friend.get(0), enemy.get(1), round));
    }

    @Test
    public void delayValue1() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 1);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(-104, ai.getValueLogic().delayValue(friend.get(0), round));
    }

    @Test
    public void delayValueDefensivenessDecreasesValueWhenEnemiesInRange() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 2);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(-419, ai.getValueLogic().delayValue(friend.get(0), round));
    }

    @Test
    public void delayValueEnemiesNotThreatening() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 2);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        for (AIUnit unit : enemy) {
            unit.attacks();
            unit.moves();
        }
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(1, ai.getValueLogic().delayValue(friend.get(0), round));
    }

    @Test
    public void movementValue1() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 2);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(-25, ai.getValueLogic().movementValue(friend.get(0), map.getTile(1, 1), round));
    }

    @Test
    public void movementValueEnemiesNotThreatening() {
        command.rollForInitiative();
        command.nextTurn();
        ai = new ArtificialIntelligence(game, command.getCurrentPlayer(), 1, 0, 2, 2);
        command.getCurrentPlayer().setAI(ai);
        BattleMap map = game.getMap().copy();
        TacList<AIUnit> friend = ai.enterTheMatrix(game.getPlayers().get(0), map);
        TacList<AIUnit> enemy = ai.enterTheMatrix(game.getPlayers().get(1), map);
        for (AIUnit unit : enemy) {
            unit.attacks();
            unit.moves();
        }
        round = new SimulatedRound(friend, enemy, ai, map);
        assertEquals(5, ai.getValueLogic().movementValue(friend.get(0), map.getTile(1, 1), round));
    }

}
