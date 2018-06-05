package com.example.aidan.bakingapp.Fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aidan.bakingapp.Models.Bakes;
import com.example.aidan.bakingapp.Models.Steps;
import com.example.aidan.bakingapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressWarnings("StringBufferReplaceableByString")
public class VideoPlayerFragment extends Fragment implements Parcelable {
    public static final String PLAYER_USER_AGENT = "bakeStepVideo";
    public static final Creator<VideoPlayerFragment> CREATOR = new Creator<VideoPlayerFragment>() {
        @Override
        public VideoPlayerFragment createFromParcel(Parcel in) {
            return new VideoPlayerFragment(in);
        }

        @Override
        public VideoPlayerFragment[] newArray(int size) {
            return new VideoPlayerFragment[size];
        }
    };
    private static final String PLAYER_POSITION = "playerPosition";
    private static final String BAKES_STATE = "bakes_state";
    private static final String STEP_POSITION_STATE = "step_position_state";
    private static long currentPlayerPosition = -1;
    @BindView(R.id.tv_step_desc)
    TextView tvStepDesc;
    @BindView(R.id.tv_step_number)
    TextView tvStepNo;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_prev)
    ImageView ivPrevious;
    @BindView(R.id.exo_no_video)
    ImageView ivNoVideoPlaceholder;
    @BindView(R.id.exo_buffering)
    ProgressBar pbBuffering;
    @BindView(R.id.vw_step_video)
    PlayerView pvStepVideo;
    @BindView(R.id.exo_shutter)
    View exoShutter;
    ExoPlayer mExoPlayer = null;
    List<Steps> stepsList;
    Bakes bakes = null;
    int totalSteps = 0;
    int stepPosition;

    public VideoPlayerFragment() {
    }

    @SuppressLint("ValidFragment")
    protected VideoPlayerFragment(Parcel in) {
        stepsList = in.createTypedArrayList(Steps.CREATOR);
        totalSteps = in.readInt();
        stepPosition = in.readInt();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_video_player, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            currentPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
            bakes = savedInstanceState.getParcelable(BAKES_STATE);
            stepPosition = savedInstanceState.getInt(STEP_POSITION_STATE);
        }

        if (bakes != null) {
            stepsList = bakes.getStepsList();
            totalSteps = (stepsList.size() - 1);
            getSelectedStep(stepsList.get(stepPosition));
        }
        return view;
    }

    public void setBake(Bakes bakes) {
        this.bakes = bakes;
    }

    public void setStepPosition(int stepPosition) {
        this.stepPosition = stepPosition;
    }

    public void getSelectedStep(Steps steps) {
        String stepDesc;
        if (steps.getVideoUrl().trim().equals("")) {
            pbBuffering.setVisibility(View.GONE);
            ivNoVideoPlaceholder.setVisibility(View.VISIBLE);
            exoShutter.setBackgroundColor(getResources().getColor(R.color.no_video_background));
        } else {
            ivNoVideoPlaceholder.setVisibility(View.INVISIBLE);
            exoShutter.setBackgroundColor(getResources().getColor(R.color.black_color));
            initializePlayer(Uri.parse(steps.getVideoUrl()));
        }
        tvStepDesc.setText(steps.getFullDescription());
        if (steps.getStepId() > 0) {
            stepDesc = new StringBuilder().append(getString(R.string.step)).append(" \t")
                    .append(steps.getStepId()).append(" / ").append(totalSteps).toString();
        } else {
            stepDesc = getString(R.string.introduction_step);
        }
        tvStepNo.setText(stepDesc);
    }

    public void initializePlayer(Uri sourceUri) {
        if (getContext() == null) return;
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            pvStepVideo.setPlayer(mExoPlayer);
            mExoPlayer.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        pbBuffering.setVisibility(View.GONE);
                    } else if (playWhenReady) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            pbBuffering.setVisibility(View.VISIBLE);
                            if (currentPlayerPosition != -1) {
                                mExoPlayer.seekTo(currentPlayerPosition);
                                currentPlayerPosition = -1;
                            }
                        }
                    }
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                    Timber.d(error);
                }
            });
            String userAgent = Util.getUserAgent(getContext(), PLAYER_USER_AGENT);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(getContext(), userAgent)).createMediaSource(sourceUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @OnClick(R.id.iv_next)
    void nextStep() {
        int newPosition = stepPosition + 1;
        if (newPosition >= 0 && newPosition <= (stepsList.size() - 1)) {
            releasePlayer();
            pvStepVideo.setPlayer(null);
            stepPosition = newPosition;
            getSelectedStep(stepsList.get(stepPosition));
        }
    }

    @OnClick(R.id.iv_prev)
    void previousStep() {
        int newPosition = stepPosition - 1;
        if (newPosition >= 0 && newPosition <= (stepsList.size() - 1)) {
            releasePlayer();
            pvStepVideo.setPlayer(null);
            stepPosition = newPosition;
            getSelectedStep(stepsList.get(stepPosition));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BAKES_STATE, bakes);
        outState.putInt(STEP_POSITION_STATE, stepPosition);
        if (mExoPlayer != null)
            outState.putLong(PLAYER_POSITION, mExoPlayer.getCurrentPosition());
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(stepsList);
        dest.writeInt(totalSteps);
        dest.writeInt(stepPosition);
    }
}
