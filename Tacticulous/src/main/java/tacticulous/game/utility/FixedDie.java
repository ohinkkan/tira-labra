package tacticulous.game.utility;

/**
 * This die always returns the same value. 
 * Mostly for testing purposes, I swear.
 * 
 * @author O
 */

public class FixedDie implements Die{
    private int fixed;

    /**
     * Constructs a die that always rolls the same.
     * 
     * @param fixed the desired result.
     */
    public FixedDie(int fixed) {
        this.fixed = fixed;
    }

    /**
     * Returns the fixed result.
     * 
     * @return
     */
    @Override
    public int roll() {
        return fixed;
    }
    
}
