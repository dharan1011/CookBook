package com.example.dharanaditya.cookbook.ui.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.StepDetailActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements SimpleExoPlayer.EventListener {
    public static final String TAG = StepDetailFragment.class.getSimpleName();

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;

    private OnNextButtonClickListener nextButtonClickListener;

    @BindView(R.id.video_player)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.tv_step_description)
    TextView descriptionTextView;
    @BindView(R.id.btn_next_step)
    Button nextStepButton;

    private Step currentStep;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepDetailActivity) {
            nextButtonClickListener = (OnNextButtonClickListener) context;
        }
    }

    private void initializePlayer() {

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(this);
        simpleExoPlayerView.setPlayer(exoPlayer);

        MediaSource mediaSource = new DashMediaSource(
                Uri.parse(currentStep.getVideoURL()),
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name))),
                new DefaultDashChunkSource.Factory(new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)))),
                null,
                null
        );
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(false);
    }

    private void releasePlayer() {
        exoPlayer.release();
        exoPlayer.removeListener(this);
        exoPlayer = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setCurrentStep((Step) Parcels.unwrap(savedInstanceState.getParcelable("step_state_key")));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, v);
        descriptionTextView.setText(currentStep.getDescription());
        //TODO sanitize Step and set data accordingly
        if (exoPlayer == null)
            initializePlayer();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step_state_key", Parcels.wrap(currentStep));
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @OnClick(R.id.btn_next_step)
    public void nextStep(View v) {
        nextButtonClickListener.onNextButtonClick(currentStep.getId());
    }

    public interface OnNextButtonClickListener {
        void onNextButtonClick(int id);
    }
}
