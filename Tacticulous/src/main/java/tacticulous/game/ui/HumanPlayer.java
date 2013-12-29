/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.ui;

import java.util.Arrays;
import java.util.Scanner;
import tacticulous.game.commands.Command;
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

    @Override
    public void takeTurn() {
        Scanner scanner = new Scanner(System.in);
        int whatUnit = 0;
        while (true) {
            Unit active = player.getUnits().get(whatUnit);
            int[][] costs = GameUsage.speedRange(active, game.getMap());
            game.getMap().drawMapWithMoveRange(costs, active.getSpeed());

            System.out.println("1 = next unit; 2 = previous unit; 3 = move unit; 4 = attack; 5 = end turn");
            int pick = scanner.nextInt();
            if (pick == 1) {
                whatUnit++;
            } else if (pick == 2) {
                whatUnit--;
            } else if (pick == 3) {
                System.out.println("x:");
                int x = scanner.nextInt();
                System.out.println("y:");
                int y = scanner.nextInt();
                if (Command.move(game.getMap(), active, x, y, costs)) {
                    break;
                }
            } else if (pick == 4) {
                System.out.println("x:");
                int x = scanner.nextInt();
                System.out.println("y:");
                int y = scanner.nextInt();
                int result = Command.attack(game.getMap(),active, x, y);
                if (result != -2) {
                    break;
                }
            } else if (pick == 5) {
                break;
            }
            if (whatUnit < 0) {
                whatUnit = player.getUnits().size() - 1;
            } else if (whatUnit > player.getUnits().size() - 1) {
                whatUnit = 0;
            }

        }
    }

    public HumanPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
