/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;

/**
 *
 * @author O
 */
public class BattleMap {

    private Die die;

    public Die getDie() {
        return die;
    }
    private Tile[][] battlemap;

    public BattleMap(int size, Die die, int random) {
        if (size < 3 || random < 0) {
            throw new IllegalArgumentException();
        }
        this.die = die;
        Die randomTerrain = new DieRoller(random);
        
        this.battlemap = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                battlemap[i][j] = new Tile(randomTerrain.roll()+1, i, j);
            }
        }
    }

    public void drawMap() {
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                System.out.print(battlemap[i][j]);
            }
            System.out.println("");
        }
    }

    public void drawMapWithMoveRange(int[][] range, int speed) {
        for (int i = 0; i < this.size(); i++) {
            for (int j = 0; j < this.size(); j++) {
                if (range[i][j] > speed || battlemap[i][j].getUnit() != null) {
                    System.out.print(battlemap[i][j]);
                } else {
                    System.out.print(range[i][j]);
                }
            }
            System.out.println("");
        }
    }

    public int size() {
        return battlemap.length;
    }

    public boolean legit(int... check) {
        for (int i = 0; i < check.length; i++) {
            if (check[i] < 0 || check[i] > this.size() - 1) {
                return false;
            }
        }
        return true;
    }

    public Tile getTile(int x, int y) {
        return battlemap[x][y];
    }
}
