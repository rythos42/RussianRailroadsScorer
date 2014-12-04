package com.geeksong.russianrailroadsscorer;

public class BaseScoreValues implements IScoreValues {
    public int getGreyScore() { return 1; }

    public int getBrownScore() {
        return 2;
    }

    public int getBeigeScore() {
        return 4;
    }

    public int getWhiteScore() {
        return 7;
    }

}
