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
        units.add(new Unit(3, 5, 0, 2, "A-One"));
        units.add(new Unit(6, 5, 0, 2, "B-Two"));
        units.add(new Unit(9, 5, 0, 2, "C-Three"));
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }
}
