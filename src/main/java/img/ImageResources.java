package src.main.java.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public final class ImageResources {
     public static BufferedImage logo =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/logo.png");
     public static final BufferedImage defaultImg = fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/note.png");
     public static final BufferedImage playImg = fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/play.png");
     public static final BufferedImage stopImg =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/stop.png");
     public static final BufferedImage roundImgOff = fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/roundOff.png");
     public static final BufferedImage roundImgOn =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/roundOn.png");
     public static final BufferedImage randomImgOff = fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/randomOff.png");
     public static final BufferedImage randomImgOn =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/randomOn.png");
     public  static BufferedImage next =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/next.png");
     public  static BufferedImage back =fileToBufferedImage(System.getProperty("user.dir") + "/src/main/resources/img/back.png");
     

    private static  BufferedImage fileToBufferedImage(String filePath) {
        try {
            File file = new File(filePath);
            BufferedImage img = ImageIO.read(file);
            return img;
        } catch (IOException e) {
            e.printStackTrace(); // Логируем исключение
            return null;
        }
    }
}
