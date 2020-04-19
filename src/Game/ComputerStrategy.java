/**
 * File:        Table.java
 * Description: Poker Table
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */


package Game;

public enum ComputerStrategy {

    SAFE(8),

    NORMAL(5),

    RISKY(2),

    RANDOM((int) (Math.random() * 10));

    private int riskiness;
    public static final int FOLD = 40, INCREASE = 30;

    ComputerStrategy(int riskiness) {
        this.riskiness = riskiness;
    }

    public int getRiskiness() {
        return riskiness;
    }
}
