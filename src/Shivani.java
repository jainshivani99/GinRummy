import com.example.Card;
import com.example.Meld;
import com.example.PlayerStrategy;

import java.util.ArrayList;
import java.util.List;

public class Shivani implements PlayerStrategy{

    private List<Card> hand = new ArrayList<Card>();

    public List<Card> getHand() {
        return hand;
    }

    /**
     * Called by the game engine for each player at the beginning of each round to receive and
     * process their initial hand dealt.
     *
     * @param hand The initial hand dealt to the player
     */
    public void receiveInitialHand(List<Card> hand){
        this.hand = hand;
    }

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
