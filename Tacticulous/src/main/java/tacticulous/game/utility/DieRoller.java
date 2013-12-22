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
    private int dieSize;
    private boolean notRandom = false;

    public DieRoller() {
        this.rand = new Random();
        this.dieSize = 10;
    }

    public DieRoller(int dieSize) {
        if (dieSize < 1) {
            notRandom = true;
        }
        this.rand = new Random();
        this.dieSize = dieSize;
    }

    @Override
    public int roll() {
        if (notRandom) {
            return 0;
        }
        return rand.nextInt(dieSize);
    }
}
