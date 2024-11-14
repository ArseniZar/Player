package src.main.java.files;

import java.io.File;
import java.io.IOException;
import java.util.List;
import src.main.java.stream.StreamHandler;
import java.util.stream.Stream;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import src.main.java.img.ImageExtractor;
import java.util.ArrayList;

public class MusicFileLoader {
    private List<Song> songs = new ArrayList<>();
    private String directoryPath;
    private Song currentSong = null;
    private int currentIndex;
    private int nextIndex;
    private int backIndex;
    private boolean randomMode = false;

    // Конструктор класса, принимающий путь к директории
    public MusicFileLoader(String directoryPath, int index) throws UnsupportedTagException {
        this.directoryPath = directoryPath;
        this.currentIndex = index;
        loadMusicFiles();
        this.backIndex = (currentIndex - 1 + songs.size()) % songs.size();
        this.nextIndex = (currentIndex + 1) % songs.size();
        setCurrentSong();
    }

    public void loadMusicFiles() throws UnsupportedTagException {
        File folder = new File(directoryPath);
        @SuppressWarnings("unused")
        File[] tempFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));

        if (tempFiles != null) {
            for (File file : tempFiles) {
                try {
                    // Создаем объект Song из метаданных файла
                    Song song = extractSongMetadata(file);
                    songs.add(song); // Добавляем песню в список
                    System.out.println("Loaded song: " + song.getTitle());
                } catch (IOException | InvalidDataException e) {
                    System.err.println("Error reading file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public void setRandomMode(boolean randomMode) {
        this.randomMode = randomMode;
    }

    private Song extractSongMetadata(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(file);

        // Получаем ID3 теги
        String title = "";
        String artist = "Unknown Artist";
        String album = "Unknown Album";
        String genre = "Unknown Genre";
        int year = 0;

        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            title = id3v2Tag.getTitle();
            artist = id3v2Tag.getArtist();
            album = id3v2Tag.getAlbum();
            genre = id3v2Tag.getGenreDescription();
            try {
                year = Integer.parseInt(id3v2Tag.getYear());
            } catch (Exception e) {
            }

        }

        long durationInSeconds = mp3file.getLengthInSeconds(); // Получаем длительность песни в секундах
        System.out.println(durationInSeconds);
        // Создаем объект Song и возвращаем его
        return new Song(title, artist, album, genre, year, durationInSeconds, file.getName(), file);
    }

    public void setDirectoryPath(String directoryPath) throws UnsupportedTagException {
        this.directoryPath = directoryPath;
        loadMusicFiles();
    }

    private void generationIndex(boolean mod) {
        if (randomMode) {
            int random = currentIndex;
            while (random == currentIndex) {
                random = (int) (Math.random() * (songs.size() - 1));
            }
            if (mod) {
                this.nextIndex = random;
            } else {
                this.backIndex = random;
            }

        } else {
            if (mod) {
                int newcurrentIndex = (nextIndex + 1) % songs.size();
                this.nextIndex = newcurrentIndex;

            } else {
                int newcurrentIndex = (backIndex - 1 + songs.size()) % songs.size();
                this.backIndex = newcurrentIndex;

            }
        }

    }

    private void updateIndex(boolean mod) {
        if (mod) {
            backIndex = currentIndex;
            currentIndex = nextIndex;

        } else {
            nextIndex = currentIndex;
            currentIndex = backIndex;

        }
    }

    public void setCurrentSong() {

        if (currentIndex >= 0 && currentIndex < songs.size()) {
            currentSong = songs.get(currentIndex);
        } else {
            System.out.println("Invalid file index.");
        }
    }

    public void nextSong(ImageExtractor mp3ImageExtractor) {
        if (songs.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }
        updateIndex(true);
        setCurrentSong();

        StreamHandler.startStream(_ -> {
            generationIndex(true);
        });

        mp3ImageExtractor.updateImg(this, true);
    }

    public File nextFile() {
        return songs.get(nextIndex).getFile();
        // int currentIndexNew = (currentIndex + 1) % songs.size();
        // if (currentIndexNew >= 0 && currentIndexNew < songs.size()) {
        // return songs.get(currentIndexNew).getFile();
        // } else {
        // return null;
        // }

    }

    public File backFile() {
        return songs.get(backIndex).getFile();
        // int currentIndexNew = (currentIndex - 1 + songs.size()) % songs.size();
        // if (currentIndexNew >= 0 && currentIndexNew < songs.size()) {
        // return songs.get(currentIndexNew).getFile();
        // } else {
        // return null;
        // }

    }

    public void backSong(ImageExtractor mp3ImageExtractor) {
        if (songs.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }

        updateIndex(false);
        setCurrentSong();

        StreamHandler.startStream(_ -> {
            generationIndex(false);
        });
        mp3ImageExtractor.updateImg(this, false);
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getCurrentName() {
        return currentSong.getTitle() != "" ? currentSong.getTitle() : currentSong.getFileName();

    }

    public Song getCurrentSong() {
        return currentSong;
    }

}
