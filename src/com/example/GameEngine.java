package com.example;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {
    //-------------------------------------------------------------------------------
    //Class Properties

    private static List<Card> deck;
    private static List<Card> discardPile = new ArrayList<Card>();
    private static Player p1 = new Shivani();
    private static Player p2 = new Jack();
    private static Player p3 = new Jill();
    private static final int DEADWOOD_POINTS_BOUNDARY = 25;
    private static final int DEADWOOD_POINTS_END = 50;
    //-------------------------------------------------------------------------------
    // Execute Main Method

    public static void main(String[] args) {

        //-------------------------------------------------------------------------------
        // Initialize deck, shuffle deck, deal deck, create discard pile, play first round and play normalRounds

        GameEngine.initializeDeck();
        //GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        PlayerStrategy nextActionPlayer = GameEngine.playFirstRound();
        GameEngine.playNormalRound(nextActionPlayer);
    }

    //-------------------------------------------------------------------------------
    //Initialize the deck: Retrieving all cards in the game and setting up the deck

    public static void initializeDeck() {
        deck = new ArrayList<Card>();
        deck.addAll(Card.getAllCards());
        Collections.sort(deck);
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
        for (int i = 0; i < 10; i++) {
            if (i % 1 == 0) {
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
    /** Playing the first round of the game
     @return The PlayerStrategy that goes next.
     */
    public static PlayerStrategy playFirstRound() {

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
            p2.opponentEndTurnFeedback(false, drawCard, returnCard);
        }
        // put return card ON TOP OF discard pile.
        discardPile.add(0, returnCard);


        // allow players to knock/gin
        // Assume starting player can gin first.
        if (p1.knock()) {
            // p1 reveals their cards
            List<Meld> revealMelds = p1.getMelds();

            int p1DeadwoodPoints = p1.calculateDeadwoodPoints();
            int p2DeadwoodPoints = p2.calculateDeadwoodPoints();

            //check if p1 has declared gin, which means that they will be awarded points and p2 cannot add to their melds
            if (p1DeadwoodPoints == 0) {
                int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + p2DeadwoodPoints;

            }

            // p2 should be able to check if he can use his cards in p1's melds
            List<Card> revealOpponentsCardsNotInMeld = p2.revealCardsNotInMeld();
            for (Meld meldObj : revealMelds) {
                for (Card cardObj : revealOpponentsCardsNotInMeld) {
                    if (meldObj.canAppendCard(cardObj)) {
                        meldObj.appendCard(cardObj);
                        revealOpponentsCardsNotInMeld.remove(cardObj);
                        break;
                    }
                }
            }
            // add knock points to either p1 or p2
            if (p1DeadwoodPoints < p2DeadwoodPoints) {
                int newDeadWoodPoints = p1DeadwoodPoints - p2DeadwoodPoints;
                p1.setTotalPoints(newDeadWoodPoints);
            } else {
                int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + (p1DeadwoodPoints - p2DeadwoodPoints);
                p2.setTotalPoints(newDeadWoodPoints);
            }

            // check if game is over depending on if either player has reached 50 points
            if (p1.getTotalPoints() >= DEADWOOD_POINTS_END || p2.getTotalPoints() >= DEADWOOD_POINTS_END) {
                System.out.println("The game has ended.");
                System.exit(0);
            }

            //update nextActionPlayer?
        } else if (p2.knock()) {
            // p2 reveals their cards
            List<Meld> revealMelds = p2.getMelds();

            int p1DeadwoodPoints = p1.calculateDeadwoodPoints();
            int p2DeadwoodPoints = p2.calculateDeadwoodPoints();

            //check if p2 has declared gin, which means that they will be awarded points and p1 cannot add to their melds
            if (p2DeadwoodPoints == 0) {
                int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + p1DeadwoodPoints;

            }

            // p2 should be able to check if he can use his cards in p1's melds
            List<Card> revealOpponentsCardsNotInMeld = p1.revealCardsNotInMeld();
            for (Meld meldObj : revealMelds) {
                for (Card cardObj : revealOpponentsCardsNotInMeld) {
                    if (meldObj.canAppendCard(cardObj)) {
                        meldObj.appendCard(cardObj);
                        revealOpponentsCardsNotInMeld.remove(cardObj);
                        break;
                    }
                }
            }
            // add knock points to either p1 or p2
            if (p2DeadwoodPoints < p1DeadwoodPoints) {
                int newDeadWoodPoints = p2DeadwoodPoints - p1DeadwoodPoints;
                p2.setTotalPoints(newDeadWoodPoints);
            } else {
                int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + (p2DeadwoodPoints - p1DeadwoodPoints);
                p1.setTotalPoints(newDeadWoodPoints);
            }

            // check if game is over depending on if either player has reached 50 points
            if (p2.getTotalPoints() >= DEADWOOD_POINTS_END || p1.getTotalPoints() >= DEADWOOD_POINTS_END) {
                System.out.println("The game has ended.");
                System.exit(0);
            }

            //update nextActionPlayer?
        }

        return nextActionPlayer;
    }


    //-------------------------------------------------------------------------------
    // Playing the Normal round
    public static void playNormalRound(PlayerStrategy nextActionPlayer) {
        // TODO(jainshivani99) do implementation, following similar style
        // to playFirstRound.

        //Checking if the deck is empty, which means that the round ends

            Card topCard = discardPile.get(0);
            Card returnCard;

            //Start off the round with p1
            if (p1.willTakeTopDiscard(topCard)) {
                returnCard = p1.drawAndDiscard(topCard);
                nextActionPlayer = p2;
                p2.opponentEndTurnFeedback(true, topCard, returnCard);
            } else {
                Card cardFromDeck = deck.get(0);
                returnCard = p1.drawAndDiscard(cardFromDeck);
                nextActionPlayer = p2;
                p2.opponentEndTurnFeedback(false, cardFromDeck, returnCard);
            }

            //p2's turn
            if (p2.willTakeTopDiscard(topCard)) {
                returnCard = p2.drawAndDiscard(topCard);
                nextActionPlayer = p1;
                p1.opponentEndTurnFeedback(true, topCard, returnCard);
            } else {
                Card cardFromDeck = deck.get(0);
                returnCard = p2.drawAndDiscard(cardFromDeck);
                nextActionPlayer = p1;
                p1.opponentEndTurnFeedback(false, cardFromDeck, returnCard);
            }


        System.out.println("This round has ended in a tie. No points will be awarded.");
        //p1.opponentEndRoundFeedback();
        //p2.opponentEndRoundFeedback();
    }

}

