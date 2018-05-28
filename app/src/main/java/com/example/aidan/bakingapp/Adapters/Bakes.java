package com.example.aidan.bakingapp.Adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class Bakes implements Parcelable {
    private String bakeName;
    private int cakeId, totalIngredients, totalSteps;

    public Bakes() {
    }

    public Bakes(int cakeId, String bakeName, int totalIngredients, int totalSteps) {
        this.cakeId = cakeId;
        this.bakeName = bakeName;
        this.totalIngredients = totalIngredients;
        this.totalSteps = totalSteps;
    }

    private Bakes(Parcel in) {
        bakeName = in.readString();
        cakeId = in.readInt();
        totalIngredients = in.readInt();
        totalSteps = in.readInt();
    }

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

    public int getCakeId() {
        return cakeId;
    }

    public int getTotalIngredients() {
        return totalIngredients;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public String getBakeName() {
        return bakeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bakeName);
        dest.writeInt(cakeId);
        dest.writeInt(totalIngredients);
        dest.writeInt(totalSteps);
    }
}
