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
    private final List<File> files = new ArrayList<>();
    private MediaPlayerController mediaPlayerController;
    private String currentTrack;
    private Player player;
    private boolean isPlaying = false;
    private File currentFile = null;
    private int currentIndex = 0;
    private int volume = 0;

    public MediaPlayerModel(int volume) {
        this.volume= volume;
        setVolumePlayer(volume);
        loadMusicFiles();
        if (!files.isEmpty()) {
            setCurrentFile(0);
            System.out.println("Number of MP3 files found: " + files.size());
        } else {
            System.out.println("No MP3 files found in the directory.");
        }
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
                setVolumePlayer(level);
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

    private void loadMusicFiles() {
        File folder = new File("/home/ars/Documents/Code development/Java/player/music");
        File[] tempFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
        if (tempFiles != null) {
            for (File file : tempFiles) {
                files.add(file);
                System.out.println(file.getName());
            }
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void setCurrentFile(int index) {
        if (index >= 0 && index < files.size()) {
            currentFile = files.get(index);
            currentIndex = index;
        } else {
            System.out.println("Invalid file index.");
        }
    }

    public void play() {
        if (isPlaying) {
            System.out.println("Music is already playing.");
            return;
        }

        if (currentFile == null) {
            System.out.println("No file to play.");
            return;
        }

        new Thread(() -> {
            try (FileInputStream fileInputStream = new FileInputStream(currentFile)) {
                player = new Player(fileInputStream);
                currentTrack = currentFile.getName();
                isPlaying = true;
                notifyObservers(observer -> observer.setLabelTrack("Playing track: " + currentTrack));
                notifyObservers(observer -> observer.setLabelButton("Stoping"));
                System.out.println("Playing: " + currentTrack);

                // Применяем громкость перед воспроизведением
                adjustVolume(volume);

                player.play();
            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                isPlaying = false;
                notifyObservers(observer -> observer.setLabelTrack("Stopped track: " + currentTrack));
            }
        }).start();
    }

    public void stop() {
        if (player != null) {
            player.close();
            player = null;
            isPlaying = false;
            notifyObservers(observer -> observer.setLabelTrack("Stopped track: " + currentTrack));
            notifyObservers(observer -> observer.setLabelButton("Playing"));
        } else {
            System.out.println("No music is playing.");
        }
    }

    // public void pause() {
    // if (player != null) {
    // if (isPlaying) {
    // // Если музыка воспроизводится, приостанавливаем ее
    // player.pause();
    // isPlaying = false;
    // notifyObservers1("Paused track: " + currentTrack);
    // notifyObservers2("Paused");
    // } else {
    // // Если музыка уже приостановлена, выводим сообщение
    // System.out.println("Music is already paused.");
    // }
    // } else {
    // System.out.println("No music is playing.");
    // }
    // }

    public void next() {
        if (files.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }

        stop();
        currentIndex = (currentIndex + 1) % files.size();
        setCurrentFile(currentIndex);
        play();
    }

    public void back() {
        if (files.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }

        stop();
        currentIndex = (currentIndex - 1 + files.size()) % files.size();
        setCurrentFile(currentIndex);
        play();
    }

    public void setVolumePlayer(int newVolume) {
        try {
            this.volume = newVolume;
            adjustVolume((float) newVolume);
        } catch (NumberFormatException e) {
            System.out.println("Invalid volume value: " + newVolume);
        }
    }

    // Применение громкости через amixer
    private void adjustVolume(float volume) {
        try {
            String command = "amixer set Master " + volume + "%";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // Дожидаемся завершения выполнения команды
            System.out.println("Volume set to: " + volume + "%");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error adjusting volume: " + e.getMessage());
        }
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
