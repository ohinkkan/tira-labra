/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.utility;

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
public class DieTest {

    Die die;

    @Test
    public void fixedDieWorks() {
        die = new FixedDie(5);
        assertEquals(5, die.roll());
    }

    @Test
    public void rolledDieWorks() {
        die = new DieRoller();
        die.roll();
    }

    @Test
    public void rolledDieWorks2() {
        die = new DieRoller(5);
        die.roll();
    }

    @Test
    public void oneSideDieWorks() {
        die = new DieRoller(0);
        assertEquals(1, die.roll());
    }
}