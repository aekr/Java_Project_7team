//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import java.io.File;
//import java.io.IOException;
//
//public class BackgroundMusic {
//    private Clip clip;
//
//    public void playBackgroundMusic(String filePath) {
//        try {
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(filePath));
//            clip = AudioSystem.getClip();
//            clip.open(audioIn);
//            clip.loop(Clip.LOOP_CONTINUOUSLY); // 배경음악을 무한 반복
//        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stopBackgroundMusic() {
//        if (clip != null && clip.isRunning()) {
//            clip.stop();
//        }
//    }
//}
