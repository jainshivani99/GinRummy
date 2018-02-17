import com.example.Card;
import com.example.Meld;
import com.example.PlayerStrategy;
import java.util.Collections;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private static List<Card> deck;
    private static List<Card> discardPile = new ArrayList<Card>();
    private static Shivani p1 = new Shivani();
    private static Shivani p2 = new Shivani();

    public static void main(String[] args) {
        GameEngine.initializeDeck();
        GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        //Adding a card to the discard pile
        Card topCard = deck.get(deck.size() - 1);
        discardPile.add(topCard);
        GameEngine.playFirstRound();


    }

    //Retrieving all cards in the game and setting up the deck
    public static void initializeDeck() {
        deck = new ArrayList<Card>();
        deck.addAll(Card.getAllCards());
    }

    //Shuffling the deck
    public static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    //Dealing ten cards to each player
    public static void dealDeck() {
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                //end of the array is the top of the deck
                p1.getHand().add(deck.remove(deck.size() - 1));
            } else {
                p2.getHand().add(deck.remove(deck.size() - 1));
            }
        }
        p1.receiveInitialHand(p1.getHand());
        p2.receiveInitialHand(p2.getHand());
    }

    //Assume that p1 is always the starting player
    public static int playFirstRound() {
        //Run runMeld with p1's hand & get deadwood points
        Meld playerRunMeld = Meld.buildRunMeld(p1.getHand());
        int p1DwpRun = GameEngine.calculateDeadwoodPoints(playerRunMeld, p1.getHand());
        //Run setMeld with p1's hand & get deadwood points
        Meld playerSetMeld = Meld.buildSetMeld(p1.getHand());
        int p1DwpSet = GameEngine.calculateDeadwoodPoints(playerSetMeld, p1.getHand());
        //compare 2 deadwood points from above & keep the meld with lower deadwood points
        if (p1DwpRun > p1DwpSet) {
            return p1DwpSet;
        } else {
            return p1DwpRun;
        }
        //Card topCard = discardPile.get(0);
        //p1.willTakeTopDiscard(topCard);
    }

    public static void playNormalRound() {

    }

    public static int calculateDeadwoodPoints(Meld playerMeld, List<Card> playerHand) {
        int deadwoodPoints = 0;
        if (playerMeld != null) {
            for (Card cardObj : playerHand) {
                if (!playerMeld.containsCard(cardObj)) {
                    deadwoodPoints += cardObj.getPointValue();
                }
            }
        } else {
            for (Card cardObj : playerHand) {
                deadwoodPoints += cardObj.getPointValue();
            }
        }
        return deadwoodPoints;
    }




}
