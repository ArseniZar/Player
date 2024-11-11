package interfaces.observer;

import java.awt.image.BufferedImage;

public interface Observer2 {
    void setProgressBar(int level);

    void setLabelTrack(String label);

    void setLabelButton(String label);

    void setVolume(int level);

    void setImg(BufferedImage img);

}
