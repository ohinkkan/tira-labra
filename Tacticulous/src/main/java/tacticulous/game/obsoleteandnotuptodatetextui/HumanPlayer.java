/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.obsoleteandnotuptodatetextui;

import java.util.Arrays;
import java.util.Scanner;
import tacticulous.game.commands.UnitCommand;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;
import tacticulous.tira.algorithms.GameUsage;

/**
 *
 * @author O
 */
public class HumanPlayer implements UserInterface {

    Player player;
    Game game;
    Scanner scanner;

    @Override
    public void takeTurn() {
        
        int unitIndex = 0;
        while (true) {                     
            Unit active = player.getUnitsWithActions().get(unitIndex);
            int[][] costs = GameUsage.speedRange(active, game.getMap());
//            game.getMap().drawMapWithMoveRange(costs, active.getSpeed());
            System.out.println("1 = next unit; 2 = previous unit; 3 = move unit; 4 = attack; 5 = end turn");
            int selectedCommand = scanner.nextInt();
            if (selectedCommand == 1) {
                unitIndex++;
            } else if (selectedCommand == 2) {
                unitIndex--;
            } else if (selectedCommand == 3) {
                int[] xy = getXY();
//                if (UnitCommand.move(game.getMap(), active, xy[1], xy[0], costs)) {
//                    break;
//                }
            } else if (selectedCommand == 4) {
                int[] xy = getXY();
//                int result = UnitCommand.attack(game.getMap(), active, xy[1], xy[0]);
                int result = 0;
                if (result != -2) {
                    break;
                }
            } else if (selectedCommand == 5) {
                break;
            }
            if (unitIndex < 0) {
                unitIndex = player.getUnitsWithActions().size() - 1;
            } else if (unitIndex > player.getUnitsWithActions().size() - 1) {
                unitIndex = 0;
            }

        }
    }

    private int[] getXY() {
        int[] xy = new int[2];
        System.out.println("horizontal:");
        xy[0] = scanner.nextInt();
        System.out.println("vertical:");
        xy[1] = scanner.nextInt();
        return xy;
    }

    public HumanPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
