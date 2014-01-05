package tacticulous.game.graphicalui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import tacticulous.game.domain.Game;

/**
 * Control and logic class for using mouse in graphical map display.
 *
 * @author O
 * @see tacticulous.game.graphicalui.ActionController
 */
public class MouseController implements MouseListener {

    private Game game;

    private GraphicalMap map;

    /**
     * Basic constructor.
     *
     * @param game provides access to necessary data
     * @param map access to map display
     */
    public MouseController(Game game, GraphicalMap map) {
        this.game = game;
        this.map = map;
    }

    /**
     * Reacts to player's clicks on map display.
     *
     * @param me triggering mouse event
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (game.checkIfGameOver()) {
            return;
        }
        map.repaint();
        int x = me.getX() / map.getScale();
        int y = me.getY() / map.getScale();
        if (game.getMap().legit(x, y)) {
            game.setTargetTile(game.getMap().getTile(y, x));
            game.getActions().checkLegitActions();
            game.getActions().updateUnitDisplays();
            map.repaint();
            if (me.getClickCount() > 1) {
                if (game.getActions().canAttack()) {
                    game.command().attack();
                    map.repaint();
                } else if (game.getActions().canMove()) {
                    game.command().move();
                    map.repaint();
                }
                game.getActions().checkLegitActions();
                game.getActions().updateUnitDisplays();
            }
        }

    }

    /**
     * Unused for now.
     *
     * @param me triggering mouse event
     */
    @Override
    public void mousePressed(MouseEvent me) {
    }

    /**
     * Unused for now.
     *
     * @param me triggering mouse event
     */
    @Override
    public void mouseReleased(MouseEvent me) {
    }

    /**
     * Unused for now.
     *
     * @param me triggering mouse event
     */
    @Override
    public void mouseEntered(MouseEvent me) {
    }

    /**
     * Unused for now.
     *
     * @param me triggering mouse event
     */
    @Override
    public void mouseExited(MouseEvent me) {
    }
}