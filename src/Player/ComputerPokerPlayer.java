/**
 * File:        Table.java
 * Description: Poker Table
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */


package Player;

import Game.ComputerStrategy;
import Game.FiveDrawPokerRules;
import Game.HandRanking;
import Game.*;
import Table.Card;

import java.util.Arrays;

public class ComputerPokerPlayer extends PokerPlayer{

    private ComputerStrategy strategy;

    public ComputerPokerPlayer() {
        super();
        strategy = ComputerStrategy.NORMAL; // default strategy/personality
        money += Settings.difficulty.getChips();
    }

    public ComputerPokerPlayer(ComputerStrategy strategy) {
        this.strategy = strategy;
    }

    private int getRandUpDown() {
        double randomness = Settings.difficulty.getRandomness();
        return (int) (Math.random() * randomness - (randomness / 2));
    }

    @Override
    public double getBet(double currentBet, double kitty) {
        double max = getMaxBet();
        if(currentBet > max) {
            return 0; // match, which is do nothing (I don't think there is a reason to fold while betting no new money)
        }
        return (max - currentBet) * money / FiveDrawPokerRules.DEFAULT_CHIPS / 2; // only willing to go halfway to the max
    }

    @Override
    public int getReaction(double currentBet, double kitty, double change) {
        double max = getMaxBet();
        if(currentBet <= max) {
            return 0;
        }
        else {
            return -1;
        }
    }

    private double getMaxBet() {
        // consider the current amount of money it has, its personality, and the current hand
        int ranking = getHandRanking().getRanking();
        if(ranking < HandRanking.ONE_PAIR.getRanking()) { // if it's better than a ONE_PAIR, then it's rather unlikely that someone else will do better
            ranking = ranking / 2; // TODO make the constant a bit better
        }
        return Math.min(money - strategy.getRiskiness() * ranking + getRandUpDown(), money);
    }


    private HandRanking getHandRanking() {
        return FiveDrawPokerRules.getHandRanking(hand);
        //TODO figure out a way to make this not dependent on "FiveDrawPokerRules" make it a "Rules" or something
    }

    @Override
    public Card[] exchangeCards() {
        HandRanking handRanking = getHandRanking();
        Card[] discard;
        switch(handRanking) {
            // The current hand is good and requires all 5 Cards
            case ROYAL_FLUSH:
            case STRAIGHT_FLUSH:
            case FULL_HOUSE:
            case FLUSH:
            case STRAIGHT:
                discard = new Card[0]; // exchange nothing
                break;
            case FOUR_OF_A_KIND:
                int kicker = FiveDrawPokerRules.groupValue(hand, 4, 1);
                discard = FiveDrawPokerRules.getCardsValue(hand, kicker, true);
                break;
            case THREE_OF_A_KIND:
                int threeVal = FiveDrawPokerRules.groupValue(hand, 3, 1);
                discard = FiveDrawPokerRules.getCardsValue(hand, threeVal, false);
                break;
            case TWO_PAIR:
                int oneVal = FiveDrawPokerRules.groupValue(hand, 2, 1);
                discard = FiveDrawPokerRules.getCardsValue(hand, oneVal, true);
                break;
            case ONE_PAIR:
                int pairVal = FiveDrawPokerRules.groupValue(hand, 2, 1);
                discard = FiveDrawPokerRules.getCardsValue(hand, pairVal, false);
                break;
            default: // this is just HIGH_CARD
                discard = new Card[hand.length];
                System.arraycopy(hand, 0, discard, 0, hand.length); // try again by discarding all

            // TODO try to look for hands that are close to straights and flushes (like 4 CLUBS or something)
        }

        for(int i = 0; i < hand.length; i++) {
            if(hand[i] != null && Arrays.asList(discard).contains(hand[i])) {
                discard(i); // removes the Card from the hand
            }
        }

        return discard;
    }
}
