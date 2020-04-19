/**
 * File:        FiveDrawPokerRules.java
 * Description: 5 Card Draw Poker Rules
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Game;

import Table.Card;
import Table.Deck;
import Table.Suit;

public class FiveDrawPokerRules extends Rules{
    public static int ACE_VALUE = 13; // or 0
    public static int DEFAULT_CHIPS = 100;
    public final int MAX_HAND_SIZE = 5;

    public FiveDrawPokerRules() {}

    @Override
    public int compare(Card[] o1, Card[] o2) {
        HandRanking a = getHandRanking(o1);
        HandRanking b = getHandRanking(o2);
        if(a == b) {
            return compareSameRanking(o1, o2, a);
        }
        return b.compareTo(a); // the smallest rankings are the highest, so we do b - a
    }

    private static int compareSameRanking(Card[] a, Card[] b, HandRanking ranking) {
        int higha, highb;
        switch(ranking) {
            case ROYAL_FLUSH:
                return 0; // they are equally good
            case STRAIGHT_FLUSH:
                return highCard(a) - highCard(b);
            case FOUR_OF_A_KIND:
                higha = groupValue(a, 4, 1);
                highb = groupValue(b, 4, 1);
                return higha - highb;
            case FULL_HOUSE:
                higha = groupValue(a, 3, 1);
                highb = groupValue(b, 3, 1);
                // Aces high
                higha = applyAceVal(higha);
                highb = applyAceVal(highb);
                // it's not possible for higha == highb because only 4 of each value in a deck
                if(higha == highb) {
                    higha = groupValue(a, 2, 1);
                    highb = groupValue(b, 2, 1);
                    // Aces high
                    higha = applyAceVal(higha);
                    highb = applyAceVal(highb);
                    return higha - highb;
                }
                return higha - highb;
            case FLUSH:
                return highCard(a) - highCard(b);
            case STRAIGHT:
                return highCard(a) - highCard(b);
            case THREE_OF_A_KIND:
                higha = groupValue(a, 3, 1);
                highb = groupValue(b, 3, 1);

                // Aces high
                higha = applyAceVal(higha);
                highb = applyAceVal(highb);

                if(higha == highb) {
                    int groupNum = 2;
                    do { // compares individual cards from high to low
                        higha = groupValue(a, 1, groupNum);
                        highb = groupValue(b, 1, groupNum);
                        if(higha == highb) {
                            groupNum--;
                            if(groupNum == 0)
                                break;
                        }
                        return higha - highb;
                    } while(higha == highb);
                    return 0;
                }
                return higha - highb;
            case TWO_PAIR:
                // Aces high
                higha = groupValue(a, 2, 1);
                highb = groupValue(b, 2, 1);
                if(higha == 0 || highb == 0) { // one or both have aces
                    higha = applyAceVal(higha);
                    highb = applyAceVal(highb);
                    if(higha != highb) {
                        return higha - highb;
                    }
                }
                // second pair should be higher
                higha = groupValue(a, 2, 2);
                highb = groupValue(b, 2, 2);
                if (higha == highb) { // compare first pair
                    higha = groupValue(a, 2, 1);
                    highb = groupValue(b, 2, 1);
                    if (higha == highb) { // compare individual cards
                        higha = groupValue(a, 1, 1);
                        highb = groupValue(b, 1, 1);
                    }
                }

                return higha - highb;
            case ONE_PAIR:
                higha = groupValue(a, 2, 1);
                highb = groupValue(b, 2, 1);

                // Aces high
                higha = applyAceVal(higha);
                highb = applyAceVal(highb);

                if(higha == highb) {
                    int groupNum = 3;
                    do { // compares individual cards from high to low
                        higha = groupValue(a, 1, groupNum);
                        highb = groupValue(b, 1, groupNum);
                        if(higha == highb) {
                            groupNum--;
                            if(groupNum == 0)
                                break;
                        }
                    } while(higha == highb);;
                }
                return higha - highb;
            default: // this is just HIGH_CARD
                return highCard(a) - highCard(b);
        }
    }

    private static int applyAceVal(int cardVal) {
        cardVal = cardVal == 0 ? ACE_VALUE : cardVal;
        return cardVal;
    }

    public static HandRanking getHandRanking(Card[] hand) {
        Deck.sortCards(hand);
        if(isRoyalFlush(hand)) {
            return HandRanking.ROYAL_FLUSH;
        }
        if(isStraightFlush(hand)) {
            return HandRanking.STRAIGHT_FLUSH;
        }
        if(isFourOfAKind(hand)) {
            return HandRanking.FOUR_OF_A_KIND;
        }
        if(isFullHouse(hand)) {
            return HandRanking.FULL_HOUSE;
        }
        if(isFlush(hand)) {
            return HandRanking.FLUSH;
        }
        if(isStraight(hand)) {
            return HandRanking.STRAIGHT;
        }
        if(isThreeOfAKind(hand)) {
            return HandRanking.THREE_OF_A_KIND;
        }
        if(isTwoPair(hand)) {
            return HandRanking.TWO_PAIR;
        }
        if(isOnePair(hand)) {
            return HandRanking.ONE_PAIR;
        }
        //if(isHighCard(hand)) {
        return HandRanking.HIGH_CARD;
        //}
    }

    // returns whether the hand is a Royal Flush, also assumes that hand is sorted
    private static boolean isRoyalFlush(Card[] hand) {
        // check all same suit, check all consecutive and all royal and ace
        return sameSuit(hand) && (isConsecutive(hand) && hand[0].getValue() == 11);
        // continuous, same suit, and starts with a Jack
    }

    // only returns whether or not it is a Straight Flush assuming it is not a Royal Flush, also assumes that hand is sorted
    private static boolean isStraightFlush(Card[] hand) {
        return sameSuit(hand) && isConsecutive(hand);
    }

    // only returns whether or not it is a Four of a Kind assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isFourOfAKind(Card[] hand) {
        return hasNumSame(hand, 4) == 1;
    }

    // only returns whether or not it is a Full House assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isFullHouse(Card[] hand) {
        return hasNumSame(hand, 3) == 1 && hasNumSame(hand, 2) == 1;
    }

    // only returns whether or not it is a Flush assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isFlush(Card[] hand) {
        return sameSuit(hand);
    }

    // only returns whether or not it is a Straight assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isStraight(Card[] hand) {
        return isConsecutive(hand);
    }

    // only returns whether or not it is a Three of a Kind assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isThreeOfAKind(Card[] hand) {
        return hasNumSame(hand, 3) == 1;
    }

    // only returns whether or not it is a Two Pair assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isTwoPair(Card[] hand) {
        return hasNumSame(hand, 2) == 2;
    }

    // only returns whether or not it is a One Pair assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isOnePair(Card[] hand) {
        return hasNumSame(hand, 2) == 1;
    }

    // only returns whether or not it is a High Card assuming it is not any of the above, also assumes that hand is sorted
    private static boolean isHighCard(Card[] hand) {
        return true;
    }

    private static boolean sameSuit(Card[] hand) {
        Suit suit = hand[0].getSuit();
        for(int i = 1; i < hand.length; i++) {
            if(hand[i].getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    private static boolean isConsecutive(Card[] hand) {
        int start = 1;
        if(hand[0].getValue() == 0 && hand[1].getValue() == 11) {
            start = 2; // skip the ace and compare it later, assuming this is a royal straight
        }
        for(int i = start; i < hand.length; i++) {
            if(hand[i].getValue() != hand[i - 1].getValue() + 1) {
                // if not consecutive or a king to ace, return false
                return false;
            }
        }
        if(start == 2) {
            return hand[hand.length - 1].getValue() == 13; // the last card has to be a king
        }
        return true;
    }

    public static int groupValue(Card[] hand, int numSame, int groupNum) {
        int count = 0;
        int prev = -1;
        int group = 0;
        for(int i = 0; i < hand.length; i++) {
            if(hand[i].getValue() != prev) {
                if(count == numSame) {
                    group++;
                }
                if(group == groupNum) {
                    return prev;
                }
                prev = hand[i].getValue();
                count = 0;
            }
            if(hand[i].getValue() == prev) {
                count++;
            }
        }
        if(count == numSame) {
            group++;
        }
        if(group == groupNum) {
            return prev;
        }
        return -1;
    }

    public static int highCard(Card[] hand) {
        // the highest card will be the last card, or the ace
        return hand[0].getValue() == 0 ? ACE_VALUE : hand[hand.length - 1].getValue();
    }

    // Gets the cards in a hand of a particular value, or all the cards not of that value
    public static Card[] getCardsValue(Card[] hand, int val, boolean match) {
        Card[] cards;
        int size = 0;
        for(int i = 0; i < hand.length; i++) {
            if((hand[i].getValue() == val) == match) {
                size++;
            }
        }

        cards = new Card[size];
        int index = 0;
        for(int i = 0; i < hand.length; i++) {
            if((hand[i].getValue() == val) == match) {
                cards[index++] = hand[i];
            }
        }
        return cards;
    }

    // Gets the cards in a hand of a particular suit, or all the cards not of that suit
    public static Card[] getCardsSuit(Card[] hand, Suit suit, boolean match) {
        Card[] cards;
        int size = 0;
        for(int i = 0; i < hand.length; i++) {
            if((hand[i].getSuit() == suit) == match) {
                size++;
            }
        }

        cards = new Card[size];
        int index = 0;
        for(int i = 0; i < hand.length; i++) {
            if((hand[i].getSuit() == suit) == match) {
                cards[index++] = hand[i];
            }
        }
        return cards;
    }

    private static int hasNumSame(Card[] hand, int numSame) {
        int numGroups = 0;
        for(int i = 0; i < hand.length - 1; i++) {
            int count = 1;
            while(true) {
                if(i >= hand.length - 1 || hand[i].getValue() != hand[i + 1].getValue())
                    break;
                count++;
                i++;
            }
            if(count == numSame)
                numGroups++;
        }

        return numGroups;
    }

    // Testing different hand values
    public static void main(String[] args) {
        Rules rules = new FiveDrawPokerRules();

        Card[] handa = {new Card(0, Suit.CLUBS), new Card(1, Suit.HEARTS), new Card(2, Suit.HEARTS),
                new Card(3, Suit.HEARTS), new Card(4, Suit.HEARTS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handa));
        Card[] handb = {new Card(3, Suit.CLUBS), new Card(6, Suit.HEARTS), new Card(2, Suit.HEARTS),
                new Card(3, Suit.DIAMONDS), new Card(5, Suit.HEARTS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handb));
        System.out.println(rules.compare(handa, handb));

        Card[] handc = {new Card(0, Suit.CLUBS), new Card(1, Suit.HEARTS), new Card(2, Suit.HEARTS),
                new Card(3, Suit.HEARTS), new Card(4, Suit.HEARTS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handc));
        Card[] handd = {new Card(1, Suit.HEARTS), new Card(2, Suit.HEARTS), new Card(3, Suit.HEARTS),
                new Card(4, Suit.HEARTS), new Card(5, Suit.CLUBS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handd));
        System.out.println(rules.compare(handc, handd));

        Card[] hande = {new Card(4, Suit.HEARTS), new Card(4, Suit.SPADES), new Card(9, Suit.CLUBS),
                new Card(10, Suit.CLUBS), new Card(11, Suit.CLUBS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(hande));
        Card[] handf = {new Card(0, Suit.HEARTS), new Card(0, Suit.SPADES), new Card(2, Suit.DIAMONDS),
                new Card(3, Suit.HEARTS), new Card(7, Suit.DIAMONDS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handf));
        System.out.println(rules.compare(hande, handf));

        Card[] handg = {new Card(6, Suit.SPADES), new Card(9, Suit.HEARTS), new Card(9, Suit.DIAMONDS),
                new Card(12, Suit.SPADES), new Card(12, Suit.CLUBS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handg));
        Card[] handh = {new Card(2, Suit.DIAMONDS), new Card(2, Suit.SPADES), new Card(5, Suit.DIAMONDS),
                new Card(5, Suit.SPADES), new Card(8, Suit.DIAMONDS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handh));
        System.out.println(rules.compare(handg, handh));

        //[Three of Spades][Five of Spades][Eight of Spades][King of Hearts][King of Spades]
        //[Two of Diamonds][Jack of Hearts][Queen of Hearts][Queen of Clubs][King of Diamonds]
        Card[] handj = {new Card(2, Suit.SPADES), new Card(4, Suit.SPADES), new Card(7, Suit.SPADES),
                new Card(12, Suit.HEARTS), new Card(12, Suit.SPADES)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handj));
        Card[] handk = {new Card(1, Suit.DIAMONDS), new Card(9, Suit.HEARTS), new Card(11, Suit.HEARTS),
                new Card(11, Suit.CLUBS), new Card(12, Suit.DIAMONDS)};
        System.out.println(FiveDrawPokerRules.getHandRanking(handk));
        System.out.println(rules.compare(handj, handk));

    }
}

