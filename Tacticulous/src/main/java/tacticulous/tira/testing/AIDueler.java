/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.tira.testing;

import java.util.Scanner;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.*;
import tacticulous.tira.ai.ArtificialIntelligence;

/**
 *
 * @author O
 */
public class AIDueler {

    private int ai1agg = 10;
    private int ai1def = 10;
    private int ai1ran = 1;
    private int ai1tts = 1;
    private int ai2agg = 10;
    private int ai2def = 10;
    private int ai2ran = 1;
    private int ai2tts = 1;
    private int mapSize = 10;
    private int maxUnits = 10;
    private int gamesToRun = 1;
    private boolean notOver = true;
    private boolean killLeader = false;

    private static Player aiFight(Game game, Player ai1, Player ai2,
            BattleMap map, Boolean killLeader, int maxUnits) {

        game.setCommand(new GameCommand(game));
        game.getPlayers().add(ai1);
        game.getPlayers().add(ai2);
        int unitLimit = Math.min(map.size() / 3, maxUnits / 3);
        for (int i = 0; i < unitLimit; i++) {
            ai1.quickStartUnits(1);
            ai2.quickStartUnits(1);
        }
        if (killLeader) {
            ai1.getUnits().remove(ai1.getUnits().get(0));
            ai1.addUnit(new Unit(ai1));
            ai2.getUnits().remove(ai2.getUnits().get(0));
            ai2.addUnit(new Unit(ai2));
            game.setKillLeader(true);
        }
        game.setMap(map);
        game.placeUnits(ai1.getUnits());
        game.placeUnits(ai2.getUnits());
        game.command().rollForInitiative();
        game.command().getCurrentPlayer().getAi().takeTurn();

        int startUnits = ai1.getUnits().size();
        for (int i = 0; i < Math.pow(map.size(), 3); i++) {
            if (game.command().checkIfGameOver()) {
                if (!killLeader) {
                    for (Player player : game.getPlayers()) {
                        if (!player.getUnits().isEmpty()) {
                            System.out.print("Winner: " + player.getName()
                                    + ", units alive " + player.getUnits().size()
                                    + "/" + startUnits + ". Game lasted: " + i + " turns, "
                                    + game.getRound() + " rounds, ");
                            return player;
                        }
                    }
                } else {
                    for (Player player : game.getPlayers()) {
                        if (!player.leaderIsDead()) {
                            System.out.print("Winner: " + player.getName()
                                    + ", units alive " + player.getUnits().size()
                                    + "/" + startUnits + ". ");
                        } else {
                            System.out.print("Loser: " + player.getName()
                                    + ", units alive " + player.getUnits().size()
                                    + "/" + startUnits + ". ");
                        }

                    }
                    for (Player player : game.getPlayers()) {
                        if (!player.leaderIsDead()) {
                            System.out.print("Game lasted: " + i + " turns, "
                                    + game.getRound() + " rounds, ");
                            return player;
                        }
                    }
                }
                break;
            }
            game.command().getCurrentPlayer().getAi().takeTurn();
        }
        System.out.print("Match inconclusive, aborted after " + (int) Math.pow(map.size(), 3) + " turns, ");
        return null;
    }

