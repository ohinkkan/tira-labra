/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import java.util.ArrayList;

/**
 *
 * @author O
 */
public class Player {

    private String name;
    private ArrayList<Unit> units;

    public Player(String name) {
        this.name = name;

        units = new ArrayList();

    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void testUnits() {
        units.add(new Unit(3, 5, 0, 2, 1, "A-One", this));
        units.add(new Unit(6, 5, 0, 2, 2, "B-Two", this));
        units.add(new Unit(9, 5, 0, 2, 5, "C-Three", this));
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void kill(Unit unit) {
        units.remove(unit);
    }
}
