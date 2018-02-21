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
    private static List<Card> hand = new ArrayList<Card>();
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
        //Shivani.createRunMeld();
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
        cardsNotInMeld.add(drawnCard);
        Card cardToDiscard = cardsNotInMeld.get(0);
        for (Meld meldObj : totalMelds) {
            //check if your drawn card can be appended to a meld
            //if it can, remove a different card from your hand
            if (meldObj.canAppendCard(drawnCard)) {
                meldObj.appendCard(drawnCard);
                cardsNotInMeld.remove(drawnCard);
                cardsNotInMeld.remove(cardToDiscard);
                break;
            } else {
                //if it cannot, remove that same card
                cardsNotInMeld.remove(drawnCard);
            }
        }
        return cardToDiscard;

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
        //clear the hand of cards and all melds, reset the deadwood points to 0
        hand.clear();
        cardsNotInMeld.clear();
        totalMelds.clear();
        deadwoodPoints = 0;
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
        //int endingIndex = p1PotentialRunMeld.size() - 1;
        List<Card> p1PotentialRunMeld2 = new ArrayList<Card>();
        for (int i = 0; i < p1PotentialRunMeld.size();) {
            int currentCardRankValue = p1PotentialRunMeld.get(i).getRankValue();
            Card.CardSuit currentSuit = p1PotentialRunMeld.get(i).getSuit();

            for (int j = 0; j < p1PotentialRunMeld.size(); j++) {
                Card nextCard = p1PotentialRunMeld.get(j);
                if (nextCard.getSuit().equals(currentSuit)) {
                    if (nextCard.getRankValue() == (currentCardRankValue + 1)) {
                        p1PotentialRunMeld2.add(nextCard);
                        currentCardRankValue = nextCard.getRankValue();
                    }
                }
            }

            RunMeld p1RunMeld = Meld.buildRunMeld(p1PotentialRunMeld2);

            if (p1RunMeld != null) {
                totalMelds.add(p1RunMeld);
                cardsNotInMeld.removeAll(p1PotentialRunMeld2);
                i = 0;
            } else {
                i++;
            }

        }

    }


    //Set Meld creation
    public static void createSetMeld() {
        //Sort remaining cards based on rank to create Set Meld
        List<Card> p1PotentialSetMeld = new ArrayList(cardsNotInMeld);

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
    }

    // Calculate deadwood points
    public static int calculateDeadwoodPoints() {
        deadwoodPoints = 0;
        for (Card cardObj : cardsNotInMeld) {
            deadwoodPoints += cardObj.getPointValue();
        }
        return deadwoodPoints;

    }

    //reveals the whole hand of the player including deadwood cards as well as melds
    public List<Card> revealCards() {
        //cardsNotInMeld are already in the player's hand
        //add the meld objects to the player's hand and reveal the whole hand
        for (Meld meldObj : totalMelds) {
            Card[] meldContents = meldObj.getCards();
            Collections.addAll(hand, meldContents);
        }
        return hand;
    }

}
