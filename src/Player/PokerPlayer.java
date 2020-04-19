/**
 * File:        PokerPlayer.java
 * Description: A (Human) Player which participates in a Poker
 * Created:     12/18/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Player;

import Game.FiveDrawPokerRules;
import Game.Settings;
import Table.Card;
import Table.Deck;
import UI.UserInterface;

public class PokerPlayer extends Player{

    protected double money = FiveDrawPokerRules.DEFAULT_CHIPS;
    private static UserInterface ui = Settings.ui;

    public PokerPlayer() {
        super();
    }

    public PokerPlayer(String name) {
        super(name);
    }

    public double getBet(double currentBet, double kitty) {
        return ui.getBet(this, currentBet, kitty);
    }

    public int getReaction(double currentBet, double kitty, double change) {
        return ui.getReaction(this, currentBet, kitty, change);
    }

    public Card[] exchangeCards() {
        Card[] discarded = ui.getDiscard(this);
        Deck.fixCards(hand);
        return discarded;
    }

    @Override
    public String showHand() {
        String result = super.showHand();
        result += String.format("\nHand Ranking: %s", FiveDrawPokerRules.getHandRanking(hand));
        return result;
    }

    public void giveChips(double chips) {
        this.money += chips;
    }

    public void takeChips(double chips) {
        this.money -= chips;
        ui.display(String.format("$%.2f of %s's money has been taken\n", chips, getName()));
    }

    public double getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return String.format("%s\nMoney: %.2f", super.toString(), getMoney());
    }
}
