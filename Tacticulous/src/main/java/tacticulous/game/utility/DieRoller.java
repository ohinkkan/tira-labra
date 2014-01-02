package tacticulous.game.utility;

import java.util.Random;

/**
 *  This die returns a random value starting from 1 up to its number of sides.
 *  Because why not.
 * 
 * @author O
 */
public class DieRoller implements Die {

    private Random rand;
    private int dieSides;

    /**
     * Constructs a basic 10-sided die.
     */
    public DieRoller() {
        this.rand = new Random();
        this.dieSides = 10;
    }

    /**
     * Constructs a die with custom number of sides.
     * 
     * @param dieSides if less than 1, the die will always roll 1.
     * It's like a ball or something I guess.
     */
    public DieRoller(int dieSides) {
        this.rand = new Random();
        this.dieSides = dieSides;
    }

    /**  
     * @return random integer between 1 and dieSides. 
     */
    @Override
    public int roll() {
        if (dieSides<2) {
            return 1;
        }
        return rand.nextInt(dieSides)+1;
    }
}
