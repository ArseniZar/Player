public class Main {
    public static void main(String[] args) {
        MediaPlayerView view = new MediaPlayerView(50);
        // Создаем модель медиаплеера
        MediaPlayerModel model = new MediaPlayerModel(view.getVolume());
        
        // Создаем представление медиаплеера
        
        
        // Создаем контроллер медиаплеера
        MediaPlayerController controller = new MediaPlayerController(model, view);

        
    }
}

// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;

// public class Main {
//     public static void main(String[] args) {
//         Mp3ImageExtractor extractor = new Mp3ImageExtractor();
//         File mp3File = new File("/home/ars/Documents/Code development/Java/player/music/Happy.mp3");
//         byte[] imageData = extractor.getImage(mp3File);

//         if (imageData != null) {
//             // Сохраняем изображение в файл
//             try (FileOutputStream fos = new FileOutputStream("extracted_cover.jpg")) {
//                 fos.write(imageData);
//                 System.out.println("Изображение успешно извлечено и сохранено!");
//             } catch (IOException e) {
//                 System.err.println("Ошибка при сохранении изображения: " + e.getMessage());
//             }
//         } else {
//             System.out.println("Изображение не было извлечено.");
//         }
//     }
// }
