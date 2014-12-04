package com.geeksong.russianrailroadsscorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

public class CalculatorActivity extends Activity {
    private int currentScore = 0;
    private boolean isDoubled = false;
    private boolean isUsingRevaluationMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        TextView scoreValue = (TextView) findViewById(R.id.scoreValue);
        scoreValue.setText(Integer.toString(currentScore));
    }

    private void addToDisplayedScore(int add) {
        currentScore += add;
        updateScoreDisplay();
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

    public void doublerToggleButton_Click(View doublerToggleButton) {
        setIsDoubled(!isDoubled);
    }

    public void revaluationToggleButton_Click(View revaluationToggleButton) {
        setIsUsingRevaluationMarker(!isUsingRevaluationMarker);
    }

    public void greyButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getGreyScore());
    }

    public void brownButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getBrownScore());
    }

    public void beigeButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getBeigeScore());
    }

    public void whiteButton_Click(View view) {
        addToDisplayedScore(ScoreManager.getInstance().getWhiteScore());
    }

    public void oneButton_Click(View view) {
        addToDisplayedScore(1);
    }

    public void twoButton_Click(View view) {
        addToDisplayedScore(2);
    }

    public void threeButton_Click(View view) {
        addToDisplayedScore(3);
    }

    public void fourButton_Click(View view) {
        addToDisplayedScore(4);
    }

    public void fiveButton_Click(View view) {
        addToDisplayedScore(5);
    }

    public void tenButton_Click(View view) {
        addToDisplayedScore(10);
    }

    public void clearButton_Click(View view) {
        currentScore = 0;
        setIsDoubled(false);
        setIsUsingRevaluationMarker(false);
        updateScoreDisplay();
    }

    public void anyButton_Click(View view) {
        final NumberPicker numberPicker = (NumberPicker) getLayoutInflater().inflate(R.layout.number_picker_dialog_layout, null);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        new AlertDialog.Builder(this)
            .setTitle("Amount To Add:")
            .setView(numberPicker)
            .setPositiveButton(R.string.dialog_ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            addToDisplayedScore(numberPicker.getValue());
                        }
                    })
            .setNegativeButton(R.string.dialog_cancel, null)
            .show();
    }

    public void industryButton_Click(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Industry Amount:")
                .setItems(R.array.industry_amounts, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String[] industryAmounts = getResources().getStringArray(R.array.industry_amounts);
                        int selectedAmount = Integer.parseInt(industryAmounts[which]);
                        addToDisplayedScore(selectedAmount);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}