package src.main.java.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mpatric.mp3agic.UnsupportedTagException;

import src.main.java.control.MediaPlayerController;
import src.main.java.img.Mp3ImageExtractor;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import src.main.java.stream.StreamHandler;
import src.main.java.interfaces.*;
import src.main.java.interfaces.observer.*;
import src.main.java.volume.VolumeControl;
import src.main.java.music.MusicFileLoader;


public class MediaPlayerModel implements Subject<Observer2, Action<Observer2>> {
    private final List<Observer2> observers2 = new ArrayList<>();

    @SuppressWarnings("unused")
    private MediaPlayerController mediaPlayerController;
    private VolumeControl volumeControl;
    private MusicFileLoader musicFileLoader;
    private Mp3ImageExtractor mp3ImageExtractor;
    private Player player;
    private volatile boolean isPlaying = false;

    public MediaPlayerModel(String folderMusic, String defaultImg, int volume) throws UnsupportedTagException {
        volumeControl = new VolumeControl(volume, this);
        musicFileLoader = new MusicFileLoader(folderMusic);
        mp3ImageExtractor = new Mp3ImageExtractor(defaultImg);
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentSong().getfFile());
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
                stop();
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

        });
    }

    public void play() {
        File currentFile = musicFileLoader.getCurrentSong().getfFile();
        String currentTrack = musicFileLoader.getCurrentTrack();

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
                    BufferedImage img = mp3ImageExtractor.getImg();
                    if (img != null) {
                        notifyObservers(observer -> observer.setImg(img));
                    } else {
                        notifyObservers(observer -> observer.setImg(mp3ImageExtractor.getDefaultImg()));
                    }
                });

                StreamHandler.startStream(_ -> {
                    notifyObservers(observer -> observer.setLabelTrack("Playing track: " + currentTrack));
                });

                StreamHandler.startStream(_ -> {
                    notifyObservers(observer -> observer.setLabelButton("Stoping"));
                });

                StreamHandler.startStreamWithWhile(_ -> {
                    try {
                        long totalDuration = musicFileLoader.getCurrentSong().getDurationInSeconds();

                        // Получаем текущую позицию воспроизведения
                        try {
                            long currentPosition = player.getPosition();
                            int progressPercent = (int) (((double) currentPosition / 1000) / totalDuration * 100);
                            notifyObservers(observer -> observer.setProgressBar(progressPercent));
                        } catch (NullPointerException e) {
                            notifyObservers(observer -> observer.setProgressBar(0));
                        }

                        // Уведомляем наблюдателей об изменении прогресса

                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, true);

                player.play();

                // System.out.println("Playing: " + currentTrack);

            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                setPlaying(false);
                if (player != null && player.isComplete()) {
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

    // Метод для получения значения флага isPlaying
    public boolean isPlaying() {
        synchronized (this) {
            return this.isPlaying;
        }
    }

    public void stop() {

        if (player != null) {
            player.close();
            player = null;
            setPlaying(false);
            StreamHandler.startStream(_ -> {
                notifyObservers(
                        observer -> observer.setLabelTrack("Stopped track: " + musicFileLoader.getCurrentTrack()));
            });
            StreamHandler.startStream(_ -> {
                notifyObservers(observer -> observer.setLabelButton("Playing"));
            });

        } else {
            System.out.println("No music is playing.");
        }
    }

    public void next() {
        stop();
        musicFileLoader.nextFile();
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentSong().getfFile());
        play();
    }

    public void back() {
        stop();
        musicFileLoader.backFile();
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentSong().getfFile());
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