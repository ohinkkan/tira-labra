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
        unit = new Unit(-1, 1, 1, 1, "A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroStartingHitpointsNotAllowed() {
        unit = new Unit(1, 1, 1, 0, "A");
    }

    @Test
    public void hitRemovesHealth() {
        unit = new Unit(1, 1, 1, 2, "A");
        unit.isHitAndDies();
        assertEquals(1, unit.getHitPoints());
    }

    @Test
    public void killsWhenHealthAtZero() {
        unit = new Unit(1, 1, 1, 1, "A");
        assertEquals(true, unit.isHitAndDies());
    }
    
    @Test
    public void noKillIfHealthAboveZero() {
        unit = new Unit(1, 1, 1, 2, "A");
        assertEquals(false, unit.isHitAndDies());
    }
    
    
}