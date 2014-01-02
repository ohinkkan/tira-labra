package tacticulous.game.graphicalui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane(), game);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container, Game game) {
        GridLayout layout = new GridLayout(3, 2);
        container.setLayout(layout);

        GraphicalMap map = new GraphicalMap(game);
        JTextArea activeInfo = new JTextArea("", 10, 20);
        JTextArea targetInfo = new JTextArea("", 10, 20);
        JTextArea actionLogText = new JTextArea("", 10, 20);
        activeInfo.setEditable(false);
        targetInfo.setEditable(false);
        actionLogText.setEditable(false);
        actionLogText.setAutoscrolls(true);

        container.add(map);
        container.add(activeInfo);
        commandPanel = commandBuilder(game, map,
                activeInfo, targetInfo, actionLogText);
        container.add(commandPanel);
        game.setCommandList(commandPanel);
        container.add(targetInfo);
        JScrollPane actionLog = new JScrollPane(actionLogText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        container.add(actionLog);
        
        if (game.getCurrentPlayer().isAi()) {
            actions.aiCommands();
        } else {
            actions.playerCommands();
        }

        game.rollForInitiative();

        actions.checkLegitActions();
        actions.updateInfo();

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

        actions = new ActionController(game, map,
                nextUnit, previousUnit, move, attack, delay, endTurn,
                active, target, log);

        game.setActions(actions);

        mouse = new MouseController(game, map);

        map.addMouseListener(mouse);

        nextUnit.addActionListener(actions);
        previousUnit.addActionListener(actions);
        move.addActionListener(actions);
        attack.addActionListener(actions);
        delay.addActionListener(actions);
        endTurn.addActionListener(actions);

        return commandPanel;
    }
}
