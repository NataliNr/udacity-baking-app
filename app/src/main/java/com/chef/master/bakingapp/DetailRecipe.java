package com.chef.master.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.chef.master.bakingapp.api.model.Ingredient;
import com.chef.master.bakingapp.api.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class DetailRecipe  implements Parcelable {
    ArrayList<Step> steps=new ArrayList<>();
    ArrayList<Ingredient> ingredients=new ArrayList<>();

    public static final Creator<DetailRecipe> CREATOR = new Creator<DetailRecipe>() {
        @Override
        @SuppressWarnings("unchecked")
        public DetailRecipe createFromParcel(Parcel in) {
            DetailRecipe movie = new DetailRecipe();
            movie.setIngredients(in.readArrayList(Ingredient.class.getClassLoader()));
            movie.setSteps(in.readArrayList(Step.class.getClassLoader()));
            return movie;
        }

        @Override
        public DetailRecipe[] newArray(int size) {
            return new DetailRecipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(steps);
        dest.writeList(ingredients);
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}