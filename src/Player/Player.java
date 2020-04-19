/**
 * File:        Player.java
 * Description: A Player which participates in the game
 * Created:     12/13/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Player;

import Game.Rules;
import Game.Settings;
import Table.Card;
import Table.Deck;

import java.util.Scanner;

public class Player {

    protected Card[] hand;
    private final int MAX_HAND_SIZE;
    protected int currentSize;
    private int playerId;
    private static int numPlayers = 1; // start counting at 1

    private String name;

    public Player() {
        this("Player " + numPlayers);
    }

    public Player(String name) {
        this(name, Settings.rules.MAX_HAND_SIZE);
    }

    private Player(String name, int handSize) {
        this.name = name;
        MAX_HAND_SIZE = handSize;
        currentSize = 0;
        hand = new Card[MAX_HAND_SIZE];
        playerId = numPlayers;
        numPlayers++;
    }

    public void setCard(Card c) {
        if(currentSize >= MAX_HAND_SIZE) {
            throw new IndexOutOfBoundsException("hand is full, cannot add cards");
        }
        hand[currentSize++] = c;
    }

    public Card discard(int i) {
        if(hand[i] == null) {
            throw new NullPointerException(String.format("Player: %s's hand does not have a card at index: %d", name, i));
        }
        Card c = hand[i];
        hand[i] = null;
        currentSize--;
        return c;
    }

    public Card[] discard() {
        Card[] temp = hand;
        hand = new Card[MAX_HAND_SIZE];
        currentSize = 0;
        return temp;
    }

    public String showHand() {
        Deck.sortCards(hand);
        String hand = String.format("%s's Hand: ", name);
        for(int i = 0; i < currentSize; i++) {
            if(this.hand[i] == null) {
                throw new NullPointerException(String.format("The %dth card in %s's hand is null", i, name));
            }
            hand += String.format("[%s]", this.hand[i].toString());
        }
        return hand;
    }

    // TODO fix this potentially dangerous thing! (it should probably return a copy)
    public Card[] getHand() {
        return hand;
    }

    public void fixCards() {
        Deck.fixCards(hand);
    }

    public void sortCards() {
        Deck.sortCards(hand);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("Player: %s \nHand: %s", getName(), showHand());
    }

    public int getPlayerId() {
        return playerId;
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        Player[] players = new Player[2];
        players[0] = new Player("A");
        players[1] = new Player("B");

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 5; j++) {
                players[i].setCard(deck.deal());
            }
            System.out.println(players[i].showHand());
        }

        Card[] discardPile = new Card[52];

        Scanner stdin = new Scanner(System.in);
        System.out.println("How many cards would you like to discard?");
        int num = stdin.nextInt();
        for(int i = 0; i < num; i++) {
            System.out.println("Which card would you like to discard?");
            int index = stdin.nextInt();
            discardPile[i] = players[0].discard(index);
            System.out.println(players[0].showHand());
        }

        for(int i = 0; i < num; i++) {
            players[0].setCard(deck.deal());
        }
        System.out.println(players[0].showHand());

        System.out.println(deck);
        deck.returnToDeck(discardPile);
        System.out.println(deck);
    }
}
