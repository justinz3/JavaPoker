/**
 * File:        Poker.java
 * Description: 5 Card Draw Poker Implementation
 * Created:     12/12/18
 *
 * @author Justin Zhu
 * @version 1.0
 */


import Game.Difficulty;
import Game.FiveDrawPokerRules;
import Game.Settings;
import Player.*;
import Table.*;
import UI.UserInterface;


public class Poker {

    private PokerPlayer[] players;
    private boolean[] inRound; // means a player hasn't folded yet
    private boolean[] bankrupt; // true means a player will not continue playing
    private int currentBet; // the amount required to stay in the game
    private Table table;
    private UserInterface ui = Settings.ui;
    private int currentDealer = 0;
    private final int humanPlayerIndex = 0;

    private Poker() {
        this.table = new Table();
        this.table.shuffleDeck();
        new Settings(Difficulty.MEDIUM);
    }

    private void run() {
        // Greetings
        greet();

        // Menu
        menu();
    }

    private void greet() {
        ui.display("Welcome to Five Card Draw Poker!");
    }

    private void playPoker() {
        // Get Game Info
        // Player Info
        getGameInfo();

        displayAllPlayers();

        String[] options = {"Continue", "Quit"};
        initPoker();
        do {
            // Start game
            // deal
            // get bets
            // discard
            // get bets
            // distribute winnings
            if(!round()) {
                break;
            }
        }while(ui.getChoice(options, "You have completed a round, would you like to keep going?") == 1); // while the user wants to continue

        sayGoodbye();
    }

    private void menu() {
        int choice = 0;
        String[] choices = {"Play Poker!", "Learn to Play", "Credits", "Exit"};
        do {
            choice = ui.getChoice(choices, "Main Menu");

            switch (choice) {
                case 1:
                    playPoker();
                    break;
                case 2:
                    ui.display( "LEARN TO PLAY:\n" +
                        "How do I explain this?...\n" +
                        "I guess I'll go over how a game normally goes \n" +
                        "(not real Poker, but my version which will no doubt be inconsistent with the real one)\n\n" +
                        "Each player is dealt " + (Settings.rules.MAX_HAND_SIZE) + " cards\n" +
                        "Each player takes turns betting starting from the player after to the dealer and goes in increasing order of player number\n" +
                        "All money goes into the kitty (In game, \"Current Bet\" means the amount of money you need to have paid to continue playing)\n" +
                        "When a player raises the bet, all players react to it, either folding or matching the raise\n" +
                        "If it is not possible for someone to match a raise, they can still match it with whatever money they do have\n" +
                        "Once everyone is done betting, each player (in the same order as betting) chooses some cards from their hand to discard\n" +
                        "These are then replaced with cards from the deck\n" +
                        "After this exchange happens for each player, another round of betting happens\n" +
                        "After the second (and final) round of betting, Each player's hand is revealed and the player with the best hand wins!\n" +
                        "(If there are *exact* ties, the kitty will be split among the tied players)\n" +
                        "I won't get into how to determine the quality of a hand, but there should be a .png containing the basic rules\n\n" +
                        "Well, that was it! (I know right? You didn't come here to read this much, sorry about that)\n" +
                        "You (theoretically) know how to play my Poker Projects idea of Poker. Have fun! And Good Luck!\n");
                    break;
                case 3:
                    ui.display( "CREDITS:\n" +
                        "This Game was made possible by:\n" +
                        "IntelliJ - Who autofilled most of the code\n" +
                        "AP Java - Responsible for the assignment of this project\n" +
                        "Mrs. DiBenedetto - Who presumably came up with the idea for this project\n" +
                        "Java - For providing a language in which to express this project\n" +
                        "Justin Zhu - The person who unnecessarily complicated this with additional features which were never implemented\n" +
                        "\tAlso the person you should go to if you find any bugs in this program\n" +
                        "YOU! - For putting up with this \"great\" project. Thank you!\n");
                    break;
                case 4:
                    ui.display("Bye!\n");
                    System.exit(0);
            }
        } while(true);
    }

    private void getGameInfo() {
        // TODO take in difficulty
        int numPlayers = ui.getNumPlayers();
        players = new PokerPlayer[numPlayers];
        // TODO don't assume one human player
        String name = ui.getPlayerName(1);
        players[humanPlayerIndex] = new PokerPlayer(name);
        // TODO allow input # of chips
        for(int i = 1; i < numPlayers; i++) {
            players[i] = new ComputerPokerPlayer();
        }
    }

    private void displayAllPlayers() {
        ui.display("Here are all the players: ");
        for(int i = 0; i < players.length; i++) {
            ui.display(players[i].getName() + "\n");
        }
        ui.waitForReaction();
    }

