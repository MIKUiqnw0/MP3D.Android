class AudioModule {
  Minim minim;
  AudioPlayer song;
  
  AudioModule(PApplet applet) {
    minim = new Minim(applet);
    song = minim.loadFile("track.mp3");
  }
  
  void playTrack() {
    song.play();
  }
  
  void stopTrack() {
    song.pause();
    song.rewind();
  }
  
  void pauseTrack() {
    song.pause();
  }  
  
  AudioPlayer getTrack() {
    return song;
  }
}
