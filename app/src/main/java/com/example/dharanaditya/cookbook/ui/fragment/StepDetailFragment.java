package com.example.dharanaditya.cookbook.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dharanaditya.cookbook.R;
import com.example.dharanaditya.cookbook.model.Step;
import com.example.dharanaditya.cookbook.ui.StepDetailActivity;
import com.example.dharanaditya.cookbook.ui.StepsListActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements SimpleExoPlayer.EventListener {

    public static final String TAG = StepDetailFragment.class.getSimpleName();
    private static final String MEDIA_SESSION_TAG = "media_session_tag";
    private static final String STEP_STATE_KEY = "step_state_key";
    private static final String POSITION_STATE_KEY = "exoplayer_position_state_key";

    @BindView(R.id.video_player)
    SimpleExoPlayerView simpleExoPlayerView;
    @Nullable
    @BindView(R.id.tv_step_description)
    TextView descriptionTextView;
    @Nullable
    @BindView(R.id.btn_next_step)
    FloatingActionButton nextStepButton;
    @Nullable
    @BindView(R.id.tv_step_details_error)
    TextView errorTextView;
    @BindView(R.id.imv_no_video)
    ImageView noVideoImageView;

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder playbackStateBuilder;
    private OnNextButtonClickListener nextButtonClickListener;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            setCurrentStep((Step) Parcels.unwrap(savedInstanceState.getParcelable(STEP_STATE_KEY)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, v);

        if (StepsListActivity.isTwoPane) {
            nextStepButton.setVisibility(View.GONE);
            descriptionTextView.setTextSize(30);
        }

        if (currentStep != null) {
            descriptionTextView.setText(currentStep.getDescription());
            if (!currentStep.getVideoURL().isEmpty())
                initializeMediaSession();

            if (exoPlayer == null)
                initializePlayer(currentStep.getVideoURL());

            if (savedInstanceState != null && exoPlayer != null) {
                exoPlayer.seekTo(savedInstanceState.getLong(POSITION_STATE_KEY, 0));
                exoPlayer.setPlayWhenReady(true);
            }

        } else {
            showErrorMessage();
        }
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            releasePlayer();
        }
    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), MEDIA_SESSION_TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(new MediaSessionCallback());

        playbackStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSession.setPlaybackState(playbackStateBuilder.build());
    }

    private void initializePlayer(String videoURL) {
        if (videoURL.isEmpty() || videoURL.equals(" ")) {
            noVideoImageView.setVisibility(View.VISIBLE);
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
            return;
        }
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(this);
        simpleExoPlayerView.setPlayer(exoPlayer);
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name))),
                new DefaultExtractorsFactory(),
                null,
                null);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(false);
    }

    private void releasePlayer() {
        exoPlayer.release();
        exoPlayer.removeListener(this);
        mediaSession.release();
        exoPlayer = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_STATE_KEY, Parcels.wrap(currentStep));
        if (exoPlayer != null)
            outState.putLong(POSITION_STATE_KEY, exoPlayer.getCurrentPosition());
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
    }

    @Optional
    private void showErrorMessage() {
        errorTextView.setVisibility(View.VISIBLE);
        simpleExoPlayerView.setVisibility(View.INVISIBLE);
        descriptionTextView.setVisibility(View.INVISIBLE);
        nextStepButton.setVisibility(View.INVISIBLE);
    }

    @Optional
    @OnClick(R.id.btn_next_step)
    public void nextStep(View v) {
        nextButtonClickListener.onNextButtonClick(currentStep.getId() + 1);
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
        if (playbackState == SimpleExoPlayer.STATE_READY && playWhenReady) {
            playbackStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PLAY);
        } else if (playbackState == SimpleExoPlayer.STATE_READY) {
            playbackStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(PlaybackStateCompat.ACTION_PAUSE);
        }
        mediaSession.setPlaybackState(playbackStateBuilder.build());
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

    public interface OnNextButtonClickListener {
        void onNextButtonClick(int id);
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            exoPlayer.seekTo(0);
        }
    }

    public class MediaReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(new MediaSessionCompat(getContext(), "TAG"), intent);
        }
    }
}
