/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.graphicalui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.lang.model.type.TypeKind;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.Game;

/**
 *
 * @author O
 */
public class StartupUI implements ActionListener {

    private Game game;
    private JFrame frame;
    private JButton startGame;
    private JButton startGame2;

    public void spawn(Game game) {
        this.game = game;
        frame = new JFrame("Startup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setLocation(200, 50);
        createComponents(frame.getContentPane(), game);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container, Game game) {
        container.setLayout(new GridLayout());
        startGame = new JButton("Human vs AI");
        startGame.addActionListener(this);
        startGame.setBackground(Color.red);
        startGame.setForeground(Color.yellow);
        startGame2 = new JButton("AI vs AI");
        startGame2.addActionListener(this);
        startGame2.setBackground(Color.white);
        startGame2.setForeground(Color.blue);
        container.add(startGame);
        container.add(startGame2);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == startGame) {
            frame.dispose();

            game.setCommand(new GameCommand(game));
            game.startup();
            game.runMore();
        }
        if (ae.getSource() == startGame2) {
            frame.dispose();
            game.setCommand(new GameCommand(game));
            game.startup2();
            game.runMore();
        }
    }

}
