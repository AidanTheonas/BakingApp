package com.example.aidan.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    private String measure,ingredientName,quantity;

    public Ingredients(String quantity,String measure,String ingredientName){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredientName = ingredientName;
    }

    private Ingredients(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredientName = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getMeasure() {
        return measure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredientName);
    }
}
