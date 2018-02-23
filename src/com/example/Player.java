package com.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Player implements PlayerStrategy {


    //protected List<Card> hand = new ArrayList<Card>();
    protected List<Card> cardsNotInMeld = new ArrayList<Card>();
    protected List<Meld> totalMelds = new ArrayList<Meld>();
    protected int deadwoodPoints;
    protected int knockLevel = 7;
    protected boolean isKnocking = false;
    protected int totalPoints;


    // Calculate deadwood points
    public int calculateDeadwoodPoints() {
        deadwoodPoints = 0;
        for (Card cardObj : cardsNotInMeld) {
            deadwoodPoints += cardObj.getPointValue();
        }
        return deadwoodPoints;

    }

    //reveals the deadwood cards of a player
    public List<Card> revealCardsNotInMeld() {
        return cardsNotInMeld;
    }

    public void setTotalPoints(int newPoints) {
        this.totalPoints = newPoints;
    }

    public int getTotalPoints() {
        return this.totalPoints;
    }
    //-------------------------------------------------------------------------------
    // 0 to 3 melds are possible, combination of run meld and set meld
    // First try to create run melds for each suit
    //Then try to create suit melds of remaining cards
    //Then remaining cards are deadwood card
    //-------------------------------------------------------------------------------

    //Run Meld creation
    public void createRunMeld() {

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
    public void createSetMeld() {
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
}
