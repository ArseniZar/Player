import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v2;
import java.io.File;

public class Mp3ImageExtractor {
    public byte[] getImage(File mp3File) {
        try {
            // Загружаем MP3 файл
            Mp3File mp3 = new Mp3File(mp3File);

            // Проверяем, есть ли ID3v2 теги
            if (mp3.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3.getId3v2Tag();

                // Получаем изображение из тега
                byte[] imageData = id3v2Tag.getAlbumImage();

                if (imageData != null) {
                    // Возвращаем изображение в виде массива байтов
                    return imageData;
                } else {
                    System.out.println("Изображение не найдено в метаданных MP3.");
                    return null; // Если изображение не найдено, возвращаем null
                }
            } else {
                System.out.println("Файл не содержит теги ID3v2.");
                return null; // Если нет ID3v2 тегов, возвращаем null
            }
        } catch (Exception e) {
            System.err.println("Ошибка при извлечении изображения: " + e.getMessage());
            return null; // В случае ошибки, возвращаем null
        }
    }
}
