package src.main.java;

import com.mpatric.mp3agic.BaseException;

import src.main.java.gui.MediaPlayerView;
import src.main.java.model.MediaPlayerModel;
import src.main.java.volume.VolumeControl;
import src.main.java.control.MediaPlayerController;

public class Main {
    public static final int LEVEL = (int) VolumeControl.getCurrentVolume();
    public static final String FOLDER_MUSIC = System.getProperty("user.dir") + "/src/main/resources/music";
  
    public static void main(String[] args) throws BaseException {

        System.out.println(FOLDER_MUSIC);
        MediaPlayerView view = new MediaPlayerView(LEVEL);
        MediaPlayerController controller = new MediaPlayerController(view);
        MediaPlayerModel model = new MediaPlayerModel(FOLDER_MUSIC,LEVEL);
        controller.start_Observer(model);
        model.start_Observer(controller);

    }
}


