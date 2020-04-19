package Table;

public class DeckTester {
    public static void main(String[] args)
    {

        Card[] player1Hand = new Card[7];
        Card[] player2Hand = new Card[7];

        Deck myDeck = new Deck();

        System.out.println("****New Deck *******");

        System.out.println(myDeck);

        myDeck.shuffle();

        System.out.println("****Shuffled Deck - ensure order changed *******");
        System.out.println(myDeck);

        System.out.println("****Check Deck - ensure all cards here *******");
        System.out.println(myDeck.checkDeck());

        for(int i=0; i<7; i++)
        {
            player1Hand[i] = myDeck.deal();
            player2Hand[i] = myDeck.deal();
        }

        System.out.println("****Check Deck After Dealing 14 Cards *******");
        System.out.println(myDeck.checkDeck());

        System.out.println("*****Deck After Dealing 14 ********");
        System.out.println(myDeck);
        myDeck.shuffle();//shuffle smaller deck

        System.out.println("*******Shuffled Smaller Deck: ");
        System.out.println(myDeck);
        System.out.println();

        System.out.println("**********Player 1 Hand:*********");
        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //Ensure we do not try to access null reference
                System.out.print(" " + player1Hand[i]);
        }
        Deck.sortCards(player1Hand);
        System.out.println("Sorted");
        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //Ensure we do not try to access null reference
                System.out.print(" " + player1Hand[i]);
        }

        System.out.println("\n**********Player 2 Hand:*********");
        for(int i=0; i<player2Hand.length; i++)
        {
            if(player2Hand[i]!=null)
                System.out.print(" " + player2Hand[i]);
        }

        //Return player 1's middle card:
        myDeck.returnToDeck(player1Hand[player1Hand.length/2]);
        player1Hand[player1Hand.length/2]=null;

        System.out.println("\n***********player 1 hand after returning card**************:");

        for(int i=0; i<player1Hand.length; i++)
        {
            if(player1Hand[i]!=null) //needed so we don't access null Card
                System.out.print(" " + player1Hand[i]);
        }
        System.out.println("\n**********Deck after having one card returned ***********//*");
        System.out.println("\n" + myDeck);
        System.out.println(myDeck.checkDeck());

        Deck.fixCards(player1Hand);

        myDeck.returnToDeck(player1Hand);
        myDeck.returnToDeck(player2Hand);
        System.out.println("\n**********Deck after all cards returned ***********//*");

        System.out.println(myDeck.checkDeck());
        System.out.println(myDeck);

    }


}
