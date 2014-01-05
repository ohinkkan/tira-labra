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
public class TileTest {

    Unit unit;
    Tile tile;

    @Test
    public void notNullUnitSetWorks() {
        tile = new Tile(1, 2, 3);
        unit = new Unit(1, 1, 1, 1, "1");
        tile.setUnit(unit);
        assertEquals(2, tile.getUnit().getX());
        assertEquals(3, tile.getUnit().getY());
    }

    @Test
    public void nullUnitSetWorks() {
        tile = new Tile(1, 2, 3);
        unit = new Unit(1, 1, 1, 1, "1");
        tile.setUnit(unit);
        tile.setUnit(null);
        assertNull(tile.getUnit());
    }

    @Test
    public void tileToStringWorksOrAtLeastDoesNotCauseErrors() {
        tile = new Tile(1, 2, 3);
        tile.toString();
        unit = new Unit(1, 1, 1, 1, 1, "A", new Player("A", null));
        tile.setUnit(unit);
        tile.toString();
    }
}
