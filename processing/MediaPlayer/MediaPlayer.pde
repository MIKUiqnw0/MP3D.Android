import ddf.minim.*;

AudioModule audio;
VisualCube cubeDemo, cubeDemo2;

void setupAudio() {
  audio = new AudioModule(this);
  audio.playTrack();
}

void setup() {
  size(600, 600, P3D);
  setupAudio();
  cubeDemo = new VisualCube(200, 200, 0);
  cubeDemo2 = new VisualCube(width - 200, height - 200, 0);
}

void draw() {
  background(0);
  lights();
  
  cubeDemo.update();
  cubeDemo2.update();
}


