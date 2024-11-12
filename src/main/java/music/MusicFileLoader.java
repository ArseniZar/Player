package src.main.java.music;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.util.ArrayList;

public class MusicFileLoader {
    private List<Song> songs = new ArrayList<>();
    private String directoryPath;
    private Song currentSong = null;
    private int currentIndex = 0;

    // Конструктор класса, принимающий путь к директории
    public MusicFileLoader(String directoryPath) throws UnsupportedTagException {
        this.directoryPath = directoryPath;
        loadMusicFiles();
        setCurrentSong(0);
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

    private Song extractSongMetadata(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(file);

        // Получаем ID3 теги
        String title = "Unknown Title";
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

    public void setCurrentSong(int index) {
        if (index >= 0 && index < songs.size()) {
            currentSong = songs.get(index);
            this.currentIndex = index;
        } else {
            System.out.println("Invalid file index.");
        }
    }

    public void nextFile() {
        if (songs.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }
        currentIndex = (currentIndex + 1) % songs.size();
        setCurrentSong(currentIndex);
    }

    public void backFile() {
        if (songs.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }

        currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
        setCurrentSong(currentIndex);
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getCurrentTrack() {
        return currentSong.getFileName();
    }

    public Song getCurrentSong() {
        return currentSong;
    }

}
