/**
 * File:        Kitty.java
 * Description: The pool of bets in a Poker Game
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Table;

public class Kitty {

    private double total;

    Kitty() {
        this.total = 0;
    }

    public void update(double m) {
        total += m;
    }

    public double payout() {
        double money = total;
        total = 0;
        return money;
    }

    public double getTotal() {
        return total;
    }

    public String toString() {
        return String.format("Total: %.2f", total);
    }
}
