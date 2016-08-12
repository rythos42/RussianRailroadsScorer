package com.geeksong.russianrailroadsscorer;

public class DrawnCards {
    private StartBonusCards[] drawnCards;

    public DrawnCards(StartBonusCards[] drawnCards) {
        this.drawnCards = drawnCards;
    }

    public StartBonusCards firstCard() {
        return this.drawnCards[0];
    }

    public StartBonusCards secondCard() {
        return this.drawnCards[1];
    }

    public boolean hasThirdCard() {
        return this.drawnCards.length == 3;
    }

    public StartBonusCards thirdCard() {
        return this.drawnCards[2];
    }
}
