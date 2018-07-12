package com.chef.master.bakingapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chef.master.bakingapp.Constants;
import com.chef.master.bakingapp.DetailRecipe;
import com.chef.master.bakingapp.R;
import com.chef.master.bakingapp.api.model.Step;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Petya Marinova on 5/20/2018.
 */
public class StepDetailFragment extends Fragment {
    @BindView(R.id.long_description)
    TextView description;
    @BindView(R.id.thumb)
    ImageView thumb;
    @BindView(R.id.exo_player)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.no_source)
    TextView noSource;
    //    @Inject
//    protected RxBus rxBus;
    @BindView(R.id.btn_back)
    Button back;
    @BindView(R.id.btn_next)
    Button next;
    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;
    int page;
    private static final String DETAIL_KEY = "STEP_RECIPE";
    private static final String PAGE_KEY = "PAGE";
    private static final String DETAIL_RECIPE_KEY = "DETAIL_RECIPE";
    Step step;
    DetailRecipe detailRecipe;
    int mCurCheckPosition;
    private static final String TAG = StepDetailFragment.class.getSimpleName();

    public static StepDetailFragment newInstance(Step step, int positionAdapter, DetailRecipe detailRecipe) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(DETAIL_KEY, step);
        args.putInt(PAGE_KEY, positionAdapter);
        args.putParcelable(DETAIL_RECIPE_KEY, detailRecipe);
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
        View v = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, v);
        // Inflate the layout for this fragment

        Bundle bundle = getArguments();
        if (bundle != null) {
            step = bundle.getParcelable(DETAIL_KEY);
            page = bundle.getInt(PAGE_KEY);
            detailRecipe = bundle.getParcelable(DETAIL_RECIPE_KEY);
            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            if (rotation != 90 || rotation != 270) {


                description.setText(step.getDescription());

                if (!step.getThumbnailURL().isEmpty()) {
                    Picasso.with(getActivity()).load(step.getThumbnailURL()).into(thumb);
                }
            }
            if (!step.getVideoURL().isEmpty()) {
                mExoPlayerView.setVisibility(View.VISIBLE);
                mExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                bandwidthMeter = new DefaultBandwidthMeter();
                mainHandler = new Handler();

                initializePlayer(Uri.parse(step.getVideoURL()));

            } else {
                mExoPlayerView.setVisibility(View.GONE);
                noSource.setText(R.string.no_video);
            }

        }
        return v;
    }

    @OnClick(R.id.btn_next)
    public void nextClicked() {
        if (page < detailRecipe.getSteps().size()-1) {
            Step nextStep = detailRecipe.getSteps().get(page + 1);

            if (nextStep != null) {
                page++;
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

            } else {
                mExoPlayerView.setVisibility(View.GONE);
                noSource.setText(R.string.no_video);
            }
        }
    }

    @OnClick(R.id.btn_back)
    public void backClicked() {
        if (page > 0) {
            Step stepBack = detailRecipe.getSteps().get(page - 1);
            if (stepBack != null) {
                page--;
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
                noSource.setText(R.string.no_video);
            }
        }

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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (player != null) {
            outState.putLong(Constants.PLAYER_POSITION, player.getCurrentPosition());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (player != null) {
            player.stop();
            player.release();
        }
    }

}
