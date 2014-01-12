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
import tacticulous.game.domain.Player;

/**
 *
 * @author O
 */
public class AIUnitTest {

    AIUnit unit;

    @Test
    public void constructorWorks() {
        unit = new AIUnit(1, 2, 3, 4, 5, "Test1", new Player("Test2", null), 0, 0, true, true, true, 0, true);
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotDelayed());
        assertTrue(unit.hasNotMoved());
        assertTrue(unit.isLeader());
        assertEquals(0, unit.getAttackedCount());
        assertEquals(0, unit.getX());
        assertEquals(0, unit.getY());

        unit = new AIUnit(1, 2, 3, 4, 5, "Test1", new Player("Test2", null), 4, 7, false, false, false, 5, false);
        assertTrue(!unit.hasNotAttacked());
        assertTrue(!unit.hasNotDelayed());
        assertTrue(!unit.hasNotMoved());
        assertTrue(!unit.isLeader());
        assertEquals(5, unit.getAttackedCount());
        assertEquals(4, unit.getX());
        assertEquals(7, unit.getY());
    }

    @Test
    public void valueWorks() {
        unit = new AIUnit(1, 2, 3, 4, 2, "Test1", new Player("Test2", null), 0, 0, true, true, true, 0, true);
        assertEquals(60, unit.targetValue());
        unit = new AIUnit(1, 2, 3, 4, 1, "Test1", new Player("Test2", null), 0, 0, true, true, true, 1, false);
        assertEquals(5, unit.targetValue());
    }

    @Test
    public void undosWork() {
        unit = new AIUnit(1, 2, 3, 4, 2, "Test1", new Player("Test2", null), 0, 0, true, true, true, 0, true);
        assertTrue(unit.hasNotMoved());
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotDelayed());
        assertEquals(0, unit.getAttackedCount());
        unit.moves();
        assertTrue(!unit.hasNotMoved());
        unit.attacks();
        assertTrue(!unit.hasNotAttacked());
        unit.delays();
        assertTrue(!unit.hasNotDelayed());
        unit.attacked();
        unit.attacked();
        assertEquals(2, unit.getAttackedCount());
        unit.undoMove();
        assertTrue(unit.hasNotMoved());
        unit.undoAttack();
        assertTrue(unit.hasNotAttacked());
        unit.undoDelay();
        assertTrue(unit.hasNotDelayed());
        unit.undoAttacked();
        assertEquals(1, unit.getAttackedCount());
    }

}
