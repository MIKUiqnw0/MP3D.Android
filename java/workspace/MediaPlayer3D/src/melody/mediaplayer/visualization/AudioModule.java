package melody.mediaplayer.visualization;

import processing.core.PApplet;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

/**
 * AudioModule.java
 * Handles track interaction through the Minim processing library.
 * <p>
 * TODO: Research PD library as alternative. Minim is not supported by Android
 * due to its internal usage of the JavaSound API.
 * @author MIKUiqnw0
 */
public class AudioModule {
	Minim minim;
	AudioPlayer song;
	
	AudioModule(PApplet applet) {
		minim = new Minim(applet);
		song = minim.loadFile("track.mp3");
	}

	public void playTrack() {
		song.play();
	}
	  
	public void stopTrack() {
		song.pause();
		song.rewind();
	}
	  
	public void pauseTrack() {
		song.pause();
	}  
	
	public AudioPlayer getTrack() {
		return song;
	}
}
