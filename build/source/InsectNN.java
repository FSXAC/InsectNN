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

ArrayList<PVector> road = new ArrayList<PVector>();

final int ROADMARGIN = 50;

public void setup() {
  
  noStroke();

  generateRoad(0, 0.005f);
}

public void draw() {
  background(0);
  drawRoad();
}

public void mousePressed() {
  generateRoad(0, 0.002f);
}

public void generateRoad(float seed, float frequency) {
  for (int row = 0; row < height; row++) {
    road.add(new PVector(map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));
  }
}

public void drawRoad() {
  for (int row = 0; row < height; row++) {
    stroke(255);
    line(road.get(row).x, row, road.get(row).y, row);
  }
}
  public void settings() {  size(400, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#272727", "--stop-color=#cccccc", "InsectNN" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
