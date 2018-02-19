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
        //GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        GameEngine.playFirstRound();
        //GameEngine.playNormalRound();
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

/*
    public static List<Card> getDiscardPile() {
        return discardPile;
    }
*/

    //-------------------------------------------------------------------------------
    // Playing the first round

    //Assume that p1 is always the starting player
    public static void playFirstRound() {
        Card topCard = discardPile.get(0);
        if (p1.knock()) {
            //p1 reveals their cards
            //p2 should be able to check if he can use his cards in p1's melds
            //add the points to either p1 or p2
            //start a new round
            //this functionality should go in knock() itself
            List<Meld> p1Melds = p1.getMelds();
        } else if (p2.knock()) {
            //p2 reveals their cards
            //p1 should be able to check if he can use his cards in p2's melds
            //add the points to either p1 or p2
            //start a new round
            //this functionality should go in knock() itself
            List<Meld> p2Melds = p2.getMelds();
        } else {
            p1.willTakeTopDiscard(topCard);
        }

        //if p1 didn't choose from the discard pile, p2 automatically has the chance to choose from the discard pile
        if (p1.willTakeTopDiscard(topCard) == false) {
            p2.willTakeTopDiscard(topCard);
            if (p2.willTakeTopDiscard(topCard)) {
                p2.drawAndDiscard(topCard);
            }
        } else {
            //if p1 chose from the discard pile, they have to handle their new card and discard another card from their hand
            p1.drawAndDiscard(topCard);
        }


 /*       if(p1DeadwoodPoints == 0){
            // Call p1.gin
            //Calculate game settlement points and add/subtract from p1 & p1 current settlement points
            //game over, start new game
        }else if(p1DeadwoodPoints <= p1.getKnockLevel()){
            // Call p1.knock
            //P2 tries to fit his deadwood cards in melds of P1
            //Calculate P2 deadwood points again
            //Calculate game settlement points and add/subtract from p1 & p1 current settlement points
        }else if(p2DeadwoodPoints == 0){
            // Call p2.gin
            //Calculate game settlement points and add/subtract from p1 & p1 current settlement points
            //game over, start new game
        }else if(p2DeadwoodPoints <= p2.getKnockLevel()){
            // Call p2.knock
            //P1 tries to fit his deadwood cards in melds of P2
            //Calculate P1 deadwood points again
            //Calculate game settlement points and add/subtract from p1 & p1 current settlement points
            //game over, start new game
        }else {*/
            //p1 has option to pick the open card
            //If p1 picks open card
            // p1 add to exisitng melds or in deadwood card pile
            //p1 will discard one card - this will mostly alter deadwood cards unless he throws the same picked card
            //P1 calculates deadwood points
            //else
            //p2 has option to pick the open card
            //If p2 picks open card
            //p2 will discard one card - this will mostly alter deadwood cards unless he throws the same picked card
            //P2 calculates deadwood points
            //Again check p1 gin or knock and p2 gin or knock as above
        }

//        Following commented code to be removed
//        boolean drewDiscarded = false;
//        int p1DwpRun = GameEngine.calculateDeadwoodPoints(playerRunMeld, p1.getHand());
//        //Run setMeld with p1's hand & get deadwood points
//        Meld playerSetMeld = Meld.buildSetMeld(p1.getHand());
//        int p1DwpSet = GameEngine.calculateDeadwoodPoints(playerSetMeld, p1.getHand());
//        //get the first card from the discard pile
//        Card topCard = discardPile.get(0);
//        //compare 2 deadwood points from above & keep the meld with lower deadwood points
//        if (playerRunMeld != null || playerSetMeld != null) {
//            if (p1DwpRun > p1DwpSet) {
//                playerRunMeld = null;
//            } else {
//                playerSetMeld = null;
//            }
//            if (playerRunMeld.canAppendCard(topCard) || playerSetMeld.canAppendCard(topCard)) {
//
//            }
//        }
//        p1.willTakeTopDiscard(topCard);

    //-------------------------------------------------------------------------------
    /*// Playing the Normal round
    public static void playNormalRound() {

    }
    //-------------------------------------------------------------------------------
    // Calculate deadwood points
    public static int calculateDeadwoodPoints (List<Card> cardsNotInMeld, int deadwoodPoints) {

        for (Card cardObj : cardsNotInMeld) {
            deadwoodPoints += cardObj.getPointValue();
        }
        return deadwoodPoints;*/

//    public static int calculateDeadwoodPoints(Meld playerMeld, List<Card> playerHand) {
//        if (playerMeld != null) {
//            for (Card cardObj : playerHand) {
//                if (!playerMeld.containsCard(cardObj)) {
//                    deadwoodPoints += cardObj.getPointValue();
//                }
//            }
//        } else {
//            for (Card cardObj : playerHand) {
//                deadwoodPoints += cardObj.getPointValue();
//            }
//        }

    }
//--------------------------------------------------------------------------------
//}
