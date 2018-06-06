package com.example.aidan.bakingapp.Helpers;

import android.content.Context;

import com.example.aidan.bakingapp.R;

import java.util.Date;

public class Helpers {

    public static int getMeasurementIcon(String measure) {
        int measurementDrawable = R.drawable.ic_ingredient;
        switch (measure) {
            case "CUP":
                measurementDrawable = R.drawable.ic_cup;
                break;

            case "TBLSP":
                measurementDrawable = R.drawable.ic_tblsp;
                break;

            case "TSP":
                measurementDrawable = R.drawable.ic_tsp;
                break;

            case "K":
                measurementDrawable = R.drawable.ic_kg;
                break;

            case "G":
                measurementDrawable = R.drawable.ic_gram;
                break;
        }
        return measurementDrawable;
    }

    public static String getFullMeasurementString(String measure, Context context, int measureCount) {
        String measureString = "";
        switch (measure) {
            case "CUP":
                if (measureCount > 1) {
                    measureString = context.getString(R.string.cups);
                } else {
                    measureString = context.getString(R.string.cup);
                }
                break;

            case "TBLSP":
                if (measureCount > 1) {
                    measureString = context.getString(R.string.table_spoons);
                } else {
                    measureString = context.getString(R.string.table_spoon);
                }
                break;

            case "TSP":
                if (measureCount > 1) {
                    measureString = context.getString(R.string.tea_spoons);
                } else {
                    measureString = context.getString(R.string.tea_spoon);
                }
                break;

            case "K":
                if (measureCount > 1) {
                    measureString = context.getString(R.string.kilos);
                } else {
                    measureString = context.getString(R.string.kilo);
                }
                break;

            case "G":
                if (measureCount > 1) {
                    measureString = context.getString(R.string.grams);
                } else {
                    measureString = context.getString(R.string.gram);
                }
                break;
        }
        return measureString;
    }

    public static int getStepIcon(String videoURL) {
        int stepIcon = R.drawable.ic_no_video_placeholder;
        if (!videoURL.trim().equals("")) {
            stepIcon = R.drawable.ic_video_placeholder;
        }
        return stepIcon;
    }

    public static int getCurrentTimeAsInteger() {
        return (int) (new Date().getTime() / 1000);
    }
}