    private void sayGoodbye() {
        // Farewells
        ui.display(String.format("You ended this session with $%.2f\n", players[humanPlayerIndex].getMoney()));
        ui.display("Thanks for playing Poker!\n");
        ui.waitForReaction();
    }

    // one round of Poker
    private boolean round() {
        if(!initRound()) {
            return false; // if there can't be a round, end the round
        }
        getMinAnte();
        dealAll();
        getBets(this.currentDealer);
        if(onlyOne()) {
            payout(getWinner());
        }
        else {
            discard();
            getBets(this.currentDealer);
            payout(getWinner());
        }

        this.currentDealer = (this.currentDealer + 1) % players.length;
        return true;
    }

    private void initPoker() {
        currentDealer = 0;
        currentBet = 0;
    }

    private boolean initRound() {
        table.shuffleDeck();
        ui.display(String.format("Current Dealer is %s, and all %d cards in the deck have been shuffled", players[currentDealer].getName(), table.getDeckSize()));
        int count = 0;
        inRound = new boolean[players.length];
        bankrupt = new boolean[players.length];
        for(int i = 0; i < inRound.length; i++) {
            bankrupt[i] = players[i].getMoney() < Settings.difficulty.getMinAnte();
            inRound[i] = !bankrupt[i];
            if(!inRound[i]) {
                continue;
            }
            count++;
            ui.display(String.format("%s will be participating in this round with $%.2f\n", players[i].getName(), players[i].getMoney()));
        }
        if(count <= 1) {
            ui.display("Uh oh! There aren't enough players (with enough money) to keep playing! Congrats on bankrupting them all? The game will now exit\n");
            sayGoodbye();
            return false;
        }
        if(bankrupt[humanPlayerIndex]) {
            ui.display("Uh... You lost all your money. The game will now exit\n");
            sayGoodbye();
            return false;
        }
        ui.waitForReaction();
        return true;
    }

    private void fold(int index) {
        inRound[index] = false;
    }

    private void getMinAnte() {
        for(int i = 0; i < players.length; i++) {
            if(!inRound[i]) {
                continue;
            }
            betChips(players[i], Settings.difficulty.getMinAnte());
        }
        currentBet = Settings.difficulty.getMinAnte();
        ui.displayKitty(currentBet, table.getKitty());
        ui.waitForReaction();
    }

    private void dealAll() {
        for(int i = 0; i < players.length; i++) {
            if(bankrupt[i]) {
                continue;
            }
            for(int j = 0; j < Settings.rules.MAX_HAND_SIZE; j++) {
                players[i].setCard(table.deal());
            }
        }
        ui.display("Cards have been dealt\n");
        ui.display(players[humanPlayerIndex]); // human player
        ui.waitForReaction();
    }

    private void getBets(int currentDealer) {
        ui.display("Betting has begun\n");
        for(int i = 1; i <= players.length; i++) {
            int index = (currentDealer + i) % players.length;
            if(!inRound[index]) {
                continue;
            }

            double bet = players[index].getBet(currentBet, table.getKitty());
            if(bet == -1 || bet < 0) { // fold
                fold(index);
                if(players[index] instanceof ComputerPokerPlayer) {
                    ui.display(String.format("%s Folded\n", players[index].getName()));
                    ui.waitForReaction();
                }
                if(onlyOne()) {
                    return;
                }
            }
            else if(bet == 0 || Math.abs(bet) < 0.01) { // check
                if(players[index] instanceof ComputerPokerPlayer) {
                    ui.display(String.format("%s checked\n", players[index].getName()));
                    ui.waitForReaction();
                }
            }
            else { // raise
                if(players[index] instanceof ComputerPokerPlayer) {
                    ui.display(String.format("%s raised by $%.2f\n", players[index].getName(), bet));
                    ui.waitForReaction();
                }
                double raise = getReactions(index, table.getKitty(), bet);
                currentBet += raise;
            }

        }
    }

    // Returns how much the bet was raised
    private double getReactions(int start, double kitty, double change) {
        ui.display(String.format("%s has increased by $%.2f\n", players[start].getName(), change));
        double minBet = change;
        PokerPlayer leastMoney = null;
        for(int i = 1; i < players.length; i++) {
            int index = (start + i) % players.length;
            if(!inRound[index]) {
                continue;
            }
            int reaction = players[index].getReaction(currentBet, kitty, change);
            if(reaction == 0) { // match
                ui.display(String.format("%s has matched the $%.2f increase\n", players[index].getName(), change));

                // According to what a friend showed me, here: https://www.pokerlistings.com/rules-for-poker-all-in-situations-poker-side-pot-calculator
                // The bet is the minimum of what people can bet, but the rest is in a side kitty...
                //      and I'm not implementing that part because laziness (it's quite a bit of work)
                if(minBet > players[index].getMoney()) {
                    minBet = players[index].getMoney();
                    leastMoney = players[index];
                }
                ui.waitForReaction();
                continue;
            }
            // the player folded, reaction == -1
            ui.display(String.format("%s has folded\n", players[index].getName()));
            fold(index);
            ui.waitForReaction();
            if(onlyOne()) {
                return change;
            }
        }

        if(minBet != change) {
            ui.display(String.format("Because %s doesn't have enough chips, the $%.2f increase is now an increase of $%.2f",
                    leastMoney.getName(), change, minBet));
        }
        for(int i = 0; i < players.length; i++) {
            int index = (start + i) % players.length;
            if(!inRound[index])
                continue;
            betChips(players[index], minBet);
        }

        ui.display("Resuming with betting\n");
        ui.waitForReaction();
        return minBet;
    }

