import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import stream.StreamHandler;

public class VolumeControl {
    private int volume = 0;
    private volatile boolean isManualChange = false;
    private MediaPlayerModel mediaPlayerModel;

    public VolumeControl(int volume, MediaPlayerModel mediaPlayerModel) {
        this.mediaPlayerModel = mediaPlayerModel;
        this.volume = volume;
        setVolume(volume);
        startVolumeCheckInBackground();
    }

    public void setVolume(int newVolume) {
        setIsManualChange(true);
        adjustVolume((float) newVolume);
        setIsManualChange(false);
    }

    public void adjustVolume(float volume) {
        try {
            String command = "amixer set Master " + volume + "%";
            @SuppressWarnings("deprecation")
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor(); // Дожидаемся завершения выполнения команды
            this.volume = (int) volume;
            System.out.println("Volume set to: " + volume + "%");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error adjusting volume: " + e.getMessage());
        }
    }

    public float getCurrentVolume() {
        try {
            String command = "amixer get Master";
            @SuppressWarnings("deprecation")
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mono") || line.contains("Front Left")) {
                    // В строках amixer содержится информация о громкости
                    // Пример строки: " Front Left: Playback 85 [85%] [on]"
                    String[] tokens = line.split("\\[");
                    if (tokens.length > 1) {
                        String volumeString = tokens[1].replace("%]", "").trim();
                        return Float.parseFloat(volumeString);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error getting volume: " + e.getMessage());
        }
        return -1; // Возвращаем -1 в случае ошибки
    }

    public void startVolumeCheckInBackground() {
        StreamHandler.startStreamWithWhile(_ -> {
            if (!getIsManualChange()) {
                try {
                    float currentVolume = getCurrentVolume();
                    if (currentVolume != -1) {
                        if (currentVolume != volume) {
                            this.volume = (int) currentVolume;
                            mediaPlayerModel.notifyObservers(observer -> observer.setVolume(volume));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error checking volume: " + e.getMessage());
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("Volume check thread interrupted: " + e.getMessage());
                    return;
                }
            }
        }, true);
    }

    public void setIsManualChange(boolean isManualChange) {
        synchronized (this) {
            this.isManualChange = isManualChange;
        }
    }

    public boolean getIsManualChange() {
        synchronized (this) {
            return isManualChange;
        }
    }
}
