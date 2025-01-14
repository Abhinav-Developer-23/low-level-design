package org.lowLevelDesign.LowLevelDesign.ATMSystem.hardware;


import lombok.Getter;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.model.Card;

@Getter
public class CardReader {
    private Card currentCard;

    public boolean insertCard(Card card) {
        if (card != null && card.isActive()) {
            this.currentCard = card;
            return true;
        }
        return false;
    }

    public Card ejectCard() {
        Card card = this.currentCard;
        this.currentCard = null;
        return card;
    }

}