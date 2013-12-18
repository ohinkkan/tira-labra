package tacticulous.game;

import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Unit;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.FixedDie;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Die die = new FixedDie(5);
        BattleMap map = new BattleMap(10, die);
        map.getTile(0, 0).setUnit(new Unit(1,1,1,1,'D'));
        map.move(0, 0, 5, 5);
        map.getTile(0, 0).setUnit(new Unit(1,1,1,1,'A'));
        map.drawMap();
        map.attack(0, 0, 5, 5);
        map.drawMap();
    }
}
