/**
 * File:        UserInterface.java
 * Description: The interface for the User Interface
 * Created:     12/Sometime before 18/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package UI;

import Player.*;
import Table.Card;

import java.util.Scanner;

public interface UserInterface {

    int getChoice(String[] choices, String menuName);

    double getBet(PokerPlayer player, double bet, double kitty);

    int getReaction(PokerPlayer player, double bet, double kitty, double change);

    Card[] getDiscard(Player player);

    void displayKitty(double bet, double kitty);

    void display(Player player);

    void display(PokerPlayer player);

    void display(String text);

    int getNumPlayers();

    String getPlayerName(int playerNum);

    void waitForReaction();
}
