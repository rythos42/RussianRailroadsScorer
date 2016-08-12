package com.geeksong.russianrailroadsscorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public enum StartBonusCards {
    Coin,
    BlackTrack,
    Industry,
    Doubler,
    Coal;

    public static DrawnCards drawTwo() {
        ArrayList<StartBonusCards> startBonusCards = new ArrayList<>(Arrays.asList(StartBonusCards.values()));
        Random random = new Random();

        int firstCardIndex = random.nextInt(startBonusCards.size());
        StartBonusCards firstCard = startBonusCards.get(firstCardIndex);
        startBonusCards.remove(firstCardIndex);

        int secondCardIndex = random.nextInt(startBonusCards.size());
        StartBonusCards secondCard = startBonusCards.get(secondCardIndex);
        startBonusCards.remove(secondCardIndex);

        if(firstCard == StartBonusCards.Coal || secondCard == StartBonusCards.Coal) {
            // draw a third
            int thirdCardIndex = random.nextInt(startBonusCards.size());
            StartBonusCards thirdCard = startBonusCards.get(thirdCardIndex);

            StartBonusCards[] draw = { firstCard, secondCard, thirdCard };
            return new DrawnCards(draw);
        } else {
            StartBonusCards[] draw = { firstCard, secondCard };
            return new DrawnCards(draw);
        }
    }
}
