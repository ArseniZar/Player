import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import interfaces.*;
import interfaces.observer.*;

public class MediaPlayerModel implements Subject<Observer2, Action<Observer2>> {
    private final List<Observer2> observers2 = new ArrayList<>();

    @SuppressWarnings("unused")
    private MediaPlayerController mediaPlayerController;
    private VolumeControl volumeControl;
    private MusicFileLoader musicFileLoader;
    private Mp3ImageExtractor mp3ImageExtractor;
    private Player player;
    private boolean isPlaying = false;

    public MediaPlayerModel(String folderMusic, String defaultImg, int volume) {
        volumeControl = new VolumeControl(volume, this);
        musicFileLoader = new MusicFileLoader(folderMusic);
        mp3ImageExtractor = new Mp3ImageExtractor(defaultImg);
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentFile());
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        File currentFile = musicFileLoader.getCurrentFile();
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
                isPlaying = true;
                BufferedImage img = mp3ImageExtractor.getImg();
                if (img != null) {
                    notifyObservers(observer -> observer.setImg(img));
                } else {
                    notifyObservers(observer -> observer.setImg(mp3ImageExtractor.getDefaultImg()));
                }

                notifyObservers(observer -> observer.setLabelTrack("Playing track: " + currentTrack));
                notifyObservers(observer -> observer.setLabelButton("Stoping"));
                System.out.println("Playing: " + currentTrack);
                player.play();
            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                isPlaying = false;
                notifyObservers(observer -> observer.setLabelTrack("Stopped track: " + currentTrack));
            }
        });
    }

    public void stop() {

        if (player != null) {
            player.close();
            player = null;
            isPlaying = false;
            notifyObservers(observer -> observer.setLabelTrack("Stopped track: " + musicFileLoader.getCurrentTrack()));
            notifyObservers(observer -> observer.setLabelButton("Playing"));
        } else {
            System.out.println("No music is playing.");
        }
    }

    public void next() {
        stop();
        musicFileLoader.nextFile();
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentFile());
        play();
    }

    public void back() {
        stop();
        musicFileLoader.backFile();
        mp3ImageExtractor.getImage(musicFileLoader.getCurrentFile());
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
