package tacticulous.game.domain;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author O
 */
public class TileTest {

    Unit unit;
    Tile tile;

    @Test(expected = IllegalArgumentException.class)
    public void illegalTileTest() {
        tile = new Tile(-1, 2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalTileTest2() {
        tile = new Tile(1, -1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalTileTest3() {
        tile = new Tile(1, 2, -1);
    }

    @Test
    public void notNullUnitSetWorks() {
        tile = new Tile(1, 2, 3);
        unit = new Unit(1, 1, 1, 1, "1");
        tile.setUnit(unit);
        assertEquals(2, tile.getUnit().getX());
        assertEquals(3, tile.getUnit().getY());
        assertEquals(unit, tile.getUnit());
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

    @Test
    public void tileToTileDistanceWorks() {
        tile = new Tile(1, 1, 1);
        assertEquals(0, tile.distanceTo(tile));

        Tile tile2 = new Tile(1, 2, 2);
        assertEquals(1, tile.distanceTo(tile2));

        tile2 = new Tile(1, 7, 1);
        assertEquals(6, tile.distanceTo(tile2));

        tile2 = new Tile(1, 1, 121);
        assertEquals(120, tile.distanceTo(tile2));
    }

    @Test
    public void tileToUnitDistanceWorks() {
        tile = new Tile(1, 1, 1);
        unit = new Unit(1, 1, 1, 1, "1");
        tile.setUnit(unit);
        assertEquals(0, tile.distanceTo(unit));

        Tile tile2 = new Tile(1, 2, 2);
        assertEquals(1, tile2.distanceTo(unit));

        tile2 = new Tile(1, 7, 1);
        assertEquals(6, tile2.distanceTo(unit));

        tile2 = new Tile(1, 1, 121);
        assertEquals(120, tile2.distanceTo(unit));

        tile2 = new Tile(1, 11, 11);
        assertEquals(10, tile2.distanceTo(unit));
    }
}
