package processing.test.mediaplayer;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MediaPlayer extends PApplet {



AudioModule audio;
VisualCube cubeDemo, cubeDemo2;

public void setupAudio() {
  audio = new AudioModule(this);
  audio.playTrack();
}

public void setup() {
 
  setupAudio();
  cubeDemo = new VisualCube(200, 200, 0);
  cubeDemo2 = new VisualCube(width - 200, height - 200, 0);
}

public void draw() {
  background(0);
  lights();
  
  cubeDemo.update();
  cubeDemo2.update();
}


class AudioModule {
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
class VisualCube {
  float x, y, z;
  float theta;
  float volumeStore;
  float volumeAverage;
  
  public VisualCube(float posX, float posY, float posZ) {
    this.x = posX;
    this.y = posY;
    this.z = posZ;
    volumeStore = 0;
  }
  
  public void update() {
    prePulse();
    
    pushMatrix();
    translate(x, y, z);
    rotateY(theta);
    rotateX(theta);
    noFill();
    stroke(255);
    sphere(volumeStore);
    popMatrix();
    
    pushMatrix();
    translate(x, y, z);
    rotateY(theta);
    rotateX(theta);
    noFill();
    stroke(255);
    box(volumeStore + 50);
    popMatrix();
    
    pushMatrix();
    translate(x, y, z);
    rotateY(theta);
    rotateX(theta);
    noStroke();
    fill(255);
    sphere(volumeAverage);
    popMatrix();
    
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
  
  public void setPosition(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
}

  public int sketchWidth() { return 600; }
  public int sketchHeight() { return 600; }
  public String sketchRenderer() { return P3D; }
}
