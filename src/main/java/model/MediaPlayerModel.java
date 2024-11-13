package src.main.java.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mpatric.mp3agic.UnsupportedTagException;
import src.main.java.control.MediaPlayerController;
import src.main.java.files.MusicFileLoader;
import src.main.java.img.ImageExtractor;
import src.main.java.img.ImageResources;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import src.main.java.stream.StreamHandler;
import src.main.java.interfaces.*;
import src.main.java.interfaces.observer.*;
import src.main.java.volume.VolumeControl;

public class MediaPlayerModel implements Subject<Observer2, Action<Observer2>> {
    private final List<Observer2> observers2 = new ArrayList<>();

    @SuppressWarnings("unused")
    private MediaPlayerController mediaPlayerController;
    private VolumeControl volumeControl;
    private MusicFileLoader musicFileLoader;
    private ImageExtractor mp3ImageExtractor;
    private Player player;
    private volatile boolean isPlaying = false;
    private volatile boolean round = false;
    private volatile boolean random = false;
    // private int pausedFrame = 0;

    public MediaPlayerModel(String folderMusic, int volume) throws UnsupportedTagException {
        volumeControl = new VolumeControl(volume, this);
        musicFileLoader = new MusicFileLoader(folderMusic, 0);
        mp3ImageExtractor = new ImageExtractor(musicFileLoader);
    }

    public void start_Observer(MediaPlayerController mediaPlayerController) {
        this.mediaPlayerController = mediaPlayerController;
        mediaPlayerController.addObserver(new Observer1() {

            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onStop() {
                pause();
            }

            @Override
            public void setVolume(int level) {
                volumeControl.setVolume(level);
            }

            @Override
            public void onNext() {
                next();
            }

            @Override
            public void onBack() {
                back();
            }

            @Override
            public void roundMode() {
                roundModes();
            }

            @Override
            public void randomMode() {
                randomModes();
            }

        });
    }

    public void roundModes() {

        round = !round;
        StreamHandler.startStream(_ -> {
            notifyObservers(
                    observer -> observer
                            .setImgButtonRound(round ? ImageResources.roundImgOn : ImageResources.roundImgOff));
        });
    }

    public void randomModes() {

        random = !random;
        StreamHandler.startStream(_ -> {
            notifyObservers(
                    observer -> observer
                            .setImgButtonRandom(random ? ImageResources.randomImgOn : ImageResources.randomImgOff));
        });
    }

    public void play() {
        File currentFile = musicFileLoader.getCurrentSong().getFile();
        String currentTrack = musicFileLoader.getCurrentName();

        if (isPlaying) {
            System.out.println("Music is already playing.");
            return;
        }

        if (currentFile == null) {
            System.out.println("No file to play.");
            return;
        }

        StreamHandler.startStream(_ -> {
            try (FileInputStream fileInputStream = new FileInputStream(currentFile)) {
                player = new Player(fileInputStream);
                setPlaying(true);
                StreamHandler.startStream(_ -> {
                    notifyObservers(observer -> observer.setImg(mp3ImageExtractor.getImg()));

                    StreamHandler.startStream(_ -> {
                        notifyObservers(observer -> observer.setLabelTrack(currentTrack));
                    });
                    StreamHandler.startStream(_ -> {
                        notifyObservers(observer -> observer.setImgButtonStartStop(ImageResources.stopImg));
                    });
                });

                StreamHandler.startStreamWithWhile(_ -> {
                    try {
                        long totalDuration = musicFileLoader.getCurrentSong().getDurationInSeconds();

                        try {
                            long currentPosition = player.getPosition();
                            int progressPercent = (int) (((double) currentPosition / 1000) / totalDuration * 100);
                            notifyObservers(observer -> observer.setProgressBar(progressPercent));
                        } catch (NullPointerException e) {
                            notifyObservers(observer -> observer.setProgressBar(0));
                        }

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, true);

                player.play();

            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                setPlaying(false);
                if (round && player != null && player.isComplete()) {
                    next();
                }

            }
        });

    }

    public void setPlaying(boolean isPlaying) {
        synchronized (this) {
            this.isPlaying = isPlaying;
        }
    }

    public boolean isPlaying() {
        synchronized (this) {
            return this.isPlaying;
        }
    }

    private void stop() {

        if (player != null) {
            player.close();
            player = null;
            setPlaying(false);
        } else {
            System.out.println("No music is playing.");
        }
    }

    public void pause() {

        if (player != null && isPlaying) {
            player.close();
            setPlaying(false);
            StreamHandler.startStream(_ -> {
                notifyObservers(observer -> observer.setImgButtonStartStop(ImageResources.playImg));
            });
        } else {
            System.out.println("No music is playing.");
        }

    }

    public void next() {
        stop();
        musicFileLoader.nextSong(mp3ImageExtractor);
        play();
    }

    public void back() {
        stop();
        musicFileLoader.backSong(mp3ImageExtractor);

        play();
    }

    @Override
    public void addObserver(Observer2 observer) {
        observers2.add(observer);
    }

    @Override
    public void removeObserver(Observer2 observer) {
        observers2.remove(observer);
    }

    @Override
    public void notifyObservers(Action<Observer2> action) {
        for (Observer2 observer : observers2) {
            action.execute(observer);
        }
    }

}
