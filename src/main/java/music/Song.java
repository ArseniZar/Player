package src.main.java.music;
import java.io.File;
import java.io.IOException;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Song {
    private String title;
    private String artist;
    private String album;
    private String genre;
    private int year;
    private long durationInSeconds;
    private String fileName;
    private  File mp3;

    // Конструктор
    public Song(String title, String artist, String album, String genre, int year, long durationInSeconds, String fileName, File mp3File) throws UnsupportedTagException, InvalidDataException, IOException {
        this.mp3 = mp3File;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.durationInSeconds = durationInSeconds;
        this.fileName = fileName;
    }

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public File getfFile(){
        return mp3;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", durationInSeconds=" + durationInSeconds +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
