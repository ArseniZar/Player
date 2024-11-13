package src.main.java.interfaces.observer;

import java.awt.image.BufferedImage;

public interface Observer2 {
    void setProgressBar(int level);

    void setLabelTrack(String label);

    void setImgButtonStartStop(BufferedImage img);
    
    void setImgButtonRound(BufferedImage img);

    void setImgButtonRandom(BufferedImage img);

    void setVolume(int level);

    void setImg(BufferedImage img);


}
