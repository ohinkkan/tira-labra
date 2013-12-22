/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

/**
 *
 * @author O
 */
public class Tile {

    private int moveCost;
    private Unit unit = null;
    private int x;
    private int y;

    public Tile(int moveCost, int x, int y) {
        this.moveCost = moveCost;
        this.x = x;
        this.y = y;
    }

    public int getMoveCost() {
        return moveCost;
    }

    @Override
    public String toString() {
        if (unit != null) {
            return "" + unit.getNimi().charAt(0);
        }
        return ".";
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        if (this.unit != null) {
            this.unit.setXY(x, y);
        }
    }

    public Unit getUnit() {
        return unit;
    }
}
