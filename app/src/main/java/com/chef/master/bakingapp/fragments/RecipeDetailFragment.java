package com.chef.master.bakingapp.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chef.master.bakingapp.Constants;
import com.chef.master.bakingapp.DetailRecipe;
import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.model.Ingredient;
import com.chef.master.bakingapp.api.model.Step;
import com.chef.master.bakingapp.fragments.adapter.DescriptionAdapter;
import com.chef.master.bakingapp.fragments.adapter.IngredientAdapter;
import com.chef.master.bakingapp.room.local.IngredientContract;
import com.chef.master.bakingapp.widget.AppWidgetProvider;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chef.master.bakingapp.Constants.RECIPE;
import static com.chef.master.bakingapp.Constants.SHAREDPREF;

/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class RecipeDetailFragment extends Fragment {
    @BindView(R.id.recipe_ingredients)
    RecyclerView ingredientsList;
    @BindView(R.id.recipe_desciption)
    RecyclerView descriptionList;
    @BindView(R.id.addToWidget)
    Button addToWidget;
    private static final String DETAIL_KEY = "DETAIL_RECIPE";
    private static final String NAME_KEY = "NAME_RECIPE";
    DetailRecipe detailRecipe;
    IngredientAdapter ingredientAdapter;
    int mCurCheckPosition;
    SimpleExoPlayerView mExoPlayerView;
    String recipeName;

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;
    int pagePosition = 0;
    DescriptionAdapter descriptionAdapter;
    int widthPixels;
    View viewDetail;
    SharedPreferences mSharedPreferences;

    public static RecipeDetailFragment newInstance(DetailRecipe detailRecipe, String recipeName) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(DETAIL_KEY, detailRecipe);
        args.putString(NAME_KEY, recipeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        viewDetail = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, viewDetail);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            detailRecipe = bundle.getParcelable(DETAIL_KEY);
            recipeName = bundle.getString(NAME_KEY);
            mSharedPreferences = getActivity().getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE);
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            widthPixels = metrics.widthPixels;

            ingredientAdapter = new IngredientAdapter(getActivity(), detailRecipe.getIngredients());
            ingredientsList.setAdapter(ingredientAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            ingredientsList.setLayoutManager(linearLayoutManager);


//            final LayoutInflater factory = getActivity().getLayoutInflater();

//            final View textEntryView = factory.inflate(R.layout.fragment_step_details, null);

//            landmarkEditNameView = (TextView) textEntryView.findViewById(R.id.landmark_name_dialog_edit);
            descriptionAdapter = new DescriptionAdapter(getActivity(), detailRecipe.getSteps());
            descriptionAdapter.setClickListener((view, position) -> {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_fragments, StepDetailFragment.newInstance(detailRecipe.getSteps().get(position), position, detailRecipe), StepDetailFragment.class.getSimpleName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });
            descriptionList.setAdapter(descriptionAdapter);
            LinearLayoutManager linearLayoutManagerDescriptions = new LinearLayoutManager(getActivity());
            descriptionList.setLayoutManager(linearLayoutManagerDescriptions);


        }
        return viewDetail;
    }
