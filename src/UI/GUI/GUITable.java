package UI.GUI;

import Game.Settings;
import Player.Player;
import Table.*;

import javax.swing.*;
import java.awt.*;

public class GUITable extends JPanel {

    private Player[] players;
    private Table table;
    public static int width = 700, height = 700;
    public static int centerX = width / 2, centerY = height / 2;
    public final static int radius = 250;

    // TODO: Dan said to look into Card Layout?

    public GUITable() {
        super();
        setSize(new Dimension(width, height));
        setBackground(Color.BLUE);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //setBounds((int) (screenSize.getWidth() / 2) - width / 2, (int) (screenSize.getHeight() / 2) - height / 2, width, height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //width = super.getWidth();
        //height = super.getHeight();
        super.setSize(new Dimension(width, height));
        centerX = width / 2;
        centerY = height / 2;
        System.out.println("Dimensions " + super.getWidth() + " " + super.getHeight());

        //(new GUICard(new Card(0, Suit.HEARTS), 100, 100)).draw(g);
        Player player = new Player("NAME");
        Deck deck = new Deck();
        deck.shuffle();
        deck.returnToDeck(player.discard());
        for(int i = 0; i < Settings.rules.MAX_HAND_SIZE; i++) {
            player.setCard(deck.deal());
        }
        GUIHand hand = new GUIHand(player, 4, false);
        hand.select(0);
        hand.draw(g);
        deck.returnToDeck(player.discard());
    }

    public int getNumPlayers() {
        return players.length;
    }

    public static void main(String[] args) {
        GUITable table = new GUITable();
        JFrame frame = new JFrame();
        frame.setTitle("A Test");
        frame.setSize(1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(table);
        frame.setVisible(true);
    }
}
