/**
 * File:        Difficulty.java
 * Description: The different difficulty settings for the game
 * Created:     12/15/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Game;

public enum Difficulty {

    EASY(0, -5, 5),

    MEDIUM(1, 0, 10),

    HARD(2, 10, 15);

    private int randomness, chips;
    private int minAnte;

    Difficulty(int randomness, int chips, int minAnte) {
        this.randomness = randomness;
        this.chips = chips;
        this.minAnte = minAnte;
    }

    public int getRandomness() {
        return randomness;
    }

    public int getChips() {
        return chips;
    }

    public int getMinAnte() {
        return minAnte;
    }
}
