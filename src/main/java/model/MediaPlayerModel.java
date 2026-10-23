package src.main.java.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mpatric.mp3agic.UnsupportedTagException;
import src.main.java.files.MusicFileLoader;
import src.main.java.img.ImageExtractor;
import src.main.java.img.ImageResources;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import src.main.java.stream.StreamHandler;
import src.main.java.interfaces.*;
import src.main.java.interfaces.observer.*;
import src.main.java.volume.VolumeControl;

public class MediaPlayerModel implements Observable<MediaPlayerStateObserver> {
    private final List<MediaPlayerStateObserver> observers = new ArrayList<>();

    private VolumeControl volumeControl;
    private MusicFileLoader musicFileLoader;
    private ImageExtractor mp3ImageExtractor;
    private Player player;
    private volatile boolean isPlaying = false;
    private volatile boolean round = false;
    private volatile boolean random = false;
    // private int pausedFrame = 0;

    public MediaPlayerModel(String folderMusic) throws UnsupportedTagException {
        volumeControl = new VolumeControl();
        musicFileLoader = new MusicFileLoader(folderMusic, 0);
        mp3ImageExtractor = new ImageExtractor(musicFileLoader);
        progressBar();

    }

    private void progressBar() {
        StreamHandler.startStreamWithWhile(_ -> {
            try {
                long totalDuration = musicFileLoader.getCurrentSong().getDurationInSeconds();

                try {
                    long currentPosition = player.getPosition();
                    int progressPercent = (int) (((double) currentPosition / 1000) / totalDuration * 100);
                    notifyObservers(observer -> observer.onProgressUpdated(progressPercent));
                } catch (NullPointerException e) {
                    notifyObservers(observer -> observer.onProgressUpdated(0));
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, true);
    }

    public void roundModes() {

        round = !round;
        StreamHandler.startStream(_ -> {
            notifyObservers(
                    observer -> observer
                            .onRepeatIconChanged(round ? ImageResources.roundImgOn : ImageResources.roundImgOff));
        });
    }

    public void randomModes() {

        random = !random;
        musicFileLoader.setRandomMode(random);
        StreamHandler.startStream(_ -> {
            notifyObservers(
                    observer -> observer
                            .onShuffleIconChanged(random ? ImageResources.randomImgOn : ImageResources.randomImgOff));
        });
    }

    public void play() {
        File currentFile = musicFileLoader.getCurrentSong().getFile();
        String currentTrack = musicFileLoader.getCurrentName();
        String artist = musicFileLoader.getCurrentSong().getArtist();

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

                // JavaSoundAudioDevice audioDevice = new JavaSoundAudioDevice() {
                //     @Override
                //     protected void createSource() throws JavaLayerException {
                //         // 1. Вызываем родителя, чтобы он заполнил свой private source
                //         super.createSource();

                //         // 2. Достаем ту же самую созданную линию из AudioSystem по ее формату
                //         try {
                //             // Поскольку super.createSource() уже открыл и занес линию в систему,
                //             // вытаскиваем активную SourceDataLine для нашего формата:
                //             javax.sound.sampled.DataLine.Info info = getSourceLineInfo();
                //             javax.sound.sampled.Line line = javax.sound.sampled.AudioSystem.getLine(info);

                //             if (line instanceof javax.sound.sampled.SourceDataLine sourceLine) {
                //                 // Передаем линию в VolumeControl
                //                 volumeControl.setAudioLine(sourceLine);
                //             }
                //         } catch (Exception ignored) {
                //             // Игнорируем, если не удалось перехватить
                //         }
                //     }
                // };

                player = new Player(fileInputStream);

                setPlaying(true);
                StreamHandler.startStream(_ -> {
                    notifyObservers(observer -> observer.onCoverImageChanged(mp3ImageExtractor.getImg()));

                    StreamHandler.startStream(_ -> {
                        notifyObservers(observer -> observer.onTrackTitleChanged(currentTrack));
                        notifyObservers(observer -> observer.onArtistNameChanged(artist));
                    });
                    StreamHandler.startStream(_ -> {
                        notifyObservers(observer -> observer.onPlayPauseIconChanged(ImageResources.stopImg));
                    });
                });

                player.play();

            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                setPlaying(false);
                if (player != null && player.isComplete()) {
                    notifyObservers(observer -> observer.onPlayPauseIconChanged(ImageResources.playImg));
                    if (round) {
                        next();
                    }
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
                notifyObservers(observer -> observer.onPlayPauseIconChanged(ImageResources.playImg));
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

    public void setVolume(int level) {
        volumeControl.setVolume(level);
    }

    @Override
    public void addObserver(MediaPlayerStateObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MediaPlayerStateObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Consumer<MediaPlayerStateObserver> action) {
        for (MediaPlayerStateObserver observer : observers) {
            action.accept(observer);
        }
    }

}
