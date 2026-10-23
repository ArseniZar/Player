package src.main.java.volume;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;

public class VolumeControl {

    private int currentVolume = 50;
    private FloatControl gainControl;

    public void setAudioLine(Line line) {
        if (line != null && line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            this.gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            applyVolumeToLine(this.currentVolume);
        } else {
            this.gainControl = null;
        }
    }

    public void setVolume(int volume) {
        this.currentVolume = Math.max(0, Math.min(100, volume));
        applyVolumeToLine(this.currentVolume);
    }

    public int getVolume() {
        return currentVolume;
    }

    private void applyVolumeToLine(int volumePercent) {
        System.out.println("Applying volume: " + volumePercent + "%");
        if (gainControl == null) {
            return;
        }

        if (volumePercent <= 0) {
            gainControl.setValue(gainControl.getMinimum());
            return;
        }

        float minDb = gainControl.getMinimum();
        float maxDb = gainControl.getMaximum(); 

        float gain = (float) (Math.log10(volumePercent / 100.0) * 20.0);

        float targetDb = Math.max(minDb, Math.min(maxDb, gain));

        gainControl.setValue(targetDb);
    }
}