/**
 * File:        TerminalUI.java
 * Description: An Implementation of UserInterface which uses a terminal
 * Created:     12/Sometime before 18/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package UI;

import Player.Player;

import java.io.InputStream;
import java.util.Scanner;
import Player.PokerPlayer;
import Table.Card;
import Table.Table;

public class TerminalUI implements UserInterface {

    private Scanner stdin;

    public TerminalUI(InputStream stream) {
        stdin = new Scanner(stream);
    }

    public int getChoice(String[] choices, String menuName) {
        equalsLine();
        String menu = formatChoices(choices);
        System.out.printf("%s:\n", menuName);
        System.out.println(menu);
        int choice = -1;
        while(choice < 1 || choice > choices.length) {

            System.out.print(String.format("Please enter your choice from 1 - %d: ", choices.length));
            choice = stdin.nextInt();
            stdin.nextLine();
        }
        equalsLine();
        return choice;
    }

    private String formatChoices(String[] choices) {
        String result = "";
        for(int i = 0; i < choices.length; i++) {
            result += String.format("%d) %s\n", i + 1, choices[i]);
        }
        return result;
    }

    // assumes that the displayKitty() function in Poker has been called
    public double getBet(PokerPlayer player, double currentBet, double kitty) {
        squigglyLine();
        displayKitty(currentBet, kitty);
        display(player);
        System.out.print("Your turn to Bet! Enter Your Decision: Fold [-1], Check [0], Raise [Any Positive #] ");
        double bet = -2;
        while(bet < -1 || bet > player.getMoney()) {
            bet = stdin.nextDouble();
            stdin.nextLine();
        }
        squigglyLine();
        return bet;
    }

    public void displayKitty(double bet, double kitty) {
        dashLine();
        display(Table.formatKitty(bet, kitty));
        dashLine();
    }

    public int getReaction(PokerPlayer player, double currentBet, double kitty, double change) {
        squigglyLine();
        displayKitty(currentBet, kitty);
        display(player);
        System.out.print(String.format("Uh oh, someone raised by $%.2f\n" +
                "Do you want to fold [-1] or match [0]? ", change));
        int bet = -2;
        while(bet != -1 && bet != 0) {
            bet = stdin.nextInt();
            stdin.nextLine();
        }
        squigglyLine();
        return bet;
    }

    // Copied and modified from Mrs. DiBenedetto's PlayerTester.java
    public Card[] getDiscard(Player player) {
        squigglyLine();
        display(player);

        Card[] discarded;

        int discardAmt=0;
        do{
            try{

                System.out.println(player.getName() + ": How many cards would you like to trade? (0-5)");
                discardAmt = stdin.nextInt();
                stdin.nextLine(); //to clean up new line character not picked up by nextInt()
            }
            catch(Exception e)
            {
                System.out.println("Invalid response, try again");
                stdin.nextLine();
                discardAmt=6;
            }
        }while(discardAmt>5||discardAmt<0); //Stay in this loop if invalid discardAmt entered

        discarded = new Card[discardAmt];
        try{
            System.out.println(player.getName() + ": Enter card indices (0-4), separated by space");
            int cardsToDiscardInts[] = new int[discardAmt];
            int j=0;

            while(j<discardAmt){
                cardsToDiscardInts[j] = stdin.nextInt();
                j++;
            }
            stdin.nextLine();
            for(int i=0; i<discardAmt; i++)
            {
                discarded[i] = player.discard(cardsToDiscardInts[i]);
            }

            player.fixCards(); //Remove the nulls
        }
        catch(Exception e)
        {
            System.out.println("An exception occurred, make sure you are entering numbers of cards to delete");
            return null;
        }
        squigglyLine();
        return discarded;
    }

    public void display(Player player) {
        dashLine();
        System.out.println(player.toString());
        dashLine();
    }

    public void display(PokerPlayer player) {
        dashLine();
        System.out.println(player.toString());
        dashLine();
    }

    public void display(String text) {
        System.out.println(text);
    }

    public int getNumPlayers() {
        squigglyLine();
        System.out.print("Please Enter The Number of Players you want(>=2, <=9): ");
        int numPlayers = -2;
        while(numPlayers < 2 || numPlayers > 9) {
            numPlayers = stdin.nextInt();
            stdin.nextLine();
        }
        squigglyLine();
        return numPlayers;
    }

    // TODO implement a generalized input function (one that asks for an int, a string, etc.)

    public String getPlayerName(int playerNum) {
        squigglyLine();
        System.out.print(String.format("Please Enter a Name for Player #%d: ", playerNum));
        String name = stdin.nextLine();
        squigglyLine();
        return name;
    }

    public void waitForReaction() {
        System.out.println("(Press Enter to Continue)");
        stdin.nextLine();
        dashLine();
    }

    private void dashLine() {
        System.out.println("--------------------------------------------------------------------------------------");
    }

    private void squigglyLine() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void equalsLine() {
        System.out.println("======================================================================================");
    }
}
