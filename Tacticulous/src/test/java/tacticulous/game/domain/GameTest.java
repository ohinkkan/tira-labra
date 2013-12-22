/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;

/**
 *
 * @author O
 */
public class GameTest {

    Game game;

    @Test
    public void newGameNoNullAssets() {
        game = new Game();
        assertNotNull(game.getDie());
        assertNotNull(game.getMap());
        assertNotNull(game.getPlayers());
    }

    @Test
    public void unitPlacementWorks() {
        game = new Game();
        assertNotNull(game.getMap().getTile(0, 4).getUnit());
        assertNotNull(game.getMap().getTile(9, 4).getUnit());
    }
    
    @Test
    public void unitPlacementReturnsFalseIfTooManyUnits() {
        game = new Game();
        ArrayList<Unit> units = game.getPlayers().get(0).getPlayer().getUnits();
        for (int i = 0; i < 8; i++) {
            units.add(new Unit(1,1,1,1,"A"));
        }
        assertEquals(false,game.placeUnits(units));
    }
}