import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class MusicFileLoader {
    private List<File> files = new ArrayList<>();
    private String directoryPath;
    private File currentFile = null;
    private int currentIndex = 0;

    // Конструктор класса, принимающий путь к директории
    public MusicFileLoader(String directoryPath) {
        this.directoryPath = directoryPath;
        loadMusicFiles();
        setCurrentFile(0);
    }

    private void loadMusicFiles() {
        File folder = new File(directoryPath);
        @SuppressWarnings("unused")
        File[] tempFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));

        if (tempFiles != null) {
            for (File file : tempFiles) {
                files.add(file);
                System.out.println(file.getName());
            }
        }
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
        loadMusicFiles();
    }

    public void setCurrentFile(int index) {
        if (index >= 0 && index < files.size()) {
            currentFile = files.get(index);
            this.currentIndex = index;
        } else {
            System.out.println("Invalid file index.");
        }
    }

    public void nextFile() {
        if (files.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }
        currentIndex = (currentIndex + 1) % files.size();
        setCurrentFile(currentIndex);
    }

    public void backFile() {
        if (files.isEmpty()) {
            System.out.println("No tracks to play.");
            return;
        }

        currentIndex = (currentIndex - 1 + files.size()) % files.size();
        setCurrentFile(currentIndex);
    }

    public List<File> getFiles() {
        return files;
    }

    public String getCurrentTrack() {
        return currentFile.getName();
    }

    public File getCurrentFile() {
        return currentFile;
    }

}
