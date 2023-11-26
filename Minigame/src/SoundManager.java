import javax.sound.sampled.*;
import java.io.*;

public class SoundManager {
    Clip bgmClip;
    Clip effectClip;

    public void loadBGM(String bgmFilePath) {
        try {
            // 상대 경로 사용
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
        		    getClass().getClassLoader().getResourceAsStream(bgmFilePath));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playBGM() {
        if (bgmClip != null) {
            bgmClip.start();
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
        }
    }

    public void loadEffect(String effectFilePath) {
        try {
        	// 상대 경로 사용
        	AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
        		    getClass().getClassLoader().getResourceAsStream(effectFilePath));
        	effectClip = AudioSystem.getClip();
        	effectClip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playEffect() {
        if (effectClip != null) {
            // 이전 효과음이 재생 중이라면 중지
            if (effectClip.isRunning()) {
                effectClip.stop();
            }
            effectClip.setFramePosition(0);  // 효과음을 처음부터 재생
            effectClip.start();
        }
    }
    
    public void stopAllSounds() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close(); // 리소스 해제
        }
        if (effectClip != null) {
            effectClip.stop();
            effectClip.close(); // 리소스 해제
        }
    }
}