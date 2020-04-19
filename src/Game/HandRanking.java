package Game;

import java.util.Comparator;

public enum HandRanking {
    ROYAL_FLUSH(0),

    STRAIGHT_FLUSH(1),

    FOUR_OF_A_KIND(2),

    FULL_HOUSE(3),

    FLUSH(4),

    STRAIGHT(5),

    THREE_OF_A_KIND(6),

    TWO_PAIR(7),

    ONE_PAIR(8),

    HIGH_CARD(9);

    private int ranking;

    HandRanking(int ranking) {
        this.ranking = ranking;
    }

    public int compareTo(HandRanking o1, HandRanking o2) {
        return o2.ranking - o1.ranking;
    }

    public int getRanking() {
        return ranking;
    }

    // This sort puts the highest ranking hands first in the array
    public Comparator<HandRanking> CardOrder = new Comparator<HandRanking>() {
        public int compare(HandRanking o1, HandRanking o2) {
            return o1.ranking - o2.ranking;
        }
    };
}