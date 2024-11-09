import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import observer.*;


public class MediaPlayerModel implements Subject {
    private final List<ObserverTreck> observers1 = new ArrayList<>();
    private final List<ObserverPlay> observers2 = new ArrayList<>();
    private final List<File> files = new ArrayList<>();
    private String currentTrack;
    private Player player;
    private boolean isPlaying = false;
    private File currentFile = null;
    private int currentIndex = 0;
    private float volume = 0; // Громкость в диапазоне от 0 до 100

    public MediaPlayerModel(float volume) {
        this.volume = volume;
        loadMusicFiles();
        if (!files.isEmpty()) {
            setCurrentFile(0);
            System.out.println("Number of MP3 files found: " + files.size());
        } else {
            System.out.println("No MP3 files found in the directory.");
        }
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

    @Override
    public void addObserver1(ObserverTreck observer) {
        observers1.add(observer);
    }

    @Override
    public void removeObserver1(ObserverTreck observer) {
        observers1.remove(observer);
    }

    @Override
    public void notifyObservers1(String message) {
        for (ObserverTreck observer : observers1) {
            observer.update(message);
        }
    }

    @Override
    public void addObserver2(ObserverPlay observer) {
        observers2.add(observer);
    }

    @Override
    public void removeObserver2(ObserverPlay observer) {
        observers2.remove(observer);
    }

    @Override
    public void notifyObservers2(String message) {
        for (ObserverPlay observer : observers2) {
            observer.update(message);
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
                notifyObservers1("Playing track: " + currentTrack);
                notifyObservers2("Stopped");
                System.out.println("Playing: " + currentTrack);

                // Применяем громкость перед воспроизведением
                adjustVolume(volume);

                player.play();
            } catch (IOException | JavaLayerException e) {
                System.err.println("Error during playback: " + e.getMessage());
                e.printStackTrace();
            } finally {
                isPlaying = false;
                notifyObservers1("Stopped track: " + currentTrack);
            }
        }).start();
    }

    public void stop() {
        if (player != null) {
            player.close();
            player = null;
            isPlaying = false;
            notifyObservers1("Stopped track: " + currentTrack);
            notifyObservers2("Playing");
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

    public void setVolume(int newVolume) {
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
}
