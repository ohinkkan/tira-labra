package tacticulous.game.domain;

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

    @Test(expected = IllegalArgumentException.class)
    public void negativeSpeedNotAllowed2() {
        unit = new Unit(-1, 1, 1, 1, 1, "A", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroStartingHitpointsNotAllowed2() {
        unit = new Unit(1, 1, 1, 1, 0, "A", null);
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

    @Test
    public void doneForTheRoundAndNewRoundWork() {
        Player player = new Player("A", null);
        player.testUnits();
        player.newRoundUnitReset();
        unit = player.getUnits().get(0);
        assertTrue(!unit.doneForTheRound());
        unit.attacks();
        assertTrue(!unit.doneForTheRound());
        unit.moves();
        assertTrue(unit.doneForTheRound());
        unit.newRound();
        assertTrue(unit.hasNotAttacked());
        assertTrue(unit.hasNotMoved());
    }

}
