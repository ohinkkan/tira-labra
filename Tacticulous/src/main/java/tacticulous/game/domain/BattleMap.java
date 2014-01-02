package tacticulous.game.domain;

import tacticulous.game.utility.Die;
import tacticulous.game.utility.DieRoller;

/**
 *  Basically a container grid for tiles. 
 * 
 * @author O
 */
public class BattleMap {

    private Tile[][] battlemap;

    /**
     * Constructor which randomises terrain movement costs.
     * 
     * @param size width and height of map, must be 3 or higher
     * @param random how much the terrain varies. 0 means no variation. 
     * Must be non-negetive.
     */
    public BattleMap(int size, int random) {
        if (size < 3 || random < 0) {
            throw new IllegalArgumentException();
        }
        Die randomTerrain = new DieRoller(random);
        this.battlemap = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                battlemap[i][j] = new Tile(randomTerrain.roll(), i, j);
            }
        }
    }

    /**
     * Constructor which uses an int array to determine tile movement costs.
     * 
     * @param map must be a square array of size 3 or higher with positive
     * numbers.
     */
    public BattleMap(int[][] map) {
        this.battlemap = new Tile[map.length][map.length];
        for (int i = 0; i < battlemap.length; i++) {
            if (map.length != map[i].length || map.length < 3) {
                throw new IllegalArgumentException();
            }
            for (int j = 0; j < battlemap.length; j++) {
                if (map[i][j] < 1) {
                    throw new IllegalArgumentException();
                }
                battlemap[i][j] = new Tile(map[i][j], i, j);
            }
        }
    }




    /**
     * Returns the width and height (in tiles) of battlemap.
     * @return
     */
    public int size() {
        return battlemap.length;
    }

    /**
     * Checks if provided integer or integers are within map boundaries.
     * 
     * @param check integers to be checked
     * @return false if any integer is outside map boundaries.
     */
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
    
    
//    these are used by the obsolete text UI.
//    
//    public void drawMap() {
//        for (int i = 0; i < this.size(); i++) {
//            for (int j = 0; j < this.size(); j++) {
//                System.out.print(battlemap[i][j]);
//            }
//            System.out.println("");
//        }
//    }
//
//    public void drawMapWithMoveRange(int[][] range, int speed) {
//        System.out.print("    ");
//        for (int i = 0; i < this.size(); i++) {
//            System.out.format("%4s", i);
//        }
//        System.out.println("");
//        for (int i = 0; i < this.size(); i++) {
//            System.out.format("%4s", i);
//            for (int j = 0; j < this.size(); j++) {
//                    
//                if (range[i][j] > speed || battlemap[i][j].getUnit() != null) {
//                    System.out.format("%4s", battlemap[i][j]);
//                } else {
//                    System.out.format("%4s", range[i][j]);
//
//                }
//            }
//            System.out.println("");
//        }
//    }
}
