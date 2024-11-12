package src.main.java.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
        view.addPlayStopListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action<Observer1> action = mediaPlayerModel.isPlaying()
                        ? observer -> observer.onStop()
                        : observer -> observer.onPlay();
                notifyObservers(action);
            }
        });
        view.addNextListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(observer -> observer.onNext());
            }
        });

        view.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyObservers(observer -> observer.onBack());
            }
        });

        view.addVolumeChangeListener(_ -> {
            int level = (view.getVolume());
            notifyObservers(observer -> observer.setVolume(level));

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
            public void setLabelButton(String label) {
                view.setPlayStopButtonText(label);
            }

            @Override
            public void setVolume(int level) {
                view.setVolume(level);
            }

            @Override
            public void setImg(BufferedImage img) {
                view.setImage(img);
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
