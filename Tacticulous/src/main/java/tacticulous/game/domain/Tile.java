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

    public Tile(int moveCost) {
        this.moveCost = moveCost;
    }

    public int getMoveCost() {
        return moveCost;
    }

    @Override
    public String toString() {
        if (unit != null) {
            return ""+unit.getNimi();
        }
        return ".";
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }
    
    
    
}
