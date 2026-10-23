package src.main.java;

import com.mpatric.mp3agic.BaseException;
import src.main.java.gui.MediaPlayerView;
import src.main.java.model.MediaPlayerModel;
import src.main.java.control.MediaPlayerController;

public class Main {
    // public static final String FOLDER_MUSIC = System.getProperty("user.dir") +
    // "/src/main/resources/music";
    public static final String FOLDER_MUSIC = "/Users/ars/Documents/music";

    public static void main(String[] args) throws BaseException {
        
        System.out.println(FOLDER_MUSIC);
        MediaPlayerView view = new MediaPlayerView();
        MediaPlayerModel model = new MediaPlayerModel(FOLDER_MUSIC);
        MediaPlayerController _ = new MediaPlayerController(view, model);

    }
}
