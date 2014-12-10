package com.geeksong.russianrailroadsscorer;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AddedActionFactory {
    private static LayoutInflater inflater;
    private static Resources resources;

    public static void setLayoutInflater(LayoutInflater newInflater) {
        inflater = newInflater;
    }

    public static void setResources(Resources newResources) {
        resources = newResources;
    }

    private static int getBaseTrackId(ScoreType type) {
        switch(type) {
            case GreyTrack:
                return R.drawable.grey;
            case BrownTrack:
                return R.drawable.brown;
            case BeigeTrack:
                return R.drawable.beige;
            case WhiteTrack:
                return R.drawable.white;
        }
        return -1;
    }

    private static Drawable createTrackDrawable(ScoreType type, boolean isDoubled, boolean usingRevaluationMarker) {
        ArrayList<Drawable> drawableList = new ArrayList<Drawable>();
        int trackDrawableId = getBaseTrackId(type);
        BitmapDrawable trackDrawable = (BitmapDrawable) resources.getDrawable(trackDrawableId);
        trackDrawable.setGravity(Gravity.CENTER);
        drawableList.add(trackDrawable);

        int doublerWidth = 0;
        int revaluationIndex = 0;
        if(isDoubled) {
            BitmapDrawable doublerSmall = (BitmapDrawable) resources.getDrawable(R.drawable.doubler_small);
            doublerSmall.setGravity(Gravity.LEFT | Gravity.TOP);
            drawableList.add(doublerSmall);
            doublerWidth = doublerSmall.getIntrinsicWidth();
        }

        if(usingRevaluationMarker) {
            BitmapDrawable revaluationSmall = (BitmapDrawable) resources.getDrawable(R.drawable.revaluation_small);
            revaluationSmall.setGravity(Gravity.LEFT|Gravity.TOP);
            revaluationIndex = drawableList.size();
            drawableList.add(revaluationSmall);
        }

        LayerDrawable layerDrawable = new LayerDrawable(drawableList.toArray(new Drawable[drawableList.size()]));

        // Move the revaluation marker over if both are used.
        if(isDoubled && usingRevaluationMarker)
            layerDrawable.setLayerInset(revaluationIndex, doublerWidth, 0, 0, 0);

        return layerDrawable;
    }

    public static View create(int add, ScoreType type, boolean isDoubled, boolean usingRevaluationMarker) {
        final View addedAction = inflater.inflate(R.layout.added_action, null);

        TextView plusMinusText = (TextView) addedAction.findViewById(R.id.plusMinusText);
        plusMinusText.setText(add < 0 ? R.string.minus_sign : R.string.plus_sign);
        add = Math.abs(add);

        TextView numeric = (TextView) addedAction.findViewById(R.id.numericText);
        ImageView image = (ImageView) addedAction.findViewById(R.id.image);

        switch(type) {
            case Numeric:
                image.setVisibility(View.INVISIBLE);
                numeric.setText(Integer.toString(add));
                break;

            case GreyTrack:
            case BrownTrack:
            case BeigeTrack:
            case WhiteTrack:
                image.setImageDrawable(createTrackDrawable(type, isDoubled, usingRevaluationMarker));
                numeric.setVisibility(View.INVISIBLE);
                break;

            case Industry:
                image.setImageResource(R.drawable.industry);
                numeric.setText(Integer.toString(add));
                numeric.setPadding(4, 0, 0, 0);
                break;
        }

        return addedAction;
    }
}
