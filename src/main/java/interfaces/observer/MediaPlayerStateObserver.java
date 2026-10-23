package src.main.java.interfaces.observer;

import java.awt.image.BufferedImage;

public interface MediaPlayerStateObserver {

    void onProgressUpdated(int level);

    void onTrackTitleChanged(String title);

    void onArtistNameChanged(String artist);

    void onPlayPauseIconChanged(BufferedImage image);

    void onRepeatIconChanged(BufferedImage image);

    void onShuffleIconChanged(BufferedImage image);

    void onVolumeLevelChanged(int level);

    void onCoverImageChanged(BufferedImage image);
}