@OnClick(R.id.addToWidget)
public void setAddToWidget(){
        addWidget();
}
    @Override
    public void onResume() {
        super.onResume();
        if (widthPixels > 600) {
            LinearLayout includedLayout = getActivity().findViewById(R.id.details_included);
            if (includedLayout != null) {
                descriptionAdapter = new DescriptionAdapter(getActivity(), detailRecipe.getSteps());


                descriptionAdapter.setClickListener((view, position) -> {
//                        TextView description = (TextView) textEntryView.findViewById(R.id.long_description);

                    Step step = detailRecipe.getSteps().get(position);
                    pagePosition = position;
                    if (step != null) {
                        if (step.getDescription() != null) {
                            TextView description = (TextView) viewDetail.findViewById(R.id.long_description);
                            description.setText(step.getDescription());
                        }
                        if (!step.getVideoURL().isEmpty()) {
                            mExoPlayerView = (SimpleExoPlayerView) includedLayout.findViewById(R.id.exo_player);
                            mExoPlayerView.setVisibility(View.VISIBLE);
                            mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                            bandwidthMeter = new DefaultBandwidthMeter();
                            mainHandler = new Handler();

                            initializePlayer(Uri.parse(step.getVideoURL()));
                        }
                    }
                });
                descriptionList.setAdapter(descriptionAdapter);

                TextView description = viewDetail.findViewById(R.id.long_description);
                ImageView thumb = viewDetail.findViewById(R.id.thumb);

                Button back = viewDetail.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pagePosition > 0) {
                            Step stepBack = detailRecipe.getSteps().get(pagePosition - 1);
                            if (stepBack != null) {
                                pagePosition--;
                                description.setText(stepBack.getDescription());

                                if (!stepBack.getThumbnailURL().isEmpty()) {
                                    Picasso.with(getActivity()).load(stepBack.getThumbnailURL()).into(thumb);
                                }
                            }
                            if (!stepBack.getVideoURL().isEmpty()) {
                                mExoPlayerView.setVisibility(View.VISIBLE);
                                mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                                bandwidthMeter = new DefaultBandwidthMeter();
                                mainHandler = new Handler();

                                initializePlayer(Uri.parse(stepBack.getVideoURL()));

                            } else {
                                mExoPlayerView.setVisibility(View.GONE);
                            }
                        }
                    }
                });

                Button next = viewDetail.findViewById(R.id.btn_next);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pagePosition < detailRecipe.getSteps().size() - 1) {
                            Step nextStep = detailRecipe.getSteps().get(pagePosition + 1);

                            if (nextStep != null) {
                                pagePosition++;
                                description.setText(nextStep.getDescription());

                                if (!nextStep.getThumbnailURL().isEmpty()) {
                                    Picasso.with(getActivity()).load(nextStep.getThumbnailURL()).into(thumb);
                                }
                            }
                            if (!nextStep.getVideoURL().isEmpty()) {
                                mExoPlayerView.setVisibility(View.VISIBLE);
                                mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                                bandwidthMeter = new DefaultBandwidthMeter();
                                mainHandler = new Handler();

                                initializePlayer(Uri.parse(nextStep.getVideoURL()));

                            }
                        }
                    }
                });
//                    LinearLayoutManager linearLayoutManagerDescriptionsBig = new LinearLayoutManager(getActivity());
//                    descriptionList.setLayoutManager(linearLayoutManagerDescriptionsBig);

            }
        }
    }

    public void addWidget() {
        ArrayList<Ingredient> ingredients = detailRecipe.getIngredients();

        mSharedPreferences.edit().putString(Constants.PREF_RECIPE, recipeName).apply();


        Uri uri1 = IngredientContract.CONTENT_URI;
        Cursor cursor = getActivity().getContentResolver().query(uri1, null, null, null, null);


        if (cursor != null) {

            //Delete the existing data
            while (cursor.moveToNext()) {
                Uri uri2 = IngredientContract.CONTENT_URI;
                getActivity().getContentResolver().delete(uri2,
                        IngredientContract.Columns._ID + "=?",
                        new String[]{cursor.getString(0)});

            }

            //Insert into database
            ContentValues values = new ContentValues();

            for (Ingredient ingredient : ingredients) {
                values.clear();
                values.put(IngredientContract.Columns.QUANTITY, ingredient.getQuantity());
                values.put(IngredientContract.Columns.MEASURE, ingredient.getMeasure());
                values.put(IngredientContract.Columns.INGREDIENT, ingredient.getIngredient());


                Uri uri = IngredientContract.CONTENT_URI;
                getActivity().getContentResolver().insert(uri, values);
            }
        }

        int[] ids = AppWidgetManager.getInstance(getActivity())
                .getAppWidgetIds(new ComponentName(getActivity(), AppWidgetProvider.class));
        AppWidgetProvider ingredientWidget = new AppWidgetProvider();
        ingredientWidget.onUpdate(getActivity(), AppWidgetManager.getInstance(getActivity()), ids);
        Context context = getActivity().getApplicationContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, AppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ing_widget_list);
        Toast.makeText(getActivity(), "successfully added", Toast.LENGTH_LONG).show();
    }

    private void initializePlayer(Uri mediaUri) {
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }


}