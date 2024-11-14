package src.main.java.control;

import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.MouseEvent;

import src.main.java.gui.MediaPlayerView;
import src.main.java.interfaces.*;
import src.main.java.interfaces.observer.*;
import src.main.java.model.MediaPlayerModel;

public class MediaPlayerController implements Subject<Observer1, Action<Observer1>> {
    private final List<Observer1> observers1 = new ArrayList<>();
    private MediaPlayerModel mediaPlayerModel;
    private MediaPlayerView view;

    public MediaPlayerController(MediaPlayerView view) {
        this.view = view;
        view.addPlayStopClickListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Action<Observer1> action = mediaPlayerModel.isPlaying()
                        ? observer -> observer.onStop()
                        : observer -> observer.onPlay();
                notifyObservers(action);
            }
        });
        view.addBackClickListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                notifyObservers(observer -> observer.onBack());
            }
        });

        view.addNextClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                notifyObservers(observer -> observer.onNext());
            }
        });

        view.addVolumeChangeListener(_ -> {
            int level = (view.getVolume());
            notifyObservers(observer -> observer.setVolume(level));

        });

        view.addRoundClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Вызов метода notifyObservers с нужным действием
                notifyObservers(observer -> observer.roundMode()); // Замените `someMethod` на нужный метод
            }
        });

        view.addRandomClickListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Вызов метода notifyObservers с нужным действием
                notifyObservers(observer -> observer.randomMode()); // Замените `someMethod` на нужный метод
            }
        });

    }

    public void start_Observer(MediaPlayerModel mediaPlayerModel) {
        this.mediaPlayerModel = mediaPlayerModel;
        mediaPlayerModel.addObserver(new Observer2() {

            @Override
            public void setProgressBar(int level) {
                view.setProgressBar(level);

            }

            @Override
            public void setLabelTrack(String label) {
                view.setTrackLabel(label);
            }

            @Override
            public void setVolume(int level) {
                view.setVolume(level);
            }

            @Override
            public void setImg(BufferedImage img) {
                view.setImage(img);
            }

            @Override
            public void setImgButtonStartStop(BufferedImage img) {
                view.setPlayStopIcon(img);

            }

            @Override
            public void setImgButtonRound(BufferedImage img) {
                view.setRoundIcon(img);

            }

            @Override
            public void setImgButtonRandom(BufferedImage img) {
                view.setRandomStopIcon(img);

            }

            @Override
            public void setLabelAdditional(String label) {
                view.setAdditionalLabel(label);
            }

        });

    }

    @Override
    public void addObserver(Observer1 observer) {
        observers1.add(observer);

    }

    @Override
    public void removeObserver(Observer1 observer) {
        observers1.remove(observer);
    }

    @Override
    public void notifyObservers(Action<Observer1> action) {
        for (Observer1 observer : observers1) {
            action.execute(observer);
        }
    }
}
