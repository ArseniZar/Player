package src.main.java;

import com.mpatric.mp3agic.BaseException;

import src.main.java.gui.MediaPlayerView;
import src.main.java.model.MediaPlayerModel;
import src.main.java.volume.VolumeControl;
import src.main.java.control.MediaPlayerController;

public class Main {
    public static final int LEVEL = (int) VolumeControl.getCurrentVolume();
    public static final String FOLDER_MUSIC = System.getProperty("user.dir") + "/src/main/resources/music";
    public static final String DEFAULT_IMG = System.getProperty("user.dir") + "/src/main/resources/img/note.png";
  
    public static void main(String[] args) throws BaseException {

        System.out.println(FOLDER_MUSIC);
        MediaPlayerView view = new MediaPlayerView(LEVEL, DEFAULT_IMG);
        MediaPlayerController controller = new MediaPlayerController(view);
        MediaPlayerModel model = new MediaPlayerModel(FOLDER_MUSIC, DEFAULT_IMG, LEVEL);
        controller.start_Observer(model);
        model.start_Observer(controller);

    }
}

// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;

// public class Main {
// public static void main(String[] args) {
// Mp3ImageExtractor extractor = new Mp3ImageExtractor();
// File mp3File = new File("/home/ars/Documents/Code
// development/Java/player/music/Happy.mp3");
// byte[] imageData = extractor.getImage(mp3File);

// if (imageData != null) {
// // Сохраняем изображение в файл
// try (FileOutputStream fos = new FileOutputStream("extracted_cover.jpg")) {
// fos.write(imageData);
// System.out.println("Изображение успешно извлечено и сохранено!");
// } catch (IOException e) {
// System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
// }
// } else {
// System.out.println("Изображение не было извлечено.");
// }
// }
// }
