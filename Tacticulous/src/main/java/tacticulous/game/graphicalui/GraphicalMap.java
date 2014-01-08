package tacticulous.game.graphicalui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Tile;
import tacticulous.game.domain.Unit;

/**
 * Draws the battlemap display.
 *
 * @author O
 */
public class GraphicalMap extends JComponent {

    private final Game game;
    private int scale;
    private Tile tile;

    /**
     * Initializes battlemap display.
     *
     * @param game provides access to necessary data
     */
    public GraphicalMap(Game game) {
        super();
        this.game = game;
        scale = 20;
        super.setBackground(Color.BLACK);
    }

    /**
     * Draws the map, tile by tile.
     *
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        scale = Math.max(Math.min(this.getHeight(), this.getWidth()) / game.getMap().size(), 1);
        for (int i = 0; i < game.getMap().size(); i++) {
            for (int j = 0; j < game.getMap().size(); j++) {
                tile = game.getMap().getTile(i, j);
                Unit unit = tile.getUnit();
                if (unit != null) {
                    if (tile == game.command().getTargetTile()) {
                        drawUnit(Color.PINK, i, j, graphics);
                    } else if (unit == game.command().getActiveUnit()) {
                        drawUnit(Color.CYAN, i, j, graphics);
                    } else {
                        drawUnit(unit.getPlayer().getColor(), i, j, graphics);
                    }
                } else if (tile == game.command().getTargetTile()) {
                    drawUnit(Color.YELLOW, i, j, graphics);
                } else if (!tile.getCorpses().isEmpty()) {
                    drawUnit(Color.getHSBColor(100F, 0.5F, 0.5F), i, j, graphics);
                } else if (game.command().getActiveUnit() != null) {
                    if (game.command().getActiveUnit().hasNotMoved()
                            && game.command().getActiveUnit().getSpeed() >= game.command().getMoveCosts()[i][j]) {
                        drawMovement(i, j, graphics);
                    } else {
                        drawTile(tile.getMoveCost(), i, j, graphics);
                    }
                } else {
                    drawTile(tile.getMoveCost(), i, j, graphics);
                }
            }
        }
    }

    /**
     * Draws a single unit.
     *
     * @param color Unit's color
     * @param i
     * @param j
     * @param graphics
     */
    private void drawUnit(Color color, int i, int j, Graphics graphics) {
        drawTile(tile.getMoveCost(), i, j, graphics);
        graphics.setColor(color);
        drawSquare(i, j, Math.max(1, scale / 2), graphics);
    }

    /**
     * Draws a small movement indicator square.
     *
     * @param i
     * @param j
     * @param graphics
     */
    private void drawMovement(int i, int j, Graphics graphics) {
        drawTile(tile.getMoveCost(), i, j, graphics);
        graphics.setColor(Color.GREEN);
        drawSquare(i, j, Math.max(1, scale / 4), graphics);
    }

    /**
     * Draws a single terrain tile.
     *
     * @param terrain
     * @param i
     * @param j
     * @param graphics
     */
    private void drawTile(int terrain, int i, int j, Graphics graphics) {
        if (terrain == 1) {
            graphics.setColor(Color.WHITE);
        } else if (terrain < 3) {
            graphics.setColor(Color.LIGHT_GRAY);
        } else if (terrain < 4) {
            graphics.setColor(Color.GRAY);
        } else {
            graphics.setColor(Color.DARK_GRAY);
        }
        drawSquare(i, j, scale - 1, graphics);
    }

    /**
     * Scale is the size in pixels of a single tile. It is calculated from the
     * sizes of the map display and battlemap.
     *
     * @return the current scale of the map.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Draws a single square.
     *
     * @param i
     * @param j
     * @param size
     * @param graphics
     */
    private void drawSquare(int i, int j, int size, Graphics graphics) {
        int diff = (scale - size) / 2;
        graphics.fill3DRect(j * scale + diff, i * scale + diff, size, size, true);
    }
}
