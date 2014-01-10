package tacticulous.game.graphicalui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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
 * Game startup user interface class, includes various thingyListeners.
 *
 * @author O
 */
public class StartupUI implements ActionListener, ItemListener, ListSelectionListener {

    private Game game;
    private JFrame frame;
    private JButton startGame;
    private JButton quickStart;
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
    private JComboBox ai1LoadPicker;
    private JComboBox ai2LoadPicker;
    private JLabel gameType;
    private JLabel map;
    private JPanel unitSelector;
    private JList units;
    private JTextArea selectedUnitDisplay;
    private int selectedUnit;
    private int currentlySelectedPlayer;
    private int aiLoad1;
    private int aiLoad2;
    private int[] player1UnitCounter;
    private int[] player2UnitCounter;
    private String[] unitNames;

    /**
     * Creates the startup menu window.
     *
     * @param game provides access to necessary data
     */
    public void spawn(Game game) {
        this.game = game;
        game.startup();
        frame = new JFrame("Startup");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setLocation(200, 50);
        createComponents(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates most of the game UI components.
     *
     * @param container
     */
    private void createComponents(Container container) {
        container.setLayout(new GridLayout(3, 1));
        JPanel top = new JPanel();
        JPanel middle = new JPanel();
        JPanel bottom = new JPanel();

        String aiTypes[] = {"Aggressive", "Defensive", "Neutral", "Weird"};
        String aiLoads[] = {"Simulated turns: 1", "Simulated turns: 2", "Simulated turns: 3", "Simulated turns: 4", "Simulated turns: 5"};

        player1typeToggle = new JToggleButton("Toggle Player1 AI");
        player2typeToggle = new JToggleButton("Toggle Player2 AI");
        player1AItype = new JComboBox(aiTypes);
        player2AItype = new JComboBox(aiTypes);
        ai1LoadPicker = new JComboBox(aiLoads);
        ai2LoadPicker = new JComboBox(aiLoads);

        player1typeToggle.addItemListener(this);
        player1AItype.setSelectedIndex(0);
        player1AItype.addActionListener(this);
        player1AItype.setEnabled(false);

        player2typeToggle.addItemListener(this);
        player2AItype.setSelectedIndex(0);
        player2AItype.addActionListener(this);
        player2AItype.setEnabled(false);

        ai1LoadPicker.setSelectedIndex(0);
        ai1LoadPicker.addActionListener(this);
        ai1LoadPicker.setEnabled(false);
        ai2LoadPicker.setSelectedIndex(0);
        ai2LoadPicker.addActionListener(this);
        ai2LoadPicker.setEnabled(false);

        String gameTypes[] = {"Kill everything", "Kill leader"};
        String maps[] = {"Small and smooth", "Small and rough",
            "Medium and smooth", "Medium and rough",
            "Large and smooth", "Large and rough",
            "Huegermousser"};

        gameType = new JLabel("Select game type");
        map = new JLabel("Select map");
        gameTypeList = new JComboBox(gameTypes);
        mapList = new JComboBox(maps);

        startGame = new JButton("Start game");
        quickStart = new JButton("Quick start");

        gameTypeList.setSelectedIndex(0);
        gameTypeList.addActionListener(this);
        mapList.setSelectedIndex(0);
        mapList.addActionListener(this);

        top.setLayout(new GridLayout(2, 3));
        middle.setLayout(new GridLayout(2, 2));
        bottom.setLayout(new GridLayout());

        container.add(top);
        container.add(middle);
        container.add(bottom);
        top.add(player1typeToggle);
        top.add(player1AItype);
        top.add(ai1LoadPicker);
        top.add(player2typeToggle);
        top.add(player2AItype);
        top.add(ai2LoadPicker);
        middle.add(gameType);
        middle.add(gameTypeList);
        middle.add(map);
        middle.add(mapList);

        bottom.add(unitSelectorBuilder());

        startGame.setEnabled(false);
        startGame.addActionListener(this);
        quickStart.addActionListener(this);
    }

    /**
     * Constructs the unit selector component.
     *
     * @return
     */
    private JPanel unitSelectorBuilder() {
        unitSelector = new JPanel(new GridLayout(3, 2));
        String players[] = {"Player 1", "Player 2"};
        pickPlayer = new JComboBox(players);
        aiUnitSelect = new JButton("AI unit pick");
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
        unitSelector.add(startGame);
        unitSelector.add(quickStart);
        return unitSelector;
    }

    /**
     * Checks if all players have at least 1 unit and not too many units
     * compared to map size.
     */
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

    /**
     * Creates units for the game.
     *
     * @param j determines which unit
     * @param player determines which player this unit belongs to.
     * @return some unit...
     */
    private Unit gimmeUnit(int j, Player player) {
        if (j == 0) {
            return new Unit(6, 2, 3, 3, 2, "Sharpshooter", player);
        } else if (j == 1) {
            return new Unit(9, 4, 0, 2, 1, "Scout", player);
        } else {
            return new Unit(4, 0, 0, 5, 2, "Artillery", player);
        }
    }

    /**
     * Adds commander units to players if game type is Kill Leader.
     */
    private void addLeadersIfNecessary() {
        if (game.isKillLeader()) {
            for (Player player : game.getPlayers()) {
                player.getUnits().add(new Unit(player));
            }
        }
    }

    /**
     * Checks how many units total are in an unit selection array.
     *
     * @param unitCounter an array where one element is a number of one type of
     * unit.
     *
     * @return total number of units
     */
    private int unitCount(int[] unitCounter) {
        int count = 0;
        for (int i = 0; i < unitCounter.length; i++) {
            count = count + unitCounter[i];
        }
        return count;
    }

    /**
     * Toggles game type.
     *
     * @param selectedIndex from game type dropdown list.
     */
    private void setGameType(int selectedIndex) {
        if (selectedIndex == 0) {
            game.setKillLeader(false);
        } else if (selectedIndex == 1) {
            game.setKillLeader(true);
        }
    }

    /**
     * Gives the unit counter for the player currently selected in unit selector
     * drowdown menu.
     *
     * @return correct unit array...
     */
    private int[] getCorrectUnitCounter() {
        if (currentlySelectedPlayer == 0) {
            return player1UnitCounter;
        } else {
            return player2UnitCounter;
        }
    }

    /**
     * Replaces the currently selected player's unit array with the given array.
     * Used by AI unit selection.
     *
     * @param newUnits new unit selection.
     */
    private void setCorrectUnitCounter(int[] newUnits) {
        if (currentlySelectedPlayer == 0) {
            player1UnitCounter = newUnits;
        } else {
            player2UnitCounter = newUnits;
        }
    }

    /**
     * What is shown in selected units display.
     *
     * @return
     */
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

    /**
     * Sets the player AI type, or turns it off.
     *
     * @param selectedIndex from AI type dropdown menu or AI toggle button.
     * @param i player's index whose AI is being set.
     */
    private void setPlayerAI(int selectedIndex, int i) {
        if (selectedIndex == -1) {
            game.getPlayers().get(i).setAI(null);
        } else if (selectedIndex == 0) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 10, 1));
        } else if (selectedIndex == 1) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 5, 10));
        } else if (selectedIndex == 2) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 10, 7, 7));
        } else if (selectedIndex == 3) {
            game.getPlayers().get(i).setAI(new ArtificialIntelligence(game, game.getPlayers().get(i), 1, 100, 5, 5));
        }
    }

    /**
     * Sets how many turns AI simulates.
     *
     * @param selectedIndex from AI turn simulation dropdown menu.
     * @param player which player's AI is being adjusted.
     */
    private void setAILoad(int selectedIndex, int player) {
        if (player == 0) {
            aiLoad1 = selectedIndex + 1;
        } else if (player == 1) {
            aiLoad2 = selectedIndex + 1;
        }
    }

    /**
     * Actually updates the AI turns simulated number.
     */
    private void updateAILoads() {
        if (game.getPlayers().get(0).isAi()) {
            game.getPlayers().get(0).getAi().setTurnsToSimulate(aiLoad1);
        }
        if (game.getPlayers().get(1).isAi()) {
            game.getPlayers().get(1).getAi().setTurnsToSimulate(aiLoad2);
        }
    }

    /**
     * Sets the game map size and terrain roughness.
     *
     * @param selectedIndex from map selection dropdown menu.
     */
    private void setMap(int selectedIndex) {
        if (selectedIndex == 0) {
            game.setMap(new BattleMap(8, 2));
        } else if (selectedIndex == 1) {
            game.setMap(new BattleMap(8, 4));
        } else if (selectedIndex == 2) {
            game.setMap(new BattleMap(14, 2));
        } else if (selectedIndex == 3) {
            game.setMap(new BattleMap(14, 4));
        } else if (selectedIndex == 4) {
            game.setMap(new BattleMap(20, 2));
        } else if (selectedIndex == 5) {
            game.setMap(new BattleMap(20, 4));
        } else if (selectedIndex == 6) {
            game.setMap(new BattleMap(100, 4));
        }
        gameCanStart();
    }

    /**
     * Toggles AI unit pick button on or off.
     */
    private void updateAIUnitPickButton() {
        if (game.getPlayers().get(pickPlayer.getSelectedIndex()).isAi()) {
            aiUnitSelect.setEnabled(true);
        } else {
            aiUnitSelect.setEnabled(false);
        }
    }

    /**
     * tells the currently selected player's AI to pick its own damn units.
     */
    private void haveAIPickUnits() {
        setCorrectUnitCounter(game.getPlayers().get(currentlySelectedPlayer)
                .getAi().pickUnits(3, game.getMap().size() - 1));
    }

    /**
     * Actually adds the units from unit selection arrays to players' unit
     * lists.
     */
    private void addUnitsToPlayers() {
        currentlySelectedPlayer = 0;
        for (Player player : game.getPlayers()) {
            int[] unitCounter = getCorrectUnitCounter();
            for (int i = 0; i < unitCounter.length; i++) {
                for (int j = 0; j < unitCounter[i]; j++) {
                    player.getUnits().add(gimmeUnit(i, player));
                }
            }
            currentlySelectedPlayer = 1;
        }
        addLeadersIfNecessary();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == startGame) {
            game.setCommand(new GameCommand(game));
            game.getPlayers().get(0).setGame(game);
            game.getPlayers().get(1).setGame(game);
            addUnitsToPlayers();
            game.placeUnits(game.getPlayers().get(0).getUnits());
            game.placeUnits(game.getPlayers().get(1).getUnits());
            updateAILoads();
            frame.dispose();
            game.runUI();
        } else if (ae.getSource() == quickStart) {
            game.setCommand(new GameCommand(game));
            game.getPlayers().get(0).setGame(game);
            game.getPlayers().get(1).setGame(game);
            game.getPlayers().get(0).quickStartUnits(game.getMap().size() / 3);
            game.getPlayers().get(1).quickStartUnits(game.getMap().size() / 3);
            addLeadersIfNecessary();
            game.placeUnits(game.getPlayers().get(0).getUnits());
            game.placeUnits(game.getPlayers().get(1).getUnits());
            updateAILoads();
            frame.dispose();
            game.runUI();
        } else if (ae.getSource() == addUnit) {
            int[] unitCounter = getCorrectUnitCounter();
            if (unitCount(unitCounter) < game.getMap().size() - 1) {
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
            updateAIUnitPickButton();
        } else if (ae.getSource() == ai1LoadPicker) {
            setAILoad(ai1LoadPicker.getSelectedIndex(), 0);
        } else if (ae.getSource() == ai2LoadPicker) {
            setAILoad(ai2LoadPicker.getSelectedIndex(), 1);
        } else if (ae.getSource() == aiUnitSelect) {
            haveAIPickUnits();
            selectedUnitDisplay.setText(updateDisplay());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent ie) {
        if (ie.getSource() == player1typeToggle) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                player1AItype.setEnabled(true);
                ai1LoadPicker.setEnabled(true);
                setPlayerAI(player1AItype.getSelectedIndex(), 0);
                updateAIUnitPickButton();
            } else {
                player1AItype.setEnabled(false);
                ai1LoadPicker.setEnabled(false);
                setPlayerAI(-1, 0);
                updateAIUnitPickButton();
            }
        } else if (ie.getSource() == player2typeToggle) {
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                player2AItype.setEnabled(true);
                ai2LoadPicker.setEnabled(true);
                setPlayerAI(player1AItype.getSelectedIndex(), 1);
                updateAIUnitPickButton();
            } else {
                player2AItype.setEnabled(false);
                ai2LoadPicker.setEnabled(false);
                setPlayerAI(-1, 1);
                updateAIUnitPickButton();
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
        } else if (selection == 1) {
            selectedUnit = 1;
            addUnit.setEnabled(true);
            removeUnit.setEnabled(true);
        } else if (selection == 2) {
            selectedUnit = 2;
            addUnit.setEnabled(true);
            removeUnit.setEnabled(true);
        } else if (selection == -1) {
            addUnit.setEnabled(false);
            removeUnit.setEnabled(false);
        }
    }

}
