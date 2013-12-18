/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.utility;

/**
 *
 * @author O
 */
public class FixedDie implements Die{
    private int fixed;

    public FixedDie(int fixed) {
        this.fixed = fixed;
    }
    
    
    
    @Override
    public int roll() {
        return fixed;
    }
    
}
