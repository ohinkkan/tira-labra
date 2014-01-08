package tacticulous.game.graphicalui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.lang.model.type.TypeKind;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import tacticulous.game.commands.GameCommand;
import tacticulous.game.domain.BattleMap;
import tacticulous.game.domain.Game;
import tacticulous.game.domain.Player;
import tacticulous.game.domain.Unit;
import tacticulous.tira.ai.ArtificialIntelligence;

/**
 *
 * @author O
 */
public class StartupUI implements ActionListener, ItemListener, ListSelectionListener {

    private Game game;
    private JFrame frame;
    private JButton startGame;
    private JButton startGame2;
    private JButton aiUnitSelect;
    private JButton addUnit;
    private JButton removeUnit;
    private JToggleButton player1typeToggle;
    private JToggleButton player2typeToggle;
    private JComboBox player1AItype;
    private JComboBox player2AItype;
    private JComboBox gameTypeList;
    private JComboBox mapList;
    private JComboBox pickPlayer;
    private JLabel gameType;
    private JLabel map;
    private JPanel unitSelector;
    private JList units;
    private JTextArea selectedUnitDisplay;
    private int selectedUnit;
    private int currentlySelectedPlayer;
    private int[] player1UnitCounter;
    private int[] player2UnitCounter;
    private String[] unitNames;

