package com.geeksong.russianrailroadsscorer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;

public class CalculatorActivity extends Activity {
    private int currentScore = 0;
    private boolean isDoubled = false;
    private boolean isUsingRevaluationMarker = false;
    private boolean isUsingGermanRevaluationMarker = false;
    private String refreshStartCardsAction = "refresh-start-cards";
    private ArrayList<Integer> history = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        updateScoreDisplay();

        AddedActionFactory.setLayoutInflater(getLayoutInflater());
        AddedActionFactory.setResources(getResources());

        if(getIntent().getAction().equals(refreshStartCardsAction))
            showRandomStartBonusCards();
    }

    private void updateScoreDisplay() {
        TextView scoreValue = findViewById(R.id.scoreValue);
        scoreValue.setText(String.format("%d", currentScore));
    }

    private void addToDisplayedScore(int add, ScoreType type) {
        currentScore += add;
        updateScoreDisplay();
        addToHistory(add, type);
    }

    private void addToHistory(int add, ScoreType type) {
        View addedAction = AddedActionFactory.create(add, type, isDoubled, isUsingRevaluationMarker, isUsingGermanRevaluationMarker);

        final LinearLayout previousActionsLayout = findViewById(R.id.previousActions);
        previousActionsLayout.addView(addedAction);

        final HorizontalScrollView historyScroll = findViewById(R.id.historyScroll);
        historyScroll.post(new Runnable() {
            public void run() {
                historyScroll.smoothScrollTo(previousActionsLayout.getRight(), 0);
            }
        });

        history.add(add);

        setUndoButtonEnabled(true);
    }

    private void removeLastScore() {
        int indexOfItemToRemove = history.size() - 1;
        if(indexOfItemToRemove < 0)
            return;

        int lastScore = history.get(indexOfItemToRemove);
        history.remove(indexOfItemToRemove);
        currentScore -= lastScore;
        updateScoreDisplay();

        final LinearLayout previousActionsLayout = findViewById(R.id.previousActions);
        previousActionsLayout.removeViewAt(indexOfItemToRemove);

        setUndoButtonEnabled(history.size() > 0);
    }

    private void setUndoButtonEnabled(boolean enabled) {
        ImageButton undoButton = findViewById(R.id.undoButton);

        undoButton.setEnabled(enabled);
        undoButton.setImageResource(enabled ? R.drawable.ic_backspace_black_36dp : R.drawable.ic_backspace_grey600_36dp);
    }

    private void setIsDoubled(boolean isDoubled) {
        this.isDoubled = isDoubled;

        ImageButton button = findViewById(R.id.doublerToggleButton);
        Drawable doublerImage = getResources().getDrawable(isDoubled? R.drawable.doubler_on : R.drawable.doubler_off);
        button.setImageDrawable(doublerImage);

        ScoreManager.getInstance().setIsDoubled(isDoubled);
    }

    private void setIsUsingRevaluationMarker(boolean isUsingRevaluationMarker) {
        this.isUsingRevaluationMarker = isUsingRevaluationMarker;

        ImageButton button = findViewById(R.id.revaluationToggleButton);
        Drawable doublerImage = getResources().getDrawable(isUsingRevaluationMarker? R.drawable.revaluation_on : R.drawable.revaluation_off);
        button.setImageDrawable(doublerImage);

        ScoreManager.getInstance().setIsUsingRevaluationMarker(isUsingRevaluationMarker);
    }

    private void setIsUsingGermanRevaluationMarker(boolean isUsingGermanRevaluationMarker) {
        this.isUsingGermanRevaluationMarker = isUsingGermanRevaluationMarker;

        ImageButton button = findViewById(R.id.germanRevaluationToggleButton);
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
        history.clear();
        setIsDoubled(false);
        setIsUsingRevaluationMarker(false);
        setIsUsingGermanRevaluationMarker(false);
        setUndoButtonEnabled(false);
        updateScoreDisplay();

        LinearLayout previousActionsLayout = findViewById(R.id.previousActions);
        previousActionsLayout.removeAllViews();
    }

    private void showNumberPickerDialog(final boolean addToDisplayedScore, final int titleId) {
        // https://possiblemobile.com/2013/05/layout-inflation-as-intended/
        @SuppressLint("InflateParams")
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

    private void showStartBonusCardsDialog(DrawnCards drawnCards) {
        LayoutInflater factory = LayoutInflater.from(CalculatorActivity.this);
        // https://possiblemobile.com/2013/05/layout-inflation-as-intended/
        @SuppressLint("InflateParams")
        final View dialogContent = factory.inflate(R.layout.random_start_bonus_cards_dialog, null);
        ((ImageView) dialogContent.findViewById(R.id.firstCard)).setImageResource(getDrawableIdForStartCard(drawnCards.firstCard()));
        ((ImageView) dialogContent.findViewById(R.id.secondCard)).setImageResource(getDrawableIdForStartCard(drawnCards.secondCard()));

        if(drawnCards.hasThirdCard()) {
            dialogContent.findViewById(R.id.ifNotCoal).setVisibility(View.VISIBLE);
            ((ImageView) dialogContent.findViewById(R.id.thirdCard)).setImageResource(getDrawableIdForStartCard(drawnCards.thirdCard()));
        } else {
            dialogContent.findViewById(R.id.ifNotCoal).setVisibility(View.GONE);
        }

        AlertDialog drawnCardsDialog = new AlertDialog.Builder(CalculatorActivity.this).create();
        drawnCardsDialog.setView(dialogContent);
        drawnCardsDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { dialog.dismiss(); }
        });
        drawnCardsDialog.show();
    }

    private void showStartBonusCardsNotification(DrawnCards drawnCards) {
        Intent openIntent = new Intent(CalculatorActivity.this, CalculatorActivity.class);
        openIntent.setAction(refreshStartCardsAction);
        PendingIntent openPendingIntent = PendingIntent.getActivity(this, 0, openIntent, 0);

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.random_start_bonus_cards_notification);
        notificationView.setImageViewResource(R.id.firstCard, getDrawableIdForStartCard(drawnCards.firstCard()));
        notificationView.setImageViewResource(R.id.secondCard, getDrawableIdForStartCard(drawnCards.secondCard()));
        if(drawnCards.hasThirdCard()) {
            notificationView.setViewVisibility(R.id.ifNotCoal, View.VISIBLE);
            notificationView.setImageViewResource(R.id.thirdCard, getDrawableIdForStartCard(drawnCards.thirdCard()));
        } else {
            notificationView.setViewVisibility(R.id.ifNotCoal, View.GONE);
        }

        String channelId = "notificationChannel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.notification_channel_name);
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.white)
            .setContentIntent(openPendingIntent)
            .setContent(notificationView);

        int notificationId = 1;
        notificationManager.notify(notificationId, notification.build());
    }

    public void showRandomStartBonusCards() {
        DrawnCards drawnCards = StartBonusCards.drawTwo();
        showStartBonusCardsDialog(drawnCards);
        showStartBonusCardsNotification(drawnCards);
    }

    public void randomizeStartBonusCards_Click(View view) {
        showRandomStartBonusCards();
    }

    public void undoButton_Click(View view) {
        removeLastScore();
    }
}