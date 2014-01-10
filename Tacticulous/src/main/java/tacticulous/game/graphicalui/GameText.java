package tacticulous.game.graphicalui;

/**
 * Contains almost all the text used by the game.
 *
 * @author O
 */
public abstract class GameText {

    public static String newRound(int round) {
        return "round " + round + " begins";
    }

    public static String gameOver(String loser) {
        return "game over, " + loser + " has fallen";
    }

    public static String nextPlayer(String player) {
        return player + " begins turn";
    }

    public static String attackKills(String attacker, String defender) {
        return attacker + " kills " + defender;
    }

    public static String attackHits(String attacker, String defender) {
        return attacker + " hits " + defender;
    }

    public static String attackMisses(String attacker, String defender) {
        return attacker + " misses " + defender;
    }

    public static String unitMoves(String name) {
        return name + " moves";
    }

    public static String unitEndsTurn(String name) {
        return name + " ends its turn";
    }

    public static String unitDelays(String name) {
        return name + " delays its turn";
    }

}
