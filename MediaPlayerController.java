import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MediaPlayerController {
    private MediaPlayerModel model;
    private MediaPlayerView view;

    public MediaPlayerController(MediaPlayerModel model, MediaPlayerView view) {
        this.model = model;
        this.view = view;

        // Добавляем наблюдателя
        model.addObserver1((message) -> view.setTrackLabel(message));
        model.addObserver2((message) -> view.setPlayStopButtonText(message));

        // Добавляем слушателей для кнопок
        view.addPlayStopListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.isPlaying()) { // Проверяем состояние воспроизведения
                    model.stop(); // Останавливаем воспроизведение
                } else {
                    model.play(); // Запускаем воспроизведение
                }
            }
        });
        view.addNextListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.next();
            }
        });

        view.addBackListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.back();
            }
        });

        view.addVolumeChangeListener(e -> {
            int volume =  (view.getVolume());
            model.setVolume(volume);
            // Здесь вы можете вызвать метод для установки уровня громкости в медиаплеере
            // например, mediaPlayer.setVolume(volume);
        });
    }
}
