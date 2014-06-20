package melody.mediaplayer.visualization;

import processing.core.*;

/**
 * Visualizer.java
 * Entry class for Processing.<br>
 * Sets up the environment and audio modules.
 * @author MIKUiqnw0
 */
public class Visualizer extends PApplet implements CoreInterface {

	AudioModule audio;
	VisualCage cubeDemo, cubeDemo2;
	
	public void setupAudio() {
	  audio = new AudioModule(this);
	  audio.playTrack();
	}
	
	public void setup() {
	  size(1024, 768, P3D);
	  setupAudio();
	  cubeDemo = new VisualCage(200, 200, 0, this, audio);
	  cubeDemo2 = new VisualCage(width - 200, height - 200, 0, this, audio);
	}
	
	public void draw() {
	  background(0);
	  lights();
	  
	  cubeDemo.update();
	  cubeDemo2.update();
	}
	
	@Override
	public PApplet getApplet() {
		return this;
	}
}

/*static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MediaPlayer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}*/