package com.chef.master.bakingapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class Ingredient implements Parcelable{

    @SerializedName("quantity")
    @Expose
    private float quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            Ingredient movie = new Ingredient();
            movie.setQuantity(in.readFloat());
            movie.setMeasure(in.readString());
            movie.setIngredient(in.readString());
            return movie;
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }


}