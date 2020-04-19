/**
 * File:        Settings.java
 * Description: Universal Settings for the game
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */

package Game;

import UI.*;

public class Settings {

    public static Difficulty difficulty = Difficulty.MEDIUM;
    public static UserInterface ui = new TerminalUI(System.in);
    //public static UserInterface ui = new GraphicsUI("Five Draw Poker");
    public static FiveDrawPokerRules rules = new FiveDrawPokerRules();

    public Settings(Difficulty difficulty) {
        Settings.difficulty = difficulty;
    }

    public Settings getSettings() {
        return this;
    }

    public Rules getRules() {
        return rules;
    }
}
