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
        GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        Player nextActionPlayer = GameEngine.playFirstRound();
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
    /** Playing the first round of the game
     @return The PlayerStrategy that goes next.
     */
    public static Player playFirstRound() {

        // Assume that p1 is always the starting player
        Card topCard = discardPile.get(0);
        Card returnCard;
        Player nextActionPlayer;
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
        // put return card ON TOP OFF discard pile.
        discardPile.add(0, returnCard);


        // allow players to knock/gin
        // Assume starting player can knock/gin first.
        if (p1.knock()) {
            GameEngine.callKnock(p1, p2);
        } else if (p2.knock()) {
            GameEngine.callKnock(p2, p1);
        }

        return nextActionPlayer;
    }


    //-------------------------------------------------------------------------------
    /** Playing a normal round of the game
     @param nextActionPlayer the PlayerStrategy that starts the game.
     */
    public static void playNormalRound(Player nextActionPlayer) {

        //Checking which player is nextActionPlayer and assigning the otherPlayer
        Player otherPlayer;
        if (nextActionPlayer == p1) {
            otherPlayer = p2;
        } else {
            otherPlayer = p1;
        }

        //Checking if the deck is empty, which means that the round ends
        while (deck.size() > 0) {
            Card topCard = discardPile.get(0);
            Card returnCard;

            //Start off the round with nextActionPlayer
            if (nextActionPlayer.willTakeTopDiscard(topCard)) {
                returnCard = nextActionPlayer.drawAndDiscard(topCard);
                discardPile.add(0, returnCard);
                otherPlayer.opponentEndTurnFeedback(true, topCard, returnCard);
            } else {
                Card cardFromDeck = deck.get(0);
                returnCard = nextActionPlayer.drawAndDiscard(cardFromDeck);
                discardPile.add(0, returnCard);
                nextActionPlayer = p2;
                otherPlayer = p1;
                otherPlayer.opponentEndTurnFeedback(false, cardFromDeck, returnCard);
            }

            //otherPlayer's turn
            if (otherPlayer.willTakeTopDiscard(topCard)) {
                returnCard = otherPlayer.drawAndDiscard(topCard);
                discardPile.add(0, returnCard);
                nextActionPlayer.opponentEndTurnFeedback(true, topCard, returnCard);
            } else {
                Card cardFromDeck = deck.get(0);
                returnCard = otherPlayer.drawAndDiscard(cardFromDeck);
                discardPile.add(0, returnCard);
                nextActionPlayer.opponentEndTurnFeedback(false, cardFromDeck, returnCard);
                nextActionPlayer = p1;
                otherPlayer = p2;
            }

            // allow players to knock/gin
            if (nextActionPlayer.knock()) {
                GameEngine.callKnock(nextActionPlayer, otherPlayer);
                break;
            } else if (otherPlayer.knock()) {
                GameEngine.callKnock(otherPlayer, nextActionPlayer);
                break;
            }

        }

        System.out.println("This round has ended in a tie. No points will be awarded.");
        p1.opponentEndRoundFeedback(p2.revealCardsNotInMeld(), p2.getMelds());
        p2.opponentEndRoundFeedback(p1.revealCardsNotInMeld(), p1.getMelds());

    }

    public static void callKnock(Player knockPlayer, Player opponent) {
        //the player that knocked reveals their cards
        List<Meld> revealMelds = knockPlayer.getMelds();

        int knockPlayerDeadwoodPoints = knockPlayer.calculateDeadwoodPoints();
        int opponentDeadwoodPoints = opponent.calculateDeadwoodPoints();

        //check if knocking player has declared gin, which means that they will be awarded points and the
        // opponent cannot add to their melds
        if (knockPlayerDeadwoodPoints == 0) {
            int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + opponentDeadwoodPoints;

        }

        // the opponent should be able to check if he can use his cards in the knocking player's melds
        List<Card> revealOpponentsCardsNotInMeld = opponent.revealCardsNotInMeld();
        for (Meld meldObj : revealMelds) {
            for (Card cardObj : revealOpponentsCardsNotInMeld) {
                if (meldObj.canAppendCard(cardObj)) {
                    meldObj.appendCard(cardObj);
                    revealOpponentsCardsNotInMeld.remove(cardObj);
                    break;
                }
            }
        }
        //add knock points to either the knocking player or  opponent
        if (knockPlayerDeadwoodPoints < opponentDeadwoodPoints) {
            int newDeadWoodPoints = knockPlayerDeadwoodPoints - opponentDeadwoodPoints;
            knockPlayer.setTotalPoints(newDeadWoodPoints);
        } else {
            int newDeadWoodPoints = DEADWOOD_POINTS_BOUNDARY + (knockPlayerDeadwoodPoints - opponentDeadwoodPoints);
            opponent.setTotalPoints(newDeadWoodPoints);
        }

        // check if game is over depending on if either player has reached 50 points
        if (knockPlayer.getTotalPoints() >= DEADWOOD_POINTS_END || opponent.getTotalPoints() >= DEADWOOD_POINTS_END) {
            System.out.println("The game has ended.");
            System.exit(0);
        }


    }

}

