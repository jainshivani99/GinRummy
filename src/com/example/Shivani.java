package com.example;

import com.example.Card;
import com.example.Meld;
import com.example.PlayerStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Shivani implements PlayerStrategy{

    //The player's hand of cards
    private List<Card> hand = new ArrayList<Card>();
    private static List<Card> cardsNotInMeld = new ArrayList<Card>();
    private static List<Meld> totalMelds = new ArrayList<Meld>();
    private static int deadwoodPoints;
    private static int knockLevel = 7;

    private static boolean isKnocking = false;

    /**
     * Called by the game engine for each player at the beginning of each round to receive and
     * process their initial hand dealt.
     *
     * @param hand The initial hand dealt to the player
     */
    public void receiveInitialHand(List<Card> hand){
        cardsNotInMeld = hand;
        Shivani.createRunMeld();
        Shivani.createSetMeld();
        deadwoodPoints = Shivani.calculateDeadwoodPoints();
        knock();
    }

    /**
     * Called by the game engine to prompt the player on whether they want to take the top card
     * from the discard pile or from the deck.
     *
     * @param card The card on the top of the discard pile
     * @return whether the user takes the card on the discard pile
     */
    public boolean willTakeTopDiscard(Card card){
        boolean tookDiscardCard = false;
        for (Meld meldObj : totalMelds) {
            if (meldObj.canAppendCard(card)) {
                tookDiscardCard = true;
                meldObj.appendCard(card);
                break;
            }
        }
        return tookDiscardCard;
    }

    /**
     * Called by the game engine to prompt the player to take their turn given a
     * dealt card (and returning their card they've chosen to discard).
     *
     * @param drawnCard The card the player was dealt
     * @return The card the player has chosen to discard
     */
    public Card drawAndDiscard(Card drawnCard){
        return null;
    }

    /**
     * Called by the game engine to prompt the player if whether they would like to
     * knock.
     *
     * @return True if the player has decided to knock
     */
    public boolean knock(){
        if (deadwoodPoints <= knockLevel) {
            isKnocking = true;
        }
        return isKnocking;
    }

    /**
     * Called by the game engine when the opponent has finished their turn to provide the player
     * information on what the opponent just did in their turn.
     *
     * @param drewDiscard Whether the opponent took from the discard
     * @param previousDiscardTop What the opponent could have drawn from the discard if they chose to
     * @param opponentDiscarded The card that the opponent discarded
     */
    public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded){

    }

    /**
     * Called by the game engine when the round has ended to provide this player strategy
     * information about their opponent's hand and selection of Melds at the end of the round.
     *
     * @param opponentHand The opponent's hand at the end of the round
     * @param opponentMelds The opponent's Melds at the end of the round
     */
    public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds){

    }

    /**
     * Called by the game engine to allow access the player's current list of Melds.
     *
     * @return The player's list of melds.
     */
    public List<Meld> getMelds(){
        return totalMelds;
    }

    /**
     * Called by the game engine to allow this player strategy to reset its internal state before
     * competing it against a new opponent.
     */
    public void reset(){

    }

    //-------------------------------------------------------------------------------
    // 0 to 3 melds are possible, combination of run meld and set meld
    // First try to create run melds for each suit
    //Then try to create suit melds of remaining cards
    //Then remaining cards are deadwood card
    //-------------------------------------------------------------------------------

    //Run Meld creation
    public static void createRunMeld() {

        //RunSort: Sort cardsNotInMeld (initially full hand) based on suit and then rank
        List<Card> p1PotentialRunMeld = new ArrayList<Card>(cardsNotInMeld);
        //Shuffling the cards cardsNotInMeld to test if RunSort worked - Comment/remove after test
        Collections.shuffle(p1PotentialRunMeld);

        Collections.sort(p1PotentialRunMeld, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                int suitDifference = o1.getSuit().ordinal() - o2.getSuit().ordinal();
                if (suitDifference == 0) {
                    return o1.getRankValue() - o2.getRankValue();
                }
                return suitDifference;
            }
        });

        //Iterate through sorted cardsNotInMeld (initially full hand) = p1PotentialRunMeld
