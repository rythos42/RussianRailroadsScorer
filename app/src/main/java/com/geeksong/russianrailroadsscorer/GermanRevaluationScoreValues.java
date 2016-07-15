package com.geeksong.russianrailroadsscorer;

public class GermanRevaluationScoreValues implements IScoreValues {
    @Override
    public int getGreyScore() {
        return 2;
    }

    @Override
    public int getBrownScore() {
        return 3;
    }

    @Override
    public int getBeigeScore() {
        return 7;
    }

    @Override
    public int getWhiteScore() {
        return 8;
    }
}
