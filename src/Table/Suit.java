package Table;

public enum Suit {
    HEARTS("Hearts", 0),

    DIAMONDS("Diamonds", 1),

    SPADES("Spades", 2),

    CLUBS("Clubs", 3);

    private String suit;
    public static final int NUM_SUITS = 4;
    private int num;

    Suit(String suit, int num) {
        this.suit = suit;
        this.num = num;
    }

    public String toString() {
        return suit;
    }

    public int compareTo(Suit a, Suit b) {
        return b.num - a.num; // we want to return if a is smaller
    }

    public int getNum() {
        return num;
    }
}