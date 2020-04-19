/**
 * File:        Rules.java
 * Description: Rules interface that may or may not be specific to 5 Card Draw Poker
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Game;

import Table.Card;

import java.util.Comparator;

public class Rules {
    public final int MAX_HAND_SIZE = 0;

    protected Rules() {}

    // This contains the rules for which hand is stronger/better than another
    public int compare(Card[] o1, Card[] o2) {
        return 0; // no comparison yet
    }
}
