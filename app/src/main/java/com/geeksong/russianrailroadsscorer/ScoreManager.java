package com.geeksong.russianrailroadsscorer;

public class ScoreManager implements IScoreValues {
    private static ScoreManager instance = new ScoreManager();

    public static ScoreManager getInstance() {
        return instance;
    }

    private ScoreManager() {
        setIsUsingRevaluationMarker(false);
        setIsDoubled(false);
    }

    private IScoreValues currentScoreValues;
    private int multiplier;

    public void setIsUsingRevaluationMarker(boolean usingRevaluationMarker) {
        currentScoreValues = usingRevaluationMarker ? new RevaluationScoreValues() : new BaseScoreValues();
    }

    public void setIsUsingGermanRevaluationMarker(boolean usingGermanRevaluationMarker) {
        currentScoreValues = usingGermanRevaluationMarker ? new GermanRevaluationScoreValues() : new BaseScoreValues();
    }

    public void setIsDoubled(boolean isDoubled) {
        multiplier = isDoubled ? 2 : 1;
    }

    public int getGreyScore() {
        return currentScoreValues.getGreyScore() * multiplier;
    }

    public int getBrownScore() {
        return currentScoreValues.getBrownScore() * multiplier;
    }

    public int getBeigeScore() {
        return currentScoreValues.getBeigeScore() * multiplier;
    }

    public int getWhiteScore() {
        return currentScoreValues.getWhiteScore() * multiplier;
    }
}
