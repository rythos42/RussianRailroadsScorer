package com.geeksong.russianrailroadsscorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CalculatorActivity extends Activity {
    private int currentScore = 0;
    private boolean isDoubled = false;
    private boolean isUsingRevaluationMarker = false;
    private boolean isUsingGermanRevaluationMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        updateScoreDisplay();

        AddedActionFactory.setLayoutInflater(getLayoutInflater());
        AddedActionFactory.setResources(getResources());
    }

    private void updateScoreDisplay() {
        TextView scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreValue.setText(Integer.toString(currentScore));
    }

    private void addToDisplayedScore(int add, ScoreType type) {
        currentScore += add;
        updateScoreDisplay();
        addToHistory(add, type);
    }

    private void addToHistory(int add, ScoreType type) {
        View addedAction = AddedActionFactory.create(add, type, isDoubled, isUsingRevaluationMarker, isUsingGermanRevaluationMarker);

        final LinearLayout previousActionsLayout = (LinearLayout) findViewById(R.id.previousActions);
        previousActionsLayout.addView(addedAction);

        final HorizontalScrollView historyScroll = (HorizontalScrollView) findViewById(R.id.historyScroll);
        historyScroll.post(new Runnable() {
            public void run() {
                historyScroll.smoothScrollTo(previousActionsLayout.getRight(), 0);
            }
        });
    }

    private void setIsDoubled(boolean isDoubled) {
        this.isDoubled = isDoubled;

        ImageButton button = (ImageButton)findViewById(R.id.doublerToggleButton);
        Drawable doublerImage = getResources().getDrawable(isDoubled? R.drawable.doubler_on : R.drawable.doubler_off);
        button.setImageDrawable(doublerImage);

        ScoreManager.getInstance().setIsDoubled(isDoubled);
    }

    private void setIsUsingRevaluationMarker(boolean isUsingRevaluationMarker) {
        this.isUsingRevaluationMarker = isUsingRevaluationMarker;

        ImageButton button = (ImageButton)findViewById(R.id.revaluationToggleButton);
        Drawable doublerImage = getResources().getDrawable(isUsingRevaluationMarker? R.drawable.revaluation_on : R.drawable.revaluation_off);
        button.setImageDrawable(doublerImage);

        ScoreManager.getInstance().setIsUsingRevaluationMarker(isUsingRevaluationMarker);
    }

    private void setIsUsingGermanRevaluationMarker(boolean isUsingGermanRevaluationMarker) {
        this.isUsingGermanRevaluationMarker = isUsingGermanRevaluationMarker;

        ImageButton button = (ImageButton)findViewById(R.id.germanRevaluationToggleButton);
        Drawable doublerImage = getResources().getDrawable(isUsingGermanRevaluationMarker? R.drawable.german_revaluation_on : R.drawable.german_revaluation_off);
        button.setImageDrawable(doublerImage);

        ScoreManager.getInstance().setIsUsingGermanRevaluationMarker(isUsingGermanRevaluationMarker);
    }

    public void doublerToggleButton_Click(View doublerToggleButton) {
        setIsDoubled(!isDoubled);
    }

    public void revaluationToggleButton_Click(View revaluationToggleButton) {
        // Turn off the other marker if it's on
        setIsUsingGermanRevaluationMarker(false);
        setIsUsingRevaluationMarker(!isUsingRevaluationMarker);
    }

    public void germanRevaluationToggleButton_Click(View revaluationToggleButton) {
        // Turn off the other marker if it's on
        setIsUsingRevaluationMarker(false);
        setIsUsingGermanRevaluationMarker(!isUsingGermanRevaluationMarker);
    }

    public void greyButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getGreyScore(), ScoreType.GreyTrack);
    }

    public void brownButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getBrownScore(), ScoreType.BrownTrack);
    }

    public void beigeButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getBeigeScore(), ScoreType.BeigeTrack);
    }

    public void whiteButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getWhiteScore(), ScoreType.WhiteTrack);
    }

    public void oneButton_Click(View view) {
        addToDisplayedScore(1, ScoreType.Numeric);
    }

    public void twoButton_Click(View view) {
        addToDisplayedScore(2, ScoreType.Numeric);
    }

    public void threeButton_Click(View view) {
        addToDisplayedScore(3, ScoreType.Numeric);
    }

    public void fourButton_Click(View view) {
        addToDisplayedScore(4, ScoreType.Numeric);
    }

    public void fiveButton_Click(View view) {
        addToDisplayedScore(5, ScoreType.Numeric);
    }

    public void tenButton_Click(View view) {
        addToDisplayedScore(10, ScoreType.Numeric);
    }

    public void clearButton_Click(View view) {
        currentScore = 0;
        setIsDoubled(false);
        setIsUsingRevaluationMarker(false);
        setIsUsingGermanRevaluationMarker(false);
        updateScoreDisplay();

        LinearLayout previousActionsLayout = (LinearLayout) findViewById(R.id.previousActions);
        previousActionsLayout.removeAllViews();
    }

    private void showNumberPickerDialog(final boolean addToDisplayedScore, final int titleId) {
        final NumberPicker numberPicker = (NumberPicker) getLayoutInflater().inflate(R.layout.number_picker_dialog_layout, null);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setView(numberPicker)
                .setPositiveButton(R.string.dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(addToDisplayedScore)
                                    addToDisplayedScore(numberPicker.getValue(), ScoreType.Numeric);
                                else
                                    addToDisplayedScore(-1 * numberPicker.getValue(), ScoreType.Numeric);
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }

    public void plusButton_Click(View view) {
        showNumberPickerDialog(true, R.string.amount_to_add);
    }

    public void minusButton_Click(View view) {
        showNumberPickerDialog(false, R.string.amount_to_subtract);
    }

    public void industryButton_Click(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.industry_amount)
                .setItems(R.array.industry_amounts, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] industryAmounts = getResources().getStringArray(R.array.industry_amounts);
                        int selectedAmount = Integer.parseInt(industryAmounts[which]);
                        addToDisplayedScore(selectedAmount, ScoreType.Industry);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}