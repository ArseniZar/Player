package src.main.java.control;

import java.awt.image.BufferedImage;

import src.main.java.gui.MediaPlayerView;
import src.main.java.interfaces.observer.*;
import src.main.java.model.MediaPlayerModel;

public class MediaPlayerController {
    private MediaPlayerModel model;
    private MediaPlayerView view;

    public MediaPlayerController(MediaPlayerView mediaPlayerView, MediaPlayerModel mediaPlayerModel) {
        this.view = mediaPlayerView;
        this.model = mediaPlayerModel;

        view.addObserver(new MediaPlayerUIEventsObserver() {
            @Override
            public void onPlayPauseClicked() {
                if (model.isPlaying()) {
                    model.pause();
                } else {
                    model.play();
                }
            }

            @Override
            public void onBackClicked() {
                model.back();
            }

            @Override
            public void onNextClicked() {
                model.next();
            }

            @Override
            public void onRepeatClicked() {
                model.roundModes();
            }

            @Override
            public void onShuffleClicked() {
                model.randomModes();
            }

            @Override
            public void onVolumeChanged(int level) {
                model.setVolume(level);
            }
        });

        mediaPlayerModel.addObserver(new MediaPlayerStateObserver() {

            @Override
            public void onProgressUpdated(int level) {
                view.setProgressBar(level);
            }

            @Override
            public void onTrackTitleChanged(String title) {
                view.setTrackLabel(title);
            }

            @Override
            public void onArtistNameChanged(String artist) {
                view.setAdditionalLabel(artist);
            }

            @Override
            public void onPlayPauseIconChanged(BufferedImage img) {
                view.setPlayStopIcon(img);
            }

            @Override
            public void onRepeatIconChanged(BufferedImage img) {
                view.setRoundIcon(img);
            }

            @Override
            public void onShuffleIconChanged(BufferedImage img) {
                view.setRandomStopIcon(img);
            }

            @Override
            public void onVolumeLevelChanged(int level) {
                view.setVolume(level);
            }

            @Override
            public void onCoverImageChanged(BufferedImage img) {
                view.setImage(img);
            }
        });
    }

}
