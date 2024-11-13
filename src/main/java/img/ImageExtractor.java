package src.main.java.img;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v2;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import src.main.java.files.MusicFileLoader;
import src.main.java.stream.StreamHandler;

import javax.imageio.ImageIO;

public class ImageExtractor {
    private Buffer buffer;


    public ImageExtractor(MusicFileLoader musicFileLoader) {
        BufferedImage beak = getOrDefault(musicFileLoader.backFile());
        BufferedImage next = getOrDefault(musicFileLoader.nextFile());
        BufferedImage img = getOrDefault(musicFileLoader.getCurrentSong().getFile());

        this.buffer = new Buffer(beak, img, next);

    }

    private BufferedImage getOrDefault(File file) {
        BufferedImage image = checkImgMeta(file);
        return (image != null) ? image : ImageResources.defaultImg;
    }

    public BufferedImage getImg() {
        return buffer.getImg();
    }

    public void updateImg(MusicFileLoader musicFileLoader, boolean status) {
        if (status) {
            buffer.updateImgNext();

            StreamHandler.startStream(_ -> {
                File nextFile = musicFileLoader.nextFile();
                BufferedImage img = checkImgMeta(nextFile);
                img = (img != null) ? img : ImageResources.defaultImg;
                buffer.setNextImg(img);
            });
        } else {
            buffer.updateImgBeak();
            StreamHandler.startStream(_ -> {
                File nextFile = musicFileLoader.backFile();
                BufferedImage img = checkImgMeta(nextFile);
                img = (img != null) ? img : ImageResources.defaultImg;
                buffer.setBeakImg(img);
            });
        }

    }

    private static BufferedImage checkImgMeta(File mp3File) {
        try {
            Mp3File mp3 = new Mp3File(mp3File);
            System.out.println("Файл MP3 загружен: " + mp3File.getName());

            // Проверяем, есть ли ID3v2 теги
            if (mp3.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3.getId3v2Tag();
                System.out.println("Найден тег ID3v2");

                // Получаем изображение из тега (массив байтов)
                byte[] imageData = id3v2Tag.getAlbumImage();

                if (imageData != null) {
                    // Преобразуем массив байтов в BufferedImage
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
                    BufferedImage image = ImageIO.read(bais);
                    System.out.println("Изображение извлечено из MP3.");
                    return image;
                } else {
                    System.out.println("Изображение не найдено в метаданных MP3.");
                    return null;
                }
            } else {
                System.out.println("Файл не содержит теги ID3v2.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Ошибка при извлечении изображения (IOException): " + e.getMessage());
            return null;
        } catch (com.mpatric.mp3agic.UnsupportedTagException e) {
            System.err.println("Ошибка: неподдерживаемый формат тега: " + e.getMessage());
            return null;
        } catch (com.mpatric.mp3agic.InvalidDataException e) {
            System.err.println("Ошибка в данных MP3: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка при извлечении изображения: " + e.getMessage());
            return null;
        }
    }

}
