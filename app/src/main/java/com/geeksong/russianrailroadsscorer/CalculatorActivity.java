package com.geeksong.russianrailroadsscorer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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

    private int getDrawableIdForStartCard(StartBonusCards card) {
        switch(card) {
            case BlackTrack: return R.drawable.black_advance;
            case Coal: return R.drawable.coal;
            case Coin: return R.drawable.coin;
            case Doubler: return R.drawable.doubler_on;
            case Industry: return R.drawable.industry;
        }
        return 0;
    }

    public void randomizeStartBonusCards_Click(View view) {
        ArrayList<StartBonusCards> startBonusCards = new ArrayList<StartBonusCards>(Arrays.asList(StartBonusCards.values()));
        Random random = new Random();

        int firstCardIndex = random.nextInt(startBonusCards.size());
        StartBonusCards firstCard = startBonusCards.get(firstCardIndex);
        startBonusCards.remove(firstCardIndex);

        int secondCardIndex = random.nextInt(startBonusCards.size());
        StartBonusCards secondCard = startBonusCards.get(secondCardIndex);
        startBonusCards.remove(secondCardIndex);

        LayoutInflater factory = LayoutInflater.from(CalculatorActivity.this);
        final View dialogContent = factory.inflate(R.layout.random_start_bonus_cards_dialog, null);

        ((ImageView) dialogContent.findViewById(R.id.firstCard)).setImageResource(getDrawableIdForStartCard(firstCard));
        ((ImageView) dialogContent.findViewById(R.id.secondCard)).setImageResource(getDrawableIdForStartCard(secondCard));

        if(firstCard == StartBonusCards.Coal || secondCard == StartBonusCards.Coal) {
            // draw a third
            int thirdCardIndex = random.nextInt(startBonusCards.size());
            StartBonusCards thirdCard = startBonusCards.get(thirdCardIndex);
            dialogContent.findViewById(R.id.ifNotCoal).setVisibility(View.VISIBLE);
            ((ImageView) dialogContent.findViewById(R.id.thirdCard)).setImageResource(getDrawableIdForStartCard(thirdCard));
        }

        AlertDialog drawnCardsDialog = new AlertDialog.Builder(CalculatorActivity.this).create();
        drawnCardsDialog.setView(dialogContent);
        drawnCardsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
                });
        drawnCardsDialog.show();
    }
}