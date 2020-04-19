/**
 * File:        Deck.java
 * Description: Deck of Poker Cards
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */


package Table;
import java.util.Arrays;
import java.util.Comparator;

public class Deck {
    public static final int DECK_SIZE = 52;
    private Card[] cards; // implemented like a stack
    private int currentSize;
    private int startIndex = 0, endIndex = DECK_SIZE - 1;

    public Deck() {
        currentSize = DECK_SIZE;
        cards = new Card[DECK_SIZE];
        for(int i = 0; i < DECK_SIZE; i++) {
            cards[i] = new Card(i);
        }
    }

    // For incrementing and decrementing the indices
    private int moveBack(int index) {
        return (index - 1 + DECK_SIZE) % DECK_SIZE;
    }
    private int moveForward(int index) {
        return (index + 1) % DECK_SIZE;
    }

    public void sortDeck() {
        Deck.sortCards(cards);
    }

    public static void sortCards(Card[] hand, int fromIndex, int toIndex) {
        Arrays.sort(hand, fromIndex, toIndex, Card.CardOrder);
    }

    public static void sortCards(Card[] hand) {
        int numNull = fixCards(hand);
        sortCards(hand, 0, hand.length - numNull);
    }

    public static int fixCards(Card[] hand) {
        int index = 0;
        for(int i = 0; i < hand.length; i++) {
            if(hand[i] != null) {
                hand[index++] = hand[i];
            }
        }
        int numNull = hand.length - index;
        for(int i = index; i < hand.length; i++) {
            hand[i] = null;
        }
        return numNull;
    }

    // Returns top Card reference (last element in array), reduces size, sets last element to null
    public Card deal() {
        if(currentSize == 0)
            return null;
        currentSize--;
        Card card = cards[endIndex];
        cards[endIndex] = null;
        endIndex = moveBack(endIndex);
        return card;
    }

    // adds this Card to front of deck, increment size
    public boolean returnToDeck(Card c) {
        if(c == null)
            throw new NullPointerException("Trying to return a null card");
        if(currentSize == DECK_SIZE) {
            return false;
        }
        currentSize++;
        startIndex = moveBack(startIndex);
        cards[startIndex] = c;
        return true;
    }

    // adds array of cards into deck, increase size
    public boolean returnToDeck(Card[] c) {
        for(int i = 0; i < c.length; i++) {
            if(c[i] == null)
                continue;
            if(!returnToDeck(c[i])) {
                throw new NullPointerException("Return to Deck Failed");
                //return false;
            }
        }
        return true;
    }

    // shuffle cards array, assumes the deck is full
    public void shuffle() {
        for(int i = startIndex, cnt = 0; cnt < currentSize; i = moveForward(i), cnt++) {
            int moves = (int) (Math.random() * currentSize);
            int index = startIndex;
            for(int j = 0; j < moves; j++, index = moveForward(index));
            // swap
            Card temp = cards[i];
            cards[i] = cards[index];
            cards[index] = temp;
        }
    }

    // print out current size and all Cards in deck
    public String toString() {
        String result = String.format("Current Size: %d\nstartIndex: %d\nendIndex: %d\n", currentSize, startIndex, endIndex);
        for(int i = startIndex, cnt = 0; cnt < currentSize; i = moveForward(i), cnt++) {
            if(cards[i] == null) {
                //throw new NullPointerException(String.format("Card %d is null", i));
                continue;
            }
            result += cards[i].toString() + "\n";
        }
        result += "\n";
        return result;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    // tester
    public static void main(String[] args) {
        Deck deck = new Deck();
        System.out.println(deck);
        System.out.println("---------------------------------");
        deck.shuffle();
        System.out.println(deck);
        Card[][] hands = new Card[2][7];

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 7; j++) {
                hands[i][j] = deck.deal();
            }
        }

        System.out.println("---------------------------------");
        deck.sortDeck();
        System.out.println(deck);
        System.out.println("Hands: ");
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 7; j++) {
                System.out.print(hands[i][j] + " ");
            }
            System.out.println();
        }

        Deck.sortCards(hands[0]);
        Deck.sortCards(hands[1]);
        System.out.println("Sorted: ");
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 7; j++) {
                System.out.print(hands[i][j] + " ");
            }
            System.out.println();
        }


        System.out.println("---------------------------------");
        for(int i = 0; i < 7; i++) {
            System.out.println("Returning: " + hands[0][i]);
            deck.returnToDeck(hands[0][i]);
            System.out.println(deck);
        }


        System.out.println("---------------------------------");
        deck.returnToDeck(hands[1]);
        System.out.println(deck);
        deck.sortDeck();
        System.out.println(deck);
    }

    /**An optional method that provides more precise information about the cards left in the
     *deck.  Outputs count for each suit.  Returns true if 13 cards of each suit exists and the total
     *sum of cards is 416 (but if you're me and valued the cards differently, it's 312)*/
    public boolean checkDeck()
    {
        int spadesCnt=0, diamondsCnt=0, clubsCnt=0, heartsCnt=0;
        int totalValue=0;
        Card[] spades = new Card[20];
        Card[] diamonds = new Card[20];
        Card[] hearts = new Card[20];
        Card[] clubs = new Card[20];
        System.out.println("*******Checking Deck **********/");
        for(int i=0; i<DECK_SIZE; i++)
        {
            if(cards[i]!=null && cards[i].getSuit().toString().equals("Clubs"))
            {
                clubs[clubsCnt]=cards[i];
                clubsCnt++;
            }
            else if(cards[i]!=null && cards[i].getSuit().toString().equals("Diamonds"))
            {
                diamonds[diamondsCnt]=cards[i];
                diamondsCnt++;
            }
            else if(cards[i]!=null && cards[i].getSuit().toString().equals("Hearts"))
            {
                hearts[heartsCnt]=cards[i];
                heartsCnt++;
            }
            else if(cards[i]!=null && cards[i].getSuit().toString().equals("Spades"))
            {
                spades[spadesCnt]=cards[i];
                spadesCnt++;
            }
            if(cards[i]!=null)
                totalValue+=cards[i].getValue();
        }
        for(int i=0; i<clubsCnt; i++)
            if(clubs[i]!=null)
                System.out.println(clubs[i]);
        System.out.println();
        for(int i=0; i<diamondsCnt; i++)
            if(diamonds[i]!=null)
                System.out.println(diamonds[i]);
        System.out.println();
        for(int i=0; i<spadesCnt; i++)
            if(spades[i]!=null)
                System.out.println(spades[i]);
        System.out.println();
        for(int i=0; i<heartsCnt; i++)
            if(hearts[i]!=null)
                System.out.println(hearts[i]);


        System.out.println("Clubs: " + clubsCnt + " Spades: " + spadesCnt + " Diamonds: " + diamondsCnt + " Hearts: " + heartsCnt);
        System.out.println("Total: " + totalValue);

        if(clubsCnt==13 && spadesCnt == 13 && diamondsCnt==13 && heartsCnt==13 && totalValue==312)
            return true;
        return false;

    }
}
