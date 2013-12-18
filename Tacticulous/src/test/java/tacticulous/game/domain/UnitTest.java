/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author O
 */
public class UnitTest {

    Unit unit;

    @Test(expected = IllegalArgumentException.class)
    public void negativeSpeedNotAllowed() {
        unit = new Unit(-1, 1, 1, 1, 'A');
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroStartingHitpointsNotAllowed() {
        unit = new Unit(1, 1, 1, 0, 'A');
    }

    @Test
    public void hitRemovesHealth() {
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 1, 'B');
        unit.attack(unit2, 1);
        assertEquals(0, unit2.getHitPoints());
    }

    @Test
    public void missDoesNotRemoveHealth() {
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 1, 'B');
        unit.attack(unit2, -1);
        assertEquals(1, unit2.getHitPoints());
    }
    
    @Test
    public void unitDiesAtZeroHitpoints() {
        unit = new Unit(1, 1, 1, 1, 'A');
        Unit unit2 = new Unit(1, 1, 1, 1, 'B');
        assertEquals(1, unit.attack(unit2, 1));
    }
    
}