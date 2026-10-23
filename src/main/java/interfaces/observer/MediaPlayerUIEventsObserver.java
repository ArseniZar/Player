package src.main.java.interfaces.observer;

public interface MediaPlayerUIEventsObserver {

    void onPlayPauseClicked();

    void onBackClicked();

    void onNextClicked();

    void onRepeatClicked();

    void onShuffleClicked();

    void onVolumeChanged(int level);
}