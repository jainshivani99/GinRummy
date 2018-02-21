package com.example;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    //-------------------------------------------------------------------------------
    //Class Properties

    private static List<Card> deck;
    private static List<Card> discardPile = new ArrayList<Card>();
    private static PlayerStrategy p1 = new Shivani();
    private static PlayerStrategy p2 = new Jack();
    private static PlayerStrategy p3 = new Jill();
    //-------------------------------------------------------------------------------
    // Execute Main Method

    public static void main(String[] args) {

        //-------------------------------------------------------------------------------
        // Initialize deck, shuffle deck, deal deck, create discard pile, play first round and play normalRounds

        GameEngine.initializeDeck();
        GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        PlayerStrategy nextActionPlayer = GameEngine.playFirstRound();
        GameEngine.playNormalRound(nextActionPlayer);
    }

    //-------------------------------------------------------------------------------
    //Initialize the deck: Retrieving all cards in the game and setting up the deck

    public static void initializeDeck() {
        deck = new ArrayList<Card>();
        deck.addAll(Card.getAllCards());
        //Collections.sort(deck);
    }

    //-------------------------------------------------------------------------------
    //Shuffling the deck

    public static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    //-------------------------------------------------------------------------------
    //Dealing the deck: Dealing ten cards to each player

    public static void dealDeck() {
        List<Card> hand1 = new ArrayList<Card>();
        List<Card> hand2 = new ArrayList<Card>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                //beginning of the array is the top of the deck
                hand1.add(deck.remove(0));
            } else {
                hand2.add(deck.remove(0));
            }
        }

        //Adding a card to the discard pile
        discardPile.add(deck.get(0));
        deck.remove(0);

        //Both player hand are unsorted
        p1.receiveInitialHand(hand1);
        p2.receiveInitialHand(hand2);
    }


    //-------------------------------------------------------------------------------
    /** Playing the first round
     @return The PlayerStrategy that goes next.
     */
    public static PlayerStrategy playFirstRound() {
        // TODO(jainshivani99) Can players knock right after receiving
        // their hands??
        // Assume players can only knock/gin at the end of a round.

        // Assume that p1 is always the starting player
        Card topCard = discardPile.get(0);
        Card returnCard;
        PlayerStrategy nextActionPlayer;
        if (p1.willTakeTopDiscard(topCard)) {
            returnCard = p1.drawAndDiscard(topCard);
            nextActionPlayer = p2;
            p2.opponentEndTurnFeedback(true, topCard, returnCard);
        } else if (p2.willTakeTopDiscard(topCard)) {
            returnCard = p2.drawAndDiscard(topCard);
            nextActionPlayer = p1;
            p1.opponentEndTurnFeedback(true, topCard, returnCard);
        } else { // p1 MUST draw from the deck
            Card drawCard = deck.get(0);
            deck.remove(0);
            returnCard = p1.drawAndDiscard(drawCard);
            nextActionPlayer = p2;
            p2.opponentEndTurnFeedback(true, drawCard, returnCard);
        }
        // put return card ON TOP OF discard pile.
        discardPile.add(0, returnCard);


        // allow players to knock/gin
        // Assume starting player can gin first.
        if (p1.knock()) {
            // p1 reveals their cards
            Tuple<List<Meld>, List<Card>> revealCards = p1.revealCards();
            // p2 should be able to check if he can use his cards in p1's melds
            // add the points to either p1 or p2
            List<Meld> p1Melds = revealCards.first();
            List<Card> p1DeadwoodCards = revealCards.second();
            // Award points

            // check if game is over depending on number of points.
        } else if (p2.knock()) {
            // follow strategy from above.
        }

        return nextActionPlayer;
    }


    //-------------------------------------------------------------------------------
    // Playing the Normal round
    public static void playNormalRound(PlayerStrategy nextActionPlayer) {
        // TODO(jainshivani99) do implementation, following similar style
        // to playFirstRound.
        return;
    }
    //-------------------------------------------------------------------------------
//    // Calculate deadwood points
//    public static int calculateDeadwoodPoints (List<Card> cardsNotInMeld, int deadwoodPoints) {
//
//        for (Card cardObj : cardsNotInMeld) {
//            deadwoodPoints += cardObj.getPointValue();
//        }
//        return deadwoodPoints;
//    }
}

