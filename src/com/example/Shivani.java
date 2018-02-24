package com.example;

import com.example.Card;
import com.example.Meld;
import com.example.PlayerStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Shivani extends Player{

    //All 3 players have a different knock level, or a different amount of points that they call knock at
    private int knockLevel = 7;

    /**
     * Called by the game engine for each player at the beginning of each round to receive and
     * process their initial hand dealt.
     *
     * @param hand The initial hand dealt to the player
     */
    public void receiveInitialHand(List<Card> hand){
        this.cardsNotInMeld = hand;
        this.createRunMeld();
        this.createSetMeld();
        this.deadwoodPoints = this.calculateDeadwoodPoints();
        this.knock();
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
    public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded) {
        
    }

    /**
     * Called by the game engine when the round has ended to provide this player strategy
     * information about their opponent's hand and selection of Melds at the end of the round.
     *
     * @param opponentHand The opponent's hand at the end of the round
     * @param opponentMelds The opponent's Melds at the end of the round
     */
    public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds) {

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
        cardsNotInMeld.clear();
        totalMelds.clear();
        deadwoodPoints = 0;
    }

}
