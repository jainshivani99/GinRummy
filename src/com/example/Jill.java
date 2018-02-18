package com.example;

import com.example.Card;
import com.example.Meld;
import com.example.PlayerStrategy;

import java.util.List;

public class Jill implements PlayerStrategy {

    private List<Card> playerHand;

    public void receiveInitialHand(List<Card> hand){}

    public boolean willTakeTopDiscard(Card card){
        return false;
    }

    public Card drawAndDiscard(Card drawnCard){
        return null;
    }

    public boolean knock(){
        return false;
    }

    public void opponentEndTurnFeedback(boolean drewDiscard, Card previousDiscardTop, Card opponentDiscarded){}

    public void opponentEndRoundFeedback(List<Card> opponentHand, List<Meld> opponentMelds){}

    public List<Meld> getMelds(){
        return null;
    }

    public void reset(){}

}
