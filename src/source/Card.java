package src.source;

public class Card {
    private String owner;
    private final CardSuits cardSuit;
    private final CardDenominations denomination;

    public Card(CardSuits cardSuit, CardDenominations denomination) {
        this.cardSuit = cardSuit;
        this.denomination = denomination;
    }

    public CardSuits getCardSuit() {
        return cardSuit;
    }

    public CardDenominations getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return denomination.getDescription() + " " + cardSuit.getDescription() + " (" + owner + ")";
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
