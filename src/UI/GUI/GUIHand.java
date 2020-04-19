package UI.GUI;

import Game.Settings;
import Player.Player;
import Table.Card;

import java.awt.*;

public class GUIHand {

    private Player player;
    private boolean isHuman;
    private int x, y;
    private GUICard[] cards;
    private final double compHandSize = 0.3;

    public GUIHand(Player player, int numPlayers, boolean isHuman) {
        this.player = player;
        this.isHuman = isHuman;
        // TODO figure out where to place the hand and init the GUICards

        // place the hands in clockwise order of player
        setPosition(player.getPlayerId(), numPlayers);

        cards = new GUICard[player.getHand().length];
        for(int i = 0; i < player.getHand().length; i++) {
            cards[i] = (new GUICard((isHuman ? player.getHand()[i] : null), x + i * (int) (GUICard.cardWidth * (isHuman ? 1 : compHandSize)), y));
        }
    }

    // Precondition: playerNum starts at 1
    private void setPosition(int playerNum, int numPlayers) {
        double anglePerPlayer = 2 * Math.PI / numPlayers;
        double angle = (playerNum - 1) * anglePerPlayer;
        // positions should start at -PI/2 so...
        angle += Math.PI / 2;
        System.out.println("Angle: " + (angle * 180 / Math.PI));

        x = (int) (Math.cos(angle) * GUITable.radius) + GUITable.centerX;
        y = (int) (Math.sin(angle) * GUITable.radius) + GUITable.centerY;
        // x and y are currently the center of the hand's position
        System.out.println("Center of Hand: " + x + " " + y);

        x -= Settings.rules.MAX_HAND_SIZE * (int) (GUICard.cardWidth / 2 * (isHuman ? 1 : compHandSize));
        y -= GUICard.cardHeight / 2;
        System.out.println("Hand top-left corner: " + x + " " + y);
    }

    public void select(int i) {
        cards[i].select();
    }

    public void draw(Graphics g) {
        for(int i = 0; i < player.getHand().length; i++) {
            cards[i].draw(g);
        }
    }
}