    private void fight(int howManyTimes, boolean killLeader, int maxUnits) {
        int wins1 = 0;
        int wins2 = 0;
        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < howManyTimes; i++) {

            Game game = new Game();
            Player player1 = new Player("Player 1", null);
            Player player2 = new Player("Player 2", null);
            player1.setAI(new ArtificialIntelligence(game, player1, ai1tts, ai1ran, ai1agg, ai1def));
            player2.setAI(new ArtificialIntelligence(game, player2, ai2tts, ai2ran, ai2agg, ai2def));
            BattleMap map = new BattleMap(mapSize, 4);
            int x = i + 1;
            System.out.print("Game " + x + ": ");
            long startTime = System.currentTimeMillis();
            Player player = aiFight(game, player1, player2, map, killLeader, maxUnits);
            long stopTime = System.currentTimeMillis();
            if (player == player1) {
                wins1++;
            }
            if (player == player2) {
                wins2++;
            }
            long length = stopTime - startTime;
            System.out.println(length+"ms.");
        }
        long stopTime = System.currentTimeMillis();
        long length = stopTime - startTime1;
        System.out.println("Player 1 victories: " + wins1 + ", Player 2 victories: " + wins2 + ", total time "+length+"ms\n");
    }

    public static void aiTester() {
        Scanner scanner = new Scanner(System.in);
        AIDueler dueler = new AIDueler();
        System.out.println("Welcome to AI Dueler 3000!\n");
        while (dueler.notOver) {
            dueler.mainMenu();
            String command = scanner.nextLine();
            if (checkCommand(command)) {
                if (Integer.parseInt(command) > 0 && Integer.parseInt(command) < 12) {
                    System.out.print("Input new value:\n>");
                    String input = scanner.nextLine();
                    if (checkCommand(input)) {
                        dueler.command(Integer.parseInt(command), Integer.parseInt(input));
                    } else {
                        System.out.println("Invalid command.");
                    }
                } else if (Integer.parseInt(command) == 12) {
                    dueler.killLeader = !dueler.killLeader;
                } else if (Integer.parseInt(command) == 13) {
                    dueler.fight(dueler.gamesToRun, dueler.killLeader, dueler.maxUnits);
                } else if (Integer.parseInt(command) == 14) {
                    dueler.notOver = false;
                } else {
                    System.out.println("Invalid command.");
                }
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    private void mainMenu() {
        System.out.print("Input command:\n\n"
                + "1 = AI1 turns to simulate (now " + ai1tts
                + ") 2 = AI1 aggressiveness (now " + ai1agg
                + ") 3 = AI1 defensiveness (now " + ai1def
                + ") 4 = AI1 randomness (now " + ai1ran
                + ")\n\n5 = AI2 turns to simulate (now " + ai2tts
                + ") 6 = AI2 aggressiveness (now " + ai2agg
                + ") 7 = AI2 defensiveness (now " + ai2def
                + ") 8 = AI2 randomness (now " + ai2ran
                + ")\n\n9 = set map size (now " + mapSize
                + ") 10 = set # of games to run (now " + gamesToRun + ""
                + ") 11 = select maximum number of units per player (now " + maxUnits
                + ")\n\n12 = toggle kill leader (now " + killLeader
                + ") 13 = start dueling "
                + " 14 = quit AI dueler\n"
                + ">");
    }

    private static boolean checkCommand(String nextLine) {
        try {
            Integer.parseInt(nextLine);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void command(int command, int input) {
        if (command == 1) {
            if (input > 0) {
                ai1tts = input;
            } else {
                ai1tts = 1;
            }
        } else if (command == 2) {
            ai1agg = input;
        } else if (command == 3) {
            ai1def = input;
        } else if (command == 4) {
            if (input > -1) {
                ai1ran = input;
            } else {
                ai1ran = 0;
            }
        } else if (command == 5) {
            if (input > 0) {
                ai2tts = input;
            } else {
                ai2tts = 1;
            }
        } else if (command == 6) {
            ai2agg = input;
        } else if (command == 7) {
            ai2def = input;
        } else if (command == 8) {
            if (input > -1) {
                ai2ran = input;
            } else {
                ai2ran = 0;
            }
        } else if (command == 9) {
            if (input > 2) {
                mapSize = input;
            } else {
                mapSize = 3;
            }
        } else if (command == 10) {
            if (input > 0) {
                gamesToRun = input;
            } else {
                gamesToRun = 1;
            }
        } else if (command == 11) {
            if (input > 2) {
                maxUnits = input;
            } else {
                maxUnits = 3;
            }
        }
        System.out.println("Value set. If input was too small, value set to minimum allowed.");
    }

}
