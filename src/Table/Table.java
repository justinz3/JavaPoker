/**
 * File:        Table.java
 * Description: Poker Table
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Table;

public class Table {

    private Deck deck;
    private Kitty kitty;

    public Table() {
        deck = new Deck();
        kitty = new Kitty();
    }

    public void returnToDeck(Card card) {
        deck.returnToDeck(card);
    }

    public void returnToDeck(Card[] cards) {
        deck.returnToDeck(cards);
    }

    public Card deal() {
        return deck.deal();
    }

    public void shuffleDeck() {
        deck.shuffle();
    }

    public int getDeckSize() {
        return deck.getCurrentSize();
    }

    public void update(double amt) {
        kitty.update(amt);
    }

    public double payout() {
        return kitty.payout();
    }

    public double getKitty() {
        return kitty.getTotal();
    }

    public static String formatKitty(double currentBet, double kitty) {
        return String.format("Current Kitty: %.2f\nCurrent Bet: %.2f\n", kitty, currentBet);
    }

    public String toString() {
        return String.format("Deck:\n%s\n\nKitty:\n%s", deck.toString(), kitty.toString());
    }
}
