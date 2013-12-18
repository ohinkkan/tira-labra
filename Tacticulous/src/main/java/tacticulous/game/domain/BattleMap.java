/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import tacticulous.game.utility.Die;
import tacticulous.tira.algorithms.AstarSearch;

/**
 *
 * @author O
 */
public class BattleMap {

    private Die die;
    private Tile[][] battlemap;

    public BattleMap(int size, Die die) {
        this.die = die;
        if (size < 1) {
            throw new IllegalArgumentException();
        }
        this.battlemap = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                battlemap[i][j] = new Tile(1);
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

    public int size() {
        return battlemap.length;
    }

    public boolean move(int fromX, int fromY, int toX, int toY) {
        if (!legit(fromX, fromY, toX, toY)) {
            return false;
        }
        if (this.getTile(toX, toY).getUnit() != null || this.getTile(fromX, fromY).getUnit() == null) {
            return false;
        }
        if (!AstarSearch.fastEnough(this.getTile(fromX, fromY).getUnit().getSpeed(), fromX, fromY, toX, toY, this)) {
            return false;
        }
        this.getTile(toX, toY).setUnit(this.getTile(fromX, fromY).getUnit());
        this.getTile(fromX, fromY).setUnit(null);
        return true;
    }

    public boolean attack(int fromX, int fromY, int toX, int toY) {
        if (!legit(fromX, fromY, toX, toY)) {
            return false;
        }
        if (this.getTile(toX, toY).getUnit() == null || this.getTile(fromX, fromY).getUnit() == null) {
            return false;
        }
        int result = this.getTile(fromX, fromY).getUnit().attack(this.getTile(toX, toY).getUnit(), die.roll());
        if (result == 1) {
            this.getTile(toX, toY).setUnit(null);            
        }
        return true;
    }

    private boolean legit(int... check) {
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
