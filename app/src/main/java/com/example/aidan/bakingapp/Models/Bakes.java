package com.example.aidan.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Bakes implements Parcelable {
    public static final Creator<Bakes> CREATOR = new Creator<Bakes>() {
        @Override
        public Bakes createFromParcel(Parcel in) {
            return new Bakes(in);
        }

        @Override
        public Bakes[] newArray(int size) {
            return new Bakes[size];
        }
    };
    private String bakeName, bakeImage;
    private int bakeId, servings, totalSteps;
    private List<Steps> stepsList;
    private List<Ingredients> ingredientsList;

    public Bakes(int bakeId, String bakeName, String bakeImage, int servings, int totalSteps, List<Steps> stepsList, List<Ingredients> ingredientsList) {
        this.bakeId = bakeId;
        this.bakeName = bakeName;
        this.bakeImage = bakeImage;
        this.servings = servings;
        this.totalSteps = totalSteps;
        this.stepsList = stepsList;
        this.ingredientsList = ingredientsList;
    }

    private Bakes(Parcel in) {
        bakeName = in.readString();
        bakeImage = in.readString();
        bakeId = in.readInt();
        servings = in.readInt();
        totalSteps = in.readInt();
        stepsList = in.createTypedArrayList(Steps.CREATOR);
        ingredientsList = in.createTypedArrayList(Ingredients.CREATOR);
    }

    public int getBakeId() {
        return bakeId;
    }

    public int getServings() {
        return servings;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public String getBakeName() {
        return bakeName;
    }

    public List<Ingredients> getIngredientsList() {
        return ingredientsList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(bakeName);
        dest.writeString(bakeImage);
        dest.writeInt(bakeId);
        dest.writeInt(servings);
        dest.writeInt(totalSteps);
        dest.writeTypedList(stepsList);
        dest.writeTypedList(ingredientsList);
    }

}
