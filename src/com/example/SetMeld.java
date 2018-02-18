package com.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A com.example.SetMeld represents a set of three or more cards that are of the same rank but different suit.
 * {Ace of Spades, Ace of Diamonds, Ace of Clubs} represents a valid com.example.SetMeld
 * {2 of Diamonds, 2 of Clubs} is not a valid com.example.SetMeld
 * {3 of Clubs, 3 of Spades, 3 of Hearts, 4 of Diamonds} is not a valid com.example.SetMeld
 */
public class SetMeld extends Meld {
    public static final int MIN_CARDS = 3;
    public static final int MAX_CARDS = 4;

    private Set<com.example.Card> cardsInMeld;

    protected SetMeld(Collection<com.example.Card> initialCards) {
        super();

        cardsInMeld = new HashSet<com.example.Card>(initialCards);
    }

    protected SetMeld(com.example.Card[] initialCards) {
        this(Arrays.asList(initialCards));
    }

    @Override
    public boolean containsCard(com.example.Card cardToCheck) {
        return cardsInMeld.contains(cardToCheck);
    }

    @Override
    public boolean canAppendCard(com.example.Card newCard) {
        if (cardsInMeld.size() >= MAX_CARDS || cardsInMeld.contains(newCard)) {
            return false;
        }

        com.example.Card firstCard = cardsInMeld.iterator().next();
        return (newCard.getRank() == firstCard.getRank());
    }

    @Override
    public void appendCard(com.example.Card newCard) {
        if (!canAppendCard(newCard)) {
            throw new IllegalMeldModificationException();
        }

        cardsInMeld.add(newCard);
    }

    @Override
    public boolean canRemoveCard(com.example.Card cardToRemove) {
        if (cardsInMeld.size() <= MIN_CARDS || !cardsInMeld.contains(cardToRemove)) {
            return false;
        }

        return true;
    }

    @Override
    public void removeCard(com.example.Card cardToRemove) {
        if (!canRemoveCard(cardToRemove)) {
            throw new IllegalMeldModificationException();
        }

        cardsInMeld.remove(cardToRemove);
    }

    @Override
    public com.example.Card[] getCards() {
        com.example.Card[] arrayToReturn = new com.example.Card[cardsInMeld.size()];
        return cardsInMeld.toArray(arrayToReturn);
    }
}
