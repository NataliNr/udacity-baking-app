package com.chef.master.bakingapp.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chef.master.bakingapp.DetailRecipe;
import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.BakingAPI;
import com.chef.master.bakingapp.api.model.BakingResult;
import com.chef.master.bakingapp.fragments.adapter.RecipeAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class RecipeFragment extends Fragment {
    @BindView(R.id.recipe_list)
    RecyclerView recyclerView;
    GridLayoutManager mLayoutManager;
    RecipeAdapter imageAdapter;
    DetailRecipe movie;
    int mCurCheckPosition;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }

    public static RecipeFragment newInstance() {

        Bundle args = new Bundle();

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
//        int rotation = display.getRotation();
        if (widthPixels < 600) {
            mLayoutManager = new GridLayoutManager(getActivity(), 1);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        bakingRequest(createAPI().getBakingObjects());

    }

    private BakingAPI createAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BakingAPI.BASE_URL)
                .build();

        return retrofit.create(BakingAPI.class);
    }

    public void bakingRequest(Observable<List<BakingResult>> bakingResultObservable) {

        bakingResultObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responses -> {
//                    ArrayList<BakingResult> recipes = new ArrayList<>();
//                    for (BakingResult bakingResult: responses) {
//
//                        images.add("http://image.tmdb.org/t/p/w185/" + img.posterPath);
//
//                    }
                    imageAdapter = new RecipeAdapter(getActivity(), responses);
                    imageAdapter.setClickListener((view, position) -> {
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        movie = new DetailRecipe();
                        movie.setIngredients(responses.get(position).getIngredients());
                        movie.setSteps(responses.get(position).getSteps());
                        fragmentTransaction.replace(R.id.container_fragments, RecipeDetailFragment.newInstance(movie, responses.get(position).getName()), RecipeDetailFragment.class.getSimpleName());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    });
                    recyclerView.setAdapter(imageAdapter);

//                    recyclerView.setOnItemClickListener((parent, v, position, id) -> {
//                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                        movie = new Movie();
//                        movie.setMovie_id(responses.movieResults.get(position).id);
//                        movie.setOriginalTitle(responses.movieResults.get(position).originalTitle);
//                        movie.setOverview(responses.movieResults.get(position).overview);
//                        movie.setReleaseDate(responses.movieResults.get(position).releaseDate);
//                        movie.setUserRating(responses.movieResults.get(position).voteAverage);
//                        movie.setImageThumb("http://image.tmdb.org/t/p/w185/" + responses.movieResults.get(position).posterPath);
//                        fragmentTransaction.replace(R.id.container_fragments, DetailsFragment.newInstance(movie), DetailsFragment.class.getSimpleName());
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
//                    });
                });


    }

}
