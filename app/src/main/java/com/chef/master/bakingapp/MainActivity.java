package com.chef.master.bakingapp;

import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.chef.master.bakingapp.fragments.RecipeFragment;

public class MainActivity extends AppCompatActivity {
    Parcelable mListState;
    LinearLayoutManager mLayoutManager;
    String LIST_STATE_KEY="RV";
    int mCurCheckPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goRecipes();
    }
    public void goRecipes(){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_fragments, RecipeFragment.newInstance(), RecipeFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
//        super.onBackPressed();

    }

}
