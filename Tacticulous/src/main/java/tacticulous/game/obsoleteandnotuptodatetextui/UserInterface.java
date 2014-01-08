/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.obsoleteandnotuptodatetextui;

import tacticulous.game.domain.Player;


/**
 *
 * @author O
 */
public interface UserInterface {
    void takeTurn();
    Player getPlayer();
}


//    this text interface is obsolete and not up to date.
//
//    private void useTextInterface() {
//        boolean gameNotOver = true;
//        ArrayList<UserInterface> playerUIs = new ArrayList();
//        for (Player player : players) {
//            if (player.isAi()) {
//                playerUIs.add(new CpuPlayer());
//            } else {
//                playerUIs.add(new HumanPlayer(player, this));
//            }
//        }
//        while (gameNotOver) {
//            for (UserInterface current : playerUIs) {
//                if (!current.getPlayer().getUnitsWithActions().isEmpty()) {
//                    current.takeAITurn();
//                } else {
//                    System.out.println("game ends, " + current.getPlayer().getName() + " has no units left");
//                    gameNotOver = false;
//                    break;
//                }
//            }
//        }
//    }

//    from battlemap.class these are used by the obsolete text UI.
//
//    public void drawMap() {
//        System.out.println("");
//        for (int i = 0; i < this.size(); i++) {
//            for (int j = 0; j < this.size(); j++) {
//                Tile tile = battlemap[i][j];
//                if (tile.getUnit() == null) {
//                    System.out.print(".");
//                } else {
//                    System.out.print(tile.getUnit());
//                }
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