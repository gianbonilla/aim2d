import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Sound {
    Clip clip;
    URL soundURL[] = new URL[2];

    public Sound() {
        soundURL[0] = getClass().getResource("sound/osuHitSound.wav"); // osu hit SFX
        soundURL[1] = getClass().getResource("sound/lostHeartSound.wav"); // undertale taking damage SFX
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {

        }
    }

    public void play() {
        clip.start();
    }
}
