package tacticulous.game.graphicalui;

/**
 * Contains almost all the text used by the game.
 * 
 * @author O
 */
public abstract class GameText {

    public static String newRound(int round) {
        return "round "+round+" begins";
    }

    public static String gameOver() {
        return "game over";
    }

    public static String nextPlayer(String player) {
        return player+" begins turn";
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

}
