/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.ui;

import java.util.Arrays;
import java.util.Scanner;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
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
            int[][] costs = GameUsage.speedRange(player.getUnits().get(whatUnit), game.getMap());
            game.getMap().drawMapWithMoveRange(costs, player.getUnits().get(whatUnit).getSpeed());

            System.out.println("1 = next unit; 2 = previous unit; 3 = end turn");
            int pick = scanner.nextInt();
            if (pick == 1) {
                whatUnit++;
            } else if (pick == 2) {
                whatUnit--;
            } else if (pick == 3) {
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