//        int startingIndex = 0;
//        int endingIndex = p1PotentialRunMeld.size() - 1;
//        while (startingIndex < endingIndex) {
//            //Check if all cardsNotInMeld are of same rank, if yes do what?
//            if (p1PotentialRunMeld.get(startingIndex).getRank() == p1PotentialRunMeld.get(endingIndex).getRank()) {
//
//            }
//            List<Card> p1PotentialRunMeld2 = new ArrayList<Card>(p1PotentialRunMeld);
//            //if (p1PotentialRunMeld.get(0).getRank()) {
//                //special case- check to see if the there is a run of 10 cards, check 10th element to see if it is
//                //equal to first element + 9
//            //}
//        }
        //List<Card> p1PotentialRunMeld2 = new ArrayList<Card>();
        //p1PotentialRunMeld2.add(p1PotentialRunMeld.get(0));
        //p1PotentialRunMeld2.add(p1PotentialRunMeld.get(1));
        //RunMeld p1RunMeld = Meld.buildRunMeld(p1PotentialRunMeld);
        System.out.println("");
    }


    //Set Meld creation
    public static void createSetMeld() {
        //Sort remaining cards based on rank to create Set Meld
        List<Card> p1PotentialSetMeld = new ArrayList(cardsNotInMeld);
        //Shuffling the cards cardsNotInMeld to test if SetSort worked - Comment/remove after test
        Collections.shuffle(p1PotentialSetMeld);

        Collections.sort(p1PotentialSetMeld, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRankValue() - o2.getRankValue();
            }
        });

        //Iterate through the sorted cards
        //iterator i - if the 4th element is the same, you can assume that the the cards in the middle
        // are the same because they are sorted
        int i = 0;
        while (i < p1PotentialSetMeld.size()) {
            boolean canCheck4 = (i + 3) < p1PotentialSetMeld.size();
            boolean canCheck3 = (i + 2) < p1PotentialSetMeld.size();
            if (canCheck4 && p1PotentialSetMeld.get(i).getRank().equals(p1PotentialSetMeld.get(i + 3).getRank())) {
                List<Card> p1PotentialSetMeld2 = new ArrayList<Card>();
                p1PotentialSetMeld2.add(p1PotentialSetMeld.get(i));
                p1PotentialSetMeld2.add(p1PotentialSetMeld.get(i + 1));
                p1PotentialSetMeld2.add(p1PotentialSetMeld.get(i + 2));
                p1PotentialSetMeld2.add(p1PotentialSetMeld.get(i + 3));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i + 1));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i + 2));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i + 3));
                SetMeld p1SetMeld = Meld.buildSetMeld(p1PotentialSetMeld2);
                totalMelds.add(p1SetMeld);
                i += 4;
            } else if(canCheck3 && p1PotentialSetMeld.get(i).getRank().equals(p1PotentialSetMeld.get(i + 2).getRank()))
            {
                List<Card> p1PotentialSetMeld3 = new ArrayList<Card>();
                p1PotentialSetMeld3.add(p1PotentialSetMeld.get(i));
                p1PotentialSetMeld3.add(p1PotentialSetMeld.get(i + 1));
                p1PotentialSetMeld3.add(p1PotentialSetMeld.get(i + 2));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i + 1));
                cardsNotInMeld.remove(p1PotentialSetMeld.get(i + 2));
                SetMeld p1SetMeld2 = Meld.buildSetMeld(p1PotentialSetMeld3);
                totalMelds.add(p1SetMeld2);
                i += 3;
            } else {
                i++;
            }
        }
        //-------------------------------------------------------------------------------
    }

    // Calculate deadwood points
    public static int calculateDeadwoodPoints() {
        deadwoodPoints = 0;
        for (Card cardObj : cardsNotInMeld) {
            deadwoodPoints += cardObj.getPointValue();
        }
        return deadwoodPoints;

    }


}
