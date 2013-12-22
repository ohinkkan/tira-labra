/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author O
 */
public class PlayerTest {

    Player player;

    @Test
    public void newPlayerHasUnitList() {
        player = new Player("A");
        assertNotNull(player.getUnits());
    }
}