/**
 * File:        Card.java
 * Description: Card from a standard 52 card deck
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */


package Table;

import java.util.Comparator;

public class Card{
    private Suit suit;
    private int value; // 1-13

    private int id;
    public static final String[] cardNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven",
                                              "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    public static final int suitSize = (Deck.DECK_SIZE / Suit.NUM_SUITS);
    // for GUI, Image of card

    // TODO values of cards are evaluated using the Rules

    public Card(int num) {
        this.id = num;
        int suitNum = num / suitSize;
        this.value = num % suitSize;
        this.suit = Suit.values()[suitNum];
    }

    public Card(int val, Suit suit) {
        this(suitSize * suit.getNum() + val);
    }

    public String getName() {
        return cardNames[value];
    }

    public Suit getSuit() {
        return suit;
    }

    public int getID() {
        return id;
    }

    public int getValue() {
        return id % 13;
    }

    public String toString() {
        return String.format("%s of %s", getName(), suit.toString());
    }

    public static Comparator<Card> CardOrder = new Comparator<Card>() {
        @Override
        public int compare(Card o1, Card o2) {
            //System.out.println(o1.id + " " + o2.id + " return " + (o2.id - o1.id));
            //return o1.id - o2.id; // sorts by suit then by value

            // sort by value, then by suit
            if(o1.id % 13 == o2.id % 13) {
                return o1.id / 13 - o2.id / 13;
            }
            return o1.id % 13 - o2.id % 13;
        }
    };
}