    public void spawn(Game game) {
        this.game = game;
        game.startup3();
        frame = new JFrame("Startup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setLocation(200, 50);
        createComponents(frame.getContentPane(), game);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container container, Game game) {
        container.setLayout(new GridLayout(3, 1));
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();

        top.setLayout(new GridLayout(2, 2));

        String aiTypes[] = {"Aggressive", "Defensive", "Neutral", "Weird"};

        player1typeToggle = new JToggleButton("Toggle Player1 AI");
        player2typeToggle = new JToggleButton("Toggle Player2 AI");
        player1AItype = new JComboBox(aiTypes);
        player2AItype = new JComboBox(aiTypes);

        player1typeToggle.addItemListener(this);
        player1AItype.setSelectedIndex(0);
        player1AItype.addActionListener(this);
        player1AItype.setEnabled(false);

        player2typeToggle.addItemListener(this);
        player2AItype.setSelectedIndex(0);
        player2AItype.addActionListener(this);
        player2AItype.setEnabled(false);

        middle.setLayout(new GridLayout(2, 2));

        String gameTypes[] = {"Kill everything", "Kill leader (not implemented)"};
        String maps[] = {"Small and smooth", "Small and rough",
            "Medium and smooth", "Medium and rough",
            "Large and smooth", "Large and rough",
            "Huegermousser"};

        gameType = new JLabel("Select game type");
        map = new JLabel("Select map");
        gameTypeList = new JComboBox(gameTypes);
        mapList = new JComboBox(maps);

        gameTypeList.setSelectedIndex(0);
        gameTypeList.addActionListener(this);
        mapList.setSelectedIndex(0);
        mapList.addActionListener(this);

        container.add(top);
        container.add(middle);
        container.add(bottom);
        top.add(player1typeToggle);
        top.add(player1AItype);
        top.add(player2typeToggle);
        top.add(player2AItype);
        middle.add(gameType);
        middle.add(gameTypeList);
        middle.add(map);
        middle.add(mapList);
        bottom.add(unitSelectorBuilder());
        startGame = new JButton("Start game");
//        bottom.add(startGame2);
        startGame.setEnabled(false);
//        startGame = new JButton("Human vs AI");
        startGame.addActionListener(this);
//        startGame.setBackground(Color.red);
//        startGame.setForeground(Color.yellow);
//        startGame2 = new JButton("AI vs AI");
//        startGame2.addActionListener(this);
//        startGame2.setBackground(Color.white);
//        startGame2.setForeground(Color.blue);
        bottom.add(startGame);
//        bottom.add(startGame2);
    }

    private JPanel unitSelectorBuilder() {
        unitSelector = new JPanel(new GridLayout(3, 2));
        String players[] = {"Player 1", "Player 2"};
        pickPlayer = new JComboBox(players);
        aiUnitSelect = new JButton("AI unit pick - not implemented");
        aiUnitSelect.addActionListener(this);
        aiUnitSelect.setEnabled(false);

        pickPlayer.addActionListener(this);

        unitNames = new String[]{"Sharpshooter", "Scout", "Artillery"};

        units = new JList(unitNames);
        units.setVisibleRowCount(5);
        units.setFixedCellHeight(20);
        units.setFixedCellWidth(100);
        units.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        units.addListSelectionListener(this);
        addUnit = new JButton("Add unit");
        addUnit.addActionListener(this);
        removeUnit = new JButton("Remove unit");
        removeUnit.addActionListener(this);
        selectedUnitDisplay = new JTextArea();
        selectedUnitDisplay.setEditable(false);

        player1UnitCounter = new int[unitNames.length];
        player2UnitCounter = new int[unitNames.length];

        unitSelector.add(pickPlayer);
        unitSelector.add(aiUnitSelect);
        unitSelector.add(units);
        unitSelector.add(addUnit);
        unitSelector.add(removeUnit);
        unitSelector.add(selectedUnitDisplay);
        return unitSelector;
    }

    private void gameCanStart() {
        if (unitCount(player1UnitCounter) <= game.getMap().size()
                && unitCount(player2UnitCounter) <= game.getMap().size()
                && unitCount(player1UnitCounter) > 0
                && unitCount(player2UnitCounter) > 0) {
            startGame.setEnabled(true);
        } else {
            startGame.setEnabled(false);
        }
    }

    private Unit gimmeUnit(int j, Player player) {
        if (j == 0) {
            return new Unit(6, 2, 3, 3, 2, "Sharpshooter", player);
        } else if (j == 0) {
            return new Unit(9, 4, 0, 2, 1, "Scout", player);
        } else {
            return new Unit(4, 0, 0, 5, 2, "Artillery", player);
        }
    }

    private int unitCount(int[] unitCounter) {
        int count = 0;
        for (int i = 0; i < unitCounter.length; i++) {
            count = count + unitCounter[i];
        }
        return count;
    }

    private void setGameType(int selectedIndex) {
        if (selectedIndex == 0) {
            game.setKillLeader(false);
        } else if (selectedIndex == 1) {
            game.setKillLeader(true);
        }
    }

    private int[] getCorrectUnitCounter() {
        if (currentlySelectedPlayer == 0) {
            return player1UnitCounter;
        } else {
            return player2UnitCounter;
        }
    }

    private String updateDisplay() {
        gameCanStart();
        int[] unitCounter = getCorrectUnitCounter();
        String list = "";
        for (int i = 0; i < unitCounter.length; i++) {
            if (unitCounter[i] == 1) {
                list = list + "1 " + unitNames[i] + "\n";
            } else if (unitCounter[i] > 1) {
                list = list + unitCounter[i] + " " + unitNames[i] + "s\n";
            }
        }
        return list;
    }

    private void setPlayerAI(int selectedIndex, int i) {
        if (selectedIndex == -1) {
            game.getPlayers().get(i).setAI(null);
        } else if (selectedIndex == 0) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 20, 10));
        } else if (selectedIndex == 1) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 10, 20));
        } else if (selectedIndex == 2) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 15, 15));
        } else if (selectedIndex == 3) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 100, 10, 10));
        }
    }

    private void setMap(int selectedIndex) {
        if (selectedIndex == 0) {
            game.setMap(new BattleMap(5, 2));
        } else if (selectedIndex == 1) {
            game.setMap(new BattleMap(5, 4));
        } else if (selectedIndex == 2) {
            game.setMap(new BattleMap(10, 2));
        } else if (selectedIndex == 3) {
            game.setMap(new BattleMap(10, 4));
        } else if (selectedIndex == 4) {
            game.setMap(new BattleMap(20, 2));
        } else if (selectedIndex == 5) {
            game.setMap(new BattleMap(20, 4));
        } else if (selectedIndex == 6) {
            game.setMap(new BattleMap(100, 4));
        }
        gameCanStart();
    }

    private void addUnitsToPlayers() {
        currentlySelectedPlayer = 0;
        for (Player player : game.getPlayers()) {
            int[] unitCounter = getCorrectUnitCounter();
            for (int i = 0; i < unitCounter.length; i++) {
                for (int j = 0; j < unitCounter[j]; j++) {
                    player.getUnits().add(gimmeUnit(j, player));
                }
            }
            currentlySelectedPlayer = 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == startGame) {
            addUnitsToPlayers();
            frame.dispose();
            game.setCommand(new GameCommand(game));
//            game.startup();
            game.runUI();
        } else if (ae.getSource() == startGame2) {
            frame.dispose();
            game.setCommand(new GameCommand(game));
            game.startup2();
            game.runUI();
        } else if (ae.getSource() == addUnit) {
            int[] unitCounter = getCorrectUnitCounter();
            if (unitCount(unitCounter) < game.getMap().size()) {
                unitCounter[selectedUnit]++;
                selectedUnitDisplay.setText(updateDisplay());
            }
        } else if (ae.getSource() == removeUnit) {
            int[] unitCounter = getCorrectUnitCounter();
            if (unitCounter[selectedUnit] > 0) {
                unitCounter[selectedUnit]--;
                selectedUnitDisplay.setText(updateDisplay());
            }
        } else if (ae.getSource() == player1AItype) {
            setPlayerAI(player1AItype.getSelectedIndex(), 0);
        } else if (ae.getSource() == player2AItype) {
            setPlayerAI(player2AItype.getSelectedIndex(), 1);
        } else if (ae.getSource() == gameTypeList) {
            setGameType(gameTypeList.getSelectedIndex());
        } else if (ae.getSource() == mapList) {
            setMap(mapList.getSelectedIndex());
        } else if (ae.getSource() == pickPlayer) {
            currentlySelectedPlayer = pickPlayer.getSelectedIndex();
            selectedUnitDisplay.setText(updateDisplay());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == player1typeToggle) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                player1AItype.setEnabled(true);
            } else {
                player1AItype.setEnabled(false);
                setPlayerAI(-1, 0);
            }
        } else if (ie.getSource() == player2typeToggle) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                player2AItype.setEnabled(true);
            } else {
                player2AItype.setEnabled(false);
                setPlayerAI(-1, 1);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        int selection = units.getSelectedIndex();
        if (selection == 0) {
            selectedUnit = 0;
            addUnit.setEnabled(true);
            removeUnit.setEnabled(true);
        }
        if (selection == 1) {
            selectedUnit = 1;
            addUnit.setEnabled(true);
            removeUnit.setEnabled(true);
        }
        if (selection == -1) {
            addUnit.setEnabled(false);
            removeUnit.setEnabled(false);
        }
    }

}
