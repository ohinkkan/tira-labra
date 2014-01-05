package tacticulous.tira.ai;

/**
 * This class mainly exist because I wanted to use enums. Nevertheless, it
 * also provides a list of all possible combinations of actions a player can
 * take on their turn.
 *
 * @author O
 */
public enum ActionType {

    MOVEANDATTACK, ATTACKANDMOVE, ATTACKANDDELAY, ENDTURN,
    MOVEANDDELAY, MOVEANDENDTURN, ATTACKANDENDTURN, DELAY
}
