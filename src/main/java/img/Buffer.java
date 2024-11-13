package src.main.java.img;

import java.awt.image.BufferedImage;

public class Buffer {
    private BufferedImage nextImg = null ;
    private BufferedImage img = null;
    private BufferedImage beakImg = null;

    public Buffer(BufferedImage beakImg ,BufferedImage img, BufferedImage nextImg){
        this.nextImg = nextImg;
        this.img = img;
        this.beakImg = beakImg;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public BufferedImage getBeakImg() {
        return beakImg;
    }

    public BufferedImage getNextImg() {
        return nextImg;
    }

    public void setBeakImg(BufferedImage beakImg) {
        this.beakImg = beakImg;
    }

    public void setNextImg(BufferedImage nextImg) {
        this.nextImg = nextImg;
    }

    public void updateImgNext() {
        beakImg = img;
        img = nextImg;
        nextImg = null;
    }

    public void updateImgBeak() {
        nextImg = img;
        img = beakImg;
        beakImg = null;
    }
}
