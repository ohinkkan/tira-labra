package tacticulous.game.domain;

import java.util.ArrayList;

/**
 * Single tile of terrain. May be occupied by a single unit.
 *
 * @author O
 */
public class Tile {

    private int moveCost;
    private Unit unit = null;
    private ArrayList<Unit> corpses;
    private int x;
    private int y;

    /**
     * Basic constructor.
     *
     * @param moveCost how much unit speed it costs to move into this tile.
     * @param x
     * @param y
     */
    public Tile(int moveCost, int x, int y) {
        this.moveCost = moveCost;
        this.x = x;
        this.y = y;
        corpses = new ArrayList();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * How much unit speed it costs to move into this tile.
     *
     * @return
     */
    public int getMoveCost() {
        return moveCost;
    }

    public ArrayList<Unit> getCorpses() {
        return corpses;
    }

    /**
     * Shows movement cost and, if tile is occupied, unit description.
     *
     * @return
     */
    @Override
    public String toString() {
        String tile = "terrain:" + moveCost;
        if (this.unit == null) {
            return tile;
        } else {
            return tile + "\nunit in tile:\n" + unit;
        }
    }

    /**
     * Sets occupying unit. If not null, also updates unit's location.
     *
     * @param unit
     */
    public void setUnit(Unit unit) {
        this.unit = unit;
        if (this.unit != null) {
            this.unit.setXY(x, y);
        }
    }

    public Unit getUnit() {
        return unit;
    }

    public void putCorpse(Unit unit) {
        corpses.add(unit);
    }

    public int distanceTo(Tile tile) {
        return Math.max(Math.abs(this.x - tile.x), Math.abs(this.y - tile.y));
    }

    public int distanceTo(Unit unit) {
        return Math.max(Math.abs(this.x - unit.getX()), Math.abs(this.y - unit.getY()));
    }

}
