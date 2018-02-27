package eu.lynxit.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import eu.lynxit.bakingapp.R;
import eu.lynxit.bakingapp.activities.MainActivity;
import eu.lynxit.bakingapp.model.Step;

/**
 * Created by lynx on 22/02/18.
 */

public class StepFragment extends Fragment {
    public static final String EXTRA_PLAYTIME = "eu.lynxit.bakingapp.extra.EXTRA_PLAYTIME";
    private Step mStep;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView thumbnail;
    private TrackSelector mTrackSelector = new DefaultTrackSelector();
    private LoadControl mLoadControl = new DefaultLoadControl();
    private Long mPlaytime = -1L;
    private ExoPlayer.EventListener mListener = new ExoPlayer.EventListener() {

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
            if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        mPlayer.getCurrentPosition(), 1f);
            } else if ((playbackState == ExoPlayer.STATE_READY)) {
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        mPlayer.getCurrentPosition(), 1f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        }

        @Override
        public void onSeekProcessed() {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStep = ((MainActivity) getActivity()).mViewModel.getSelectedStep();
        mPlayerView = view.findViewById(R.id.fragment_step_player);
        thumbnail = view.findViewById(R.id.fragment_step_image);
        TextView mDescription = view.findViewById(R.id.fragment_step_description);
        mDescription.setText(mStep.getDescription());
        initializeMediaSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStep.getVideoURL() != null) {
            initPlayer(Uri.parse(mStep.getVideoURL()));
        } else if (mStep.getThumbnailURL() != null) {
            RequestBuilder builder = Glide.with(thumbnail).load(mStep.getThumbnailURL());
            RequestOptions options = new RequestOptions();
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
            builder.apply(options);
            builder.into(thumbnail);
            thumbnail.setVisibility(View.VISIBLE);
        }
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getContext(), "StepFragment");
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setActive(true);

    }

    private void initPlayer(Uri mediaUri) {
        if (mPlayer == null) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), mTrackSelector, mLoadControl);
            mPlayerView.setPlayer(mPlayer);
            mPlayer.addListener(mListener);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), "BakingAppAgent"), new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);
            Log.d("StepFragment", "PlayTime " + mPlaytime);
            if (mPlaytime > 0) {
                mPlayer.seekTo(mPlaytime);
            }
            mPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaytime = mPlayer.getCurrentPosition();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_PLAYTIME, mPlaytime);
        Log.d("StepFragment","Putting "+mPlaytime);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPlaytime = savedInstanceState.getLong(EXTRA_PLAYTIME, -1L);
            Log.d("StepFragment","Restored "+mPlaytime);
        }
    }
}
