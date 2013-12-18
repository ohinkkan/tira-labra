/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.utility;

import java.util.Random;

/**
 *
 * @author O
 */
public class DieRoller implements Die {
    private Random rand;
    public DieRoller() {
        this.rand = new Random();
    }

    @Override
    public int roll() {
        return rand.nextInt(10);
    }
            
}
