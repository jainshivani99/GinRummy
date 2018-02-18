package com.example;

import java.util.Collections;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.List;

public class GameEngine {

    private static List<Card> deck;
    private static List<Card> discardPile = new ArrayList<Card>();
    private static Shivani p1 = new Shivani();
    private static Shivani p2 = new Shivani();
    private static int deadwoodPoints;
    private static List<Card> cardsNotInMeld = p1.getHand();

    public static void main(String[] args) {
        GameEngine.initializeDeck();
        //GameEngine.shuffleDeck();
        GameEngine.dealDeck();

        //Adding a card to the discard pile
        discardPile.add(deck.get(0));
        deck.remove(0);

        //GameEngine.playFirstRound();
        //List<Card> leftoverCardsFromRunMeld = new ArrayList<Card>();
        List<Meld> totalMelds = new ArrayList<Meld>();

        //Sort run meld based on suit and then rank
        List<Card> p1PotentialRunMeld = new ArrayList<>(cardsNotInMeld);
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
        //List<Card> p1PotentialRunMeld2 = new ArrayList<Card>();
        //p1PotentialRunMeld2.add(p1PotentialRunMeld.get(0));
        //p1PotentialRunMeld2.add(p1PotentialRunMeld.get(1));
        //RunMeld p1RunMeld = Meld.buildRunMeld(p1PotentialRunMeld);

        //Sort set meld based on rank
        List<Card> p1PotentialSetMeld = new ArrayList(cardsNotInMeld);
        Collections.sort(p1PotentialSetMeld, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getRankValue() - o2.getRankValue();
            }
        });
        //iterator i - if the 4th element is the same, you can assume that the the cards in the middle are the same because
        //they are sorted
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
            } else if (canCheck3 && p1PotentialSetMeld.get(i).getRank().equals(p1PotentialSetMeld.get(i + 2).getRank())) {
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
        System.out.println("");
    }

    //Retrieving all cards in the game and setting up the deck
    public static void initializeDeck() {
        deck = new ArrayList<Card>();
        deck.addAll(Card.getAllCards());
        Collections.sort(deck);
    }

    //Shuffling the deck
    public static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    //Dealing ten cards to each player
    public static void dealDeck() {
        for (int i = 0; i < 10; i++) {
            if (i % 1 == 0) {
                //beginning of the array is the top of the deck
                p1.getHand().add(deck.remove(0));
            } else {
                p2.getHand().add(deck.remove(0));
            }
        }
        //May need to sort differently, this sort works for Set Melds only
        //Collections.sort(p1.getHand());
        //Collections.sort(p2.getHand());
        p1.receiveInitialHand(p1.getHand());
        p2.receiveInitialHand(p2.getHand());
    }

    //Assume that p1 is always the starting player
    public static void playFirstRound() {
        //Run runMeld with p1's hand & get deadwood points
        Meld playerRunMeld = Meld.buildRunMeld(p1.getHand());
        int p1DwpRun = GameEngine.calculateDeadwoodPoints(playerRunMeld, p1.getHand());
        //Run setMeld with p1's hand & get deadwood points
        Meld playerSetMeld = Meld.buildSetMeld(p1.getHand());
        int p1DwpSet = GameEngine.calculateDeadwoodPoints(playerSetMeld, p1.getHand());
        //get the first card from the discard pile
        Card topCard = discardPile.get(0);
        //compare 2 deadwood points from above & keep the meld with lower deadwood points
        if (playerRunMeld != null || playerSetMeld != null) {
            if (p1DwpRun > p1DwpSet) {
                playerRunMeld = null;
            } else {
                playerSetMeld = null;
            }
            if (playerRunMeld.canAppendCard(topCard) || playerSetMeld.canAppendCard(topCard)) {

            }
        }
        p1.willTakeTopDiscard(topCard);
    }

    public static void playNormalRound() {

    }

    public static int calculateDeadwoodPoints(Meld playerMeld, List<Card> playerHand) {
        if (playerMeld != null) {
            for (Card cardObj : playerHand) {
                if (!playerMeld.containsCard(cardObj)) {
                    deadwoodPoints += cardObj.getPointValue();
                }
            }
        } else {
            for (Card cardObj : playerHand) {
                deadwoodPoints += cardObj.getPointValue();
            }
        }
        return deadwoodPoints;
    }




}
