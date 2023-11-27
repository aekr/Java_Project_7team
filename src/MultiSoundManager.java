import javax.sound.sampled.*;
import java.io.*;
import java.util.*;

public class MultiSoundManager {
    private Map<String, Clip> clips; // Clip 객체를 저장할 맵

    public void SoundManager() {
        clips = new HashMap<>();
    }

    public void loadSound(String name, String path) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getClassLoader().getResourceAsStream(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clips.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playSound(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            clip.start();
        }
    }

    public void stopSound(String name) {
        Clip clip = clips.get(name);
        if (clip != null) {
            clip.stop();
        }
    }

    public void stopAllSounds() {
        for (Clip clip : clips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }
}