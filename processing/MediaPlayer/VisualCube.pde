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
    
    theta += 0.01;
  }
  
  private void prePulse() {
    float scale = 200.0;
    
     volumeAverage = (audio.getTrack().left.get(0)*scale) +
                     (audio.getTrack().right.get(0)*scale) / 2.0;
                          
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
