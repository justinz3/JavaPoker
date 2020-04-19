/**
 * File:        GraphicsUI.java
 * Description: An (incomplete) implementation of a GUI for UserInterface
 * Created:     12/Sometime before 18/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package UI.GUI;

import Player.Player;
import Player.PokerPlayer;
import Table.Card;
import Table.Table;
import UI.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// this should implement UserInterface
public class GraphicsUI extends JFrame
                        implements ActionListener, UserInterface {

    private JTextArea textInput, textDisplay;
    private JButton submit;
    private boolean submitted;
    private String inputText;

    public GraphicsUI(String title) {
        super(title);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 1250, height = 750;
        setBounds((int) (screenSize.getWidth() / 2) - width / 2, (int) (screenSize.getHeight() / 2) - height / 2, width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupGUI();
        textDisplay.setText("This is the event log");
        textInput.setText("Type something!");

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object actor = e.getSource();
        if(submit == actor) {
            submitted = true;
            inputText = textInput.getText();
        }
    }

    private void setupGUI() {
        textInput = new JTextArea(1, 40);
        textInput.setBackground(Color.WHITE);
        JScrollPane textInputPane = new JScrollPane(textInput,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textDisplay = new JTextArea(35, 40);
        textDisplay.setEditable(false);
        textDisplay.setBackground(Color.WHITE);
        JScrollPane textDisplayPane = new JScrollPane(textDisplay,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        submit = new JButton("Enter");
        submit.addActionListener(this);

        Box display = Box.createHorizontalBox();
        Box table = Box.createHorizontalBox(); // Not sure which kind of box to do
        Box text = Box.createVerticalBox();
        Box input = Box.createVerticalBox();

        Box textInputBox = Box.createVerticalBox();
        Box submitBox = Box.createVerticalBox();
        Box textDisplayBox = Box.createVerticalBox();

        textInputBox.add(Box.createVerticalGlue());
        textInputBox.add(new JLabel("Input:"));
        textInputBox.add(textInputPane);

        submitBox.add(submit);

        input.add(Box.createVerticalGlue());
        input.add(Box.createVerticalStrut(10));
        input.add(textInputBox);
        input.add(submitBox);
        input.add(Box.createVerticalStrut(10));

        textDisplayBox.add(new JLabel("Events:")); // TODO come up with a better label...
        textDisplayBox.add(textDisplayPane);

        text.add(Box.createVerticalStrut(10));
        text.add(textDisplayBox);
        text.add(Box.createVerticalStrut(10));
        text.add(input);
        text.add(Box.createVerticalStrut(10));

        GUITable guiTable = new GUITable();
        table.add(guiTable);
        //table.add(Box.createVerticalStrut(700));
        //table.add(Box.createHorizontalStrut(700));
        System.out.println("table: " + table.getBounds());
        System.out.println("Panel: " + guiTable.getSize());

        display.add(Box.createHorizontalStrut(10));
        display.add(table);
        display.add(Box.createHorizontalStrut(10));
        display.add(text);
        display.add(Box.createHorizontalStrut(10));

        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        c.add(display);

        submitted = false;
    }

    public int getChoice(String[] choices, String menuName) {
        ImageIcon tux = null;

        try {
            tux = new ImageIcon("src/CardImg/JO.GIF");
        } catch(Exception e) {
            System.out.println("Unable to display Tux");
        }

        int choice = JOptionPane.showOptionDialog(null, menuName,
                menuName, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, tux, choices, choices[0]);
        return choice + 1;
    }

    public double getBet(PokerPlayer player, double currentBet, double kitty) {
        squigglyLine();
        displayKitty(currentBet, kitty);
        display(player);
        display("Your turn to Bet! Enter Your Decision: Fold [-1], Check [0], Raise [Any Positive #] ");
        double bet = -2;

        while(true) {
            bet = getDouble();
            if(bet < -1 || bet > player.getMoney()) {
                display("Invalid Bet, Please Try Again");
                continue;
            }
            break;
        }
        squigglyLine();
        return bet;
    }

    public int getReaction(PokerPlayer player, double currentBet, double kitty, double change) {
        squigglyLine();
        displayKitty(currentBet, kitty);
        display(player);
        display(String.format("Uh oh, someone raised by $%.2f\n" +
                "Do you want to fold [-1] or match [0]? ", change));

        int bet = -2;
        while(true) {
            bet = getInt();
            if(bet != -1 && bet != 0) {
                display("Invalid Response, Please Try Again");
                continue;
            }
            break;
        }
        squigglyLine();
        return bet;
    }

    public Card[] getDiscard(Player player) {
        squigglyLine();
        display(player);

        Card[] discarded;

        int discardAmt=0;
        do{
            try{

                display(player.getName() + ": How many cards would you like to trade? (0-5)");
                discardAmt = getInt();
            }
            catch(Exception e)
            {
                display("Invalid response, try again");
                discardAmt=6;
            }
        }while(discardAmt>5||discardAmt<0); //Stay in this loop if invalid discardAmt entered

        discarded = new Card[discardAmt];
        try{
            display(player.getName() + ": Enter card indices (0-4), one at a time (Press Enter Between Selections`)");
            int cardsToDiscardInts[] = new int[discardAmt];
            int j=0;

            while(j<discardAmt){
                cardsToDiscardInts[j] = getInt();
                j++;
            }
            for(int i=0; i<discardAmt; i++)
            {
                discarded[i] = player.discard(cardsToDiscardInts[i]);
            }

            player.fixCards(); //Remove the nulls
        }
        catch(Exception e)
        {
            display("An exception occurred, make sure you are entering numbers of cards to delete");
            return null;
        }
        squigglyLine();
        return discarded;
    }

    public void displayKitty(double bet, double kitty) {
        dashLine();
        display(Table.formatKitty(bet, kitty));
        dashLine();
    }

    public void display(Player player) {
        display(player.toString());
    }

    public void display(PokerPlayer player) {
        dashLine();
        display(player.toString());
        dashLine();
    }

    public void display(String text) {
        String log = textDisplay.getText();
        log += "\n" + text;
        // display at most 75 lines because it'll get to be to much eventually
        log = trimToLines(log, 75);
        textDisplay.setText(log);
    }

    private String trimToLines(String text, int numLines) {
        String[] lines = text.split("\n");
        String result = "";
        for(int i = Math.max(0, lines.length - numLines); i < lines.length; i++) {
            result += lines[i] + "\n";
        }
        return result;
    }

    public int getNumPlayers() {
        squigglyLine();
        display("Please Enter The Number of Players you want(>=2, <=9): ");
        int numPlayers = -2;
        while(true) {
            numPlayers = getInt();
            if(numPlayers < 2 || numPlayers > 9) {
                display("Invalid Number of Players, Please Try Again");
                continue;
            }
            break;
        }
        squigglyLine();
        return numPlayers;
    }

    public String getPlayerName(int playerNum) {
        display(String.format("Please Enter a Name for Player #%d: ", playerNum));
        String name = getString();
        squigglyLine();
        return name;
    }

    public void waitForReaction() {
        display("(Press Enter to Continue)");
        getString();
    }

    private int getInt() {
        int ans = 0;
        do {
            waitForSubmission();
            submitted = false;
            try {
                ans = Integer.parseInt(inputText);
                break;
            } catch (Exception e) {
                display("Invalid Input, Please Try Again");
            }
        } while(true);
        return ans;
    }

    private double getDouble() {
        double ans = 0;
        do {
            waitForSubmission();
            submitted = false;
            try {
                ans = Double.parseDouble(inputText);
                break;
            } catch (Exception e) {
                display("Invalid Input, Please Try Again");
            }
        } while(true);
        return ans;
    }

    private String getString() {
        waitForSubmission();
        submitted = false;
        return inputText;
    }

    private void waitForSubmission() {
        while(!submitted) {
            // wait
            wait(500);
        }
    }

    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt(); // this is what StackOverflow said to do.
        }
    }

    private void dashLine() {
        display("---------------------------------");
    }

    private void squigglyLine() {
        display("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void equalsLine() {
        display("=================================");
    }

    // Tester Code
    public static void main(String[] args) {
        GraphicsUI gui = new GraphicsUI("Poker");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 1000, height = 750;
        //gui.setBounds((int) (screenSize.getWidth() / 2) - width / 2, (int) (screenSize.getHeight() / 2) - height / 2, width, height);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        //String[] choices = {"yes", "no", "maybe", "so"};
        //gui.getChoice(choices, "This is kind of a menu");

        ImageIcon card = new ImageIcon("src/CardImg/2clubs.GIF");
    }
}
