package melody.mediaplayer.visualization;
import processing.core.PApplet;

/**
 * VisualCage.java<br>
 * Instructions for displaying various geometries and modifying them
 * based on the volume/power from a specific frequency of an audio stream. 
 * @author MIKUiqnw0
 */
class VisualCage {
	private float x, y, z;
	private float theta;
	private float volumeStore;
	private float volumeAverage;
	private PApplet applet;
	private AudioModule audio;
	  
	VisualCage(float posX, float posY, float posZ, PApplet applet, AudioModule audio) {
		this.x = posX;
		this.y = posY;
		this.z = posZ;
		volumeStore = 0;
		this.applet = applet;
		this.audio = audio;
	}
  
	void update() {
	    prePulse();
	    applet.pushMatrix();
	    applet.translate(x, y, z);
	    applet.rotateY(theta);
	    applet.rotateX(theta);
	    applet.noFill();
	    applet.stroke(255);
	    applet.sphere(volumeStore);
	    applet.popMatrix();
	    
	    applet.pushMatrix();
	    applet.translate(x, y, z);
	    applet.rotateY(theta);
	    applet.rotateX(theta);
	    applet.noFill();
	    applet.stroke(255);
	    applet.box(volumeStore + 50);
	    applet.popMatrix();
	    
	    applet.pushMatrix();
	    applet.translate(x, y, z);
	    applet.rotateY(theta);
	    applet.rotateX(theta);
	    applet.noStroke();
	    applet.fill(255);
	    applet.sphere(volumeAverage);
	    applet.popMatrix();
	    
	    theta += 0.01f;
	}
  
	private void prePulse() {
		float scale = 200.0f;
  
		volumeAverage = (audio.getTrack().left.get(0)*scale) +
						(audio.getTrack().right.get(0)*scale) / 2.0f; 
   
		if(volumeStore > volumeAverage) {
			volumeStore -= 3;
		} else {
			volumeStore = volumeAverage;
		}
	}
  
	void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
  
}

