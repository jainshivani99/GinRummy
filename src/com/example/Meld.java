package com.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public abstract class Meld {

    protected Meld() {}

    /**
     * Checks whether or not this com.example.Meld contains the given card.
     *
     * @param cardToCheck The card to check that this com.example.Meld contains.
     * @return True if the com.example.Meld contains the card, false otherwise
     */
    public abstract boolean containsCard(com.example.Card cardToCheck);

    /**
     * Checks whether or not a card can be added to this com.example.Meld
     *
     * @param newCard The card to check if it can be added.
     * @return True if the card can be added to the meld, false otherwise
     */
    public abstract boolean canAppendCard(com.example.Card newCard);

    /**
     * Adds a card to this com.example.Meld.
     *
     * @param newCard The card to be added
     * @throws IllegalMeldModificationException if the new card cannot be added to the com.example.Meld
     */
    public abstract void appendCard(com.example.Card newCard);

    /**
     * Checks whether a given card can be removed from the collection and still have a com.example.Meld.
     *
     * @param cardToRemove The card check if it can be removed
     * @return True if the card can be removed while still keeping this com.example.Meld as a single valid com.example.Meld, false otherwise
     */
    public abstract boolean canRemoveCard(com.example.Card cardToRemove);

    /**
     * Attempts to remove a card from this com.example.Meld.
     *
     * @param cardToRemove The card to be removed
     * @throws IllegalMeldModificationException if the new card cannot be removed from the com.example.Meld without leaving it as
     *  a single valid com.example.Meld
     */
    public abstract void removeCard(com.example.Card cardToRemove);

    /**
     * Gets a sorted array of all the cards contained in this com.example.Meld
     *
     * @return A sorted Card[] with all cards in the com.example.Meld
     */
    public abstract com.example.Card[] getCards();

    /**
     * Attempts to build a new com.example.SetMeld object with the given Collection of cards.
     *
     * @param initialCards The Collection of cards to be formed into a meld
     * @return A com.example.Meld of cards if the initial cards were valid, null if otherwise
     */
    public static SetMeld buildSetMeld(Collection<com.example.Card> initialCards) {
        if (initialCards == null || initialCards.size() < SetMeld.MIN_CARDS ||
                initialCards.size() > SetMeld.MAX_CARDS) {
            return null;
        }

        Iterator<com.example.Card> cardsIterator = initialCards.iterator();
        com.example.Card.CardRank setMeldRank = cardsIterator.next().getRank();

        while (cardsIterator.hasNext()) {
            if (cardsIterator.next().getRank() != setMeldRank) {
                // Collection contains a card which does not match the rank of all other cards
                return null;
            }
        }

        return new SetMeld(initialCards);
    }

    /**
     * Attempts to build a new com.example.SetMeld object with the given array of cards.
     *
     * @param initialCards The array of cards to be formed into a meld
     * @return A com.example.Meld of cards if the initial cards were valid, null if otherwise
     */
    public static SetMeld buildSetMeld(com.example.Card[] initialCards) {
        if (initialCards == null) {
            return null;
        }

        return buildSetMeld(Arrays.asList(initialCards));
    }

    /**
     * Attempts to build a new com.example.Meld object with the given Collection of cards.
     *
     * @param initialCards The Collection of cards to be formed into a meld
     * @return A com.example.Meld of cards if the initial cards were valid, null if otherwise
     */
    public static RunMeld buildRunMeld(Collection<com.example.Card> initialCards) {
        if (initialCards == null) {
            return null;
        }

        // Turn Collection into an array so it can be indexed in a manner more conducive to RunMelds
        com.example.Card[] initialCardsAsArray = new com.example.Card[initialCards.size()];
        initialCardsAsArray = initialCards.toArray(initialCardsAsArray);
        return buildRunMeld(initialCardsAsArray);
    }

    /**
     * Attempts to build a new com.example.Meld object with the given array of cards.
     *
     * @param initialCards The array of cards to be formed into a meld
     * @return A com.example.Meld of cards if the initial cards were valid, null if otherwise
     */
    public static RunMeld buildRunMeld(com.example.Card[] initialCards) {
        if (initialCards == null || initialCards.length < RunMeld.MIN_CARDS ||
                initialCards.length > RunMeld.MAX_CARDS) {
            return null;
        }

        Arrays.sort(initialCards);

        int previousCardRankValue = initialCards[0].getRankValue();
        com.example.Card.CardSuit sameSuit = initialCards[0].getSuit();

        for (int i = 1; i < initialCards.length; i++) {
            com.example.Card currentCard = initialCards[i];
            if (currentCard.getRankValue() != (previousCardRankValue + 1) || currentCard.getSuit() != sameSuit){
                return null;
            }

            previousCardRankValue = currentCard.getRankValue();
        }

        return new RunMeld(initialCards);
    }
}