    private void betChips(PokerPlayer player, double chips) {
        player.takeChips(chips);
        table.update(chips);
    }

    private boolean onlyOne() {
        int count = 0;
        for(int i = 0; i < inRound.length; i++) {
            count += inRound[i] ? 1 : 0;
            if(count > 1)
                return false;
        }
        // Should we just assume the count is never 0?
        return true;
    }

    private void discard() {
        for(int i = 0; i < players.length; i++) {
            int index = (currentDealer + i) % players.length;
            if (!inRound[index]) {
                continue;
            }

            Card[] discarded = players[index].exchangeCards();
            table.returnToDeck(discarded);

            int numCards = discarded.length;

            players[index].fixCards();
            for(int j = 0; j < numCards; j++) {
                Card dealt = table.deal();
                if(dealt == null) {
                    throw new NullPointerException("Deck dealt a null card");
                }
                players[index].setCard(dealt);
            }

            table.shuffleDeck();

            if(!(players[index] instanceof ComputerPokerPlayer)) {
                ui.display(players[index]); // let the player know about their new hand
                ui.waitForReaction();
            }
            else {
                ui.display(String.format("%s has exchanged %d of their cards\n", players[index].getName(), numCards));
            }
        }
        ui.display("Everyone has exchanged cards\n");
        ui.waitForReaction();
    }

    private PokerPlayer[] getWinner() {

        revealHands();

        int bestIndex = -1;
        int numWinners = 0;
        Card[][] hands = new Card[players.length][Settings.rules.MAX_HAND_SIZE];
        for(int i = 0; i < players.length; i++) {
            if(bankrupt[i])
                continue;
            hands[i] = players[i].discard();
            table.returnToDeck(hands[i]);
        }
        for(int i = 0; i < players.length; i++) {
            if(!inRound[i])
                continue;
            if(bestIndex == -1) {
                bestIndex = i;
                numWinners = 1;
                continue;
            }

            int comparison = Settings.rules.compare(hands[bestIndex], hands[i]);
            if(comparison < 0) {
                bestIndex = i;
                numWinners = 1;
            }
            if(comparison == 0) {
                numWinners++;
            }
        }

        if(bestIndex == -1 || numWinners == 0) {
            throw new NullPointerException("Uh oh, the program couldn't determine a winner (Complain to Justin about it)");
        }
        PokerPlayer[] winners = new PokerPlayer[numWinners];
        int index = 0;
        for(int i = 0; i < players.length; i++) {
            if(!inRound[i])
                continue;
            int comparison = Settings.rules.compare(hands[bestIndex], hands[i]);
            if(comparison == 0) {
                winners[index++] = players[i];
            }
        }

        String names = winners[0].getName();
        for(int i = 1; i < winners.length; i++) {
            names += " and " + winners[i].getName();
        }
        ui.display(String.format("%s has the highest hand with a %s!\n", names, FiveDrawPokerRules.getHandRanking(hands[bestIndex]).toString()));
        return winners;
    }

    private void revealHands() {
        ui.display("Final Kitty: \n");
        ui.displayKitty(currentBet, table.getKitty());

        for(int i = 0; i < players.length; i++) {
            if(bankrupt[i]) { // display only the non-bankrupt players which participated in the round
                continue;
            }
            ui.display(players[i]);
            ui.display(String.format("%s had %s\n", players[i].getName(), inRound[i] ? "lasted to the end" : "folded"));
            ui.waitForReaction();
        }
    }

    private void payout(PokerPlayer[] winners) {
        payout(winners, table.payout());
    }

    private void payout(PokerPlayer winner, double amt) {
        winner.giveChips(amt);
        ui.display(String.format("Yay! %s now has $%.2f!", winner.getName(), winner.getMoney()));
        ui.waitForReaction();
    }

    private void payout(PokerPlayer[] winners, double amt) {
        double kitty = amt;
        double split = kitty / winners.length;
        for(int i = 0; i < winners.length; i++) {
            payout(winners[i], split);
        }
    }

    public static void main(String[] args) {
        (new Poker()).run();
    }
}
