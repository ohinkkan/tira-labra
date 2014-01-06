package tacticulous.game.graphicalui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import tacticulous.game.domain.Game;

/**
 * Supreme Ruler class for the graphical user interface.
 *
 * @author O
 */
public class UiView {

    private JFrame frame;
    private JPanel commandPanel;
    private ActionController actions;
    private MouseController mouse;

    /**
     * Creates the main game window.
     *
     * @param game provides access to necessary data
     */
    public void spawn(Game game) {
        frame = new JFrame("Tacticulous");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        createComponents(frame.getContentPane(), game);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container, Game game) {
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        JPanel leftSide = new JPanel();
        JPanel rightSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));

        GraphicalMap map = new GraphicalMap(game);
        JTextArea activeInfo = new JTextArea("", 10, 20);
        JTextArea targetInfo = new JTextArea("", 10, 20);
        JTextArea actionLogText = new JTextArea("", 10, 20);
        commandPanel = commandBuilder(game, map,
                activeInfo, targetInfo, actionLogText);
        JScrollPane actionLog = new JScrollPane(actionLogText,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        activeInfo.setEditable(false);
        targetInfo.setEditable(false);
        actionLogText.setEditable(false);
        actionLogText.setAutoscrolls(true);

        container.add(leftSide);
        container.add(rightSide);

        leftSide.add(map);
        leftSide.add(commandPanel);
        rightSide.add(activeInfo);
        rightSide.add(new JSeparator(SwingConstants.HORIZONTAL));
        rightSide.add(targetInfo);
        rightSide.add(actionLog);

        map.setPreferredSize(new Dimension(800, 800));
        rightSide.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        commandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        game.setCommandList(commandPanel);
        if (game.getCurrentPlayer().isAi()) {
            actions.aiCommands();
        } else {
            actions.playerCommands();
        }
    }

    private JPanel commandBuilder(Game game, GraphicalMap map,
            JTextArea active, JTextArea target, JTextArea log) {

        commandPanel = new JPanel(new GridLayout(2, 1));

        JButton nextUnit = new JButton("Next unit");
        JButton previousUnit = new JButton("Previous unit");
        JButton move = new JButton("Move");
        JButton attack = new JButton("Attack");
        JButton delay = new JButton("Delay turn");
        JButton endTurn = new JButton("End turn");
        JButton takeTurn = new JButton("Take turn");
        JButton autoTurn = new JButton("Auto turn");


        actions = new ActionController(game, map,
                nextUnit, previousUnit, move, attack, delay, endTurn,
                takeTurn, autoTurn, active, target, log);

        game.setActions(actions);

        mouse = new MouseController(game, map);

        map.addMouseListener(mouse);

        nextUnit.addActionListener(actions);
        previousUnit.addActionListener(actions);
        move.addActionListener(actions);
        attack.addActionListener(actions);
        delay.addActionListener(actions);
        endTurn.addActionListener(actions);
        takeTurn.addActionListener(actions);
        autoTurn.addActionListener(actions);

        return commandPanel;
    }
}