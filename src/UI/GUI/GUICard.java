package UI.GUI;

import Table.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GUICard {

    private static final double CARD_SCALE = 1.5;
    public static final int cardWidth = (int) (73 * CARD_SCALE);
    public static final int cardHeight = (int) (97 * CARD_SCALE);
    private static final int selectHeight = 50;

    private int x, y;
    private Card card;
    private boolean selected = false;

    public GUICard(Card card, int x, int y) {
        this.x = x;
        this.y = y;
        this.card = card;
    }

    public void draw(Graphics g) {
        g.drawImage(getCardImage(card), x, y - (selected ? selectHeight : 0), GUICard.cardWidth, GUICard.cardHeight, null);
    }

    private Image getCardImage(Card card) {
        String filePath = "src/CardImg/";
        if(card == null) {
            filePath += "back1.GIF";
            return accessImageFile(filePath);
        }

        if(card.getValue() < 10 && card.getValue() > 0) {
            // if it's an integer
            filePath += "" + (card.getValue() + 1);
        }
        else if(card.getValue() == 0) {
            // if it's an Ace
            filePath += "ace";
        }
        else {
            // it's a face card
            filePath += card.getName().toLowerCase();
        }

        filePath += card.getSuit().toString().toLowerCase();
        filePath += ".GIF";

        return accessImageFile(filePath);
    }

    private Image accessImageFile(String path) {
        BufferedImage cardImg;
        try {
            cardImg = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Unable to read Image Files, No Card Image will be displayed");
            cardImg = null;
        }
        return cardImg;
    }

    public void select() {
        selected = true;
    }

    public void deselect() {
        selected = false;
    }

    public void flip() {
        card = null;
    }
}
