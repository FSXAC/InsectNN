import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class InsectNN extends PApplet {

ArrayList<Chunk> land = new ArrayList<Chunk>();

int TERRAIN_SIZE = 50;
int landwidth;
int landlength;

public void setup() {
  
  noStroke();

  landwidth = width / TERRAIN_SIZE;
  landlength = height / TERRAIN_SIZE;

  generateLand(0, 0.15f);
}

public void draw() {
  background(0);
  displayLand();
}

public void generateLand(float seed, float frequency) {
  noiseDetail(2);
  for (int y = 0; y < landlength; y++) {
    for (int x = 0; x < landwidth; x++) {
      land.add(new Chunk(noise(x * frequency, y * frequency) * 255, x, y));
    }
  }
}

public void displayLand() {
  for (int y = 0; y < landlength; y++) {
    for (int x = 0; x < landwidth; x++) {
      land.get(y * landwidth + x).display();
    }
  }
}
class Chunk {
  PVector location;
  float growth;
  float vegitation = 0;
  float maxveg;

  private final int low_cutoff = 55;
  private final int high_cutoff = 190;
  private final float default_grow_rate = 1;

  Chunk(float g, int x, int y) {
    location = new PVector(x * TERRAIN_SIZE, y * TERRAIN_SIZE);
    growth  = default_grow_rate;
    if (g < low_cutoff) maxveg = 0;
    else if (g > high_cutoff) maxveg = 255;
    else maxveg = g;
  }

  public void display() {
    fill(vegitation);
    rect(location.x, location.y,
      location.x + TERRAIN_SIZE, location.y + TERRAIN_SIZE);
    update();
  }

  public void update() {
    if (vegitation < maxveg) {
      vegitation += growth;
    }
  }
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#272727", "--stop-color=#cccccc", "InsectNN" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
