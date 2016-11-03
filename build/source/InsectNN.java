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

Insect DUT;

public void setup() {
  
  noStroke();

  generateRoad(31415, 0.005f);

  DUT = new Insect();
}

public void draw() {
  background(0);
  drawRoad();

  DUT.display();
}

public void generateRoad(long seed, float frequency) {
  noiseSeed(seed);
  for (int row = 0; row < height; row++) {
    road.add(new PVector(map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));
  }
}

public void drawRoad() {
  for (int row = 0; row < height; row++) {
    stroke(255);
    point(road.get(row).x, row);
    point(road.get(row).y, row);
  }
}

class Insect {
  PVector position;
  float heading;
  float speed;

  PVector visionL, visionLC, visionC, visionRC, visionR;

  private final int visionC_range = 80;
  private final int visionM_range = 50;
  private final int visionS_range = 30;

  Insect() {
    position = new PVector(width / 2, 900);
    heading = 0;
    speed = 1;

    // instantiate vision points
    visionL  = new PVector(0, 0);
    visionLC = new PVector(0, 0);
    visionC  = new PVector(0, 0);
    visionRC = new PVector(0, 0);
    visionR  = new PVector(0, 0);
  }

  public void display() {
    noFill();
    stroke(255, 0, 0);
    ellipse(position.x, position.y, 20, 20);

    // draw vision vector
    displayVision();

    displayInfo();
    update();
  }

  public void displayVision() {
    ellipse(visionL.x, visionL.y, 10, 10);
    ellipse(visionLC.x, visionLC.y, 10, 10);
    ellipse(visionC.x, visionC.y, 10, 10);
    ellipse(visionRC.x, visionRC.y, 10, 10);
    ellipse(visionR.x, visionR.y, 10, 10);
    line(visionL.x, visionL.y, position.x, position.y);
    line(visionLC.x, visionLC.y, position.x, position.y);
    line(visionC.x, visionC.y, position.x, position.y);
    line(visionRC.x, visionRC.y, position.x, position.y);
    line(visionR.x, visionR.y, position.x, position.y);
  }

  public void displayInfo() {
    text("X: " + position.x + ", Y: " + position.y, 5, 10);
    text("Heading: " + heading, 5, 20);
    text("FPS: " + frameRate, 5, 30);
  }

  private void update() {
    heading = map(mouseX, 0, width, 0, 2 * PI);

    // move the insect in a direction at a certain speed
    position.x +=   sin(heading) * speed;
    position.y += - cos(heading) * speed;

    // update vision points
    updateVision();
  }

  private void updateVision() {
    // change vision vectors
    // [L]
    visionL.x = position.x + sin(heading - PI / 2) * visionS_range;
    visionL.y = position.y - cos(heading - PI / 2) * visionS_range;

    // [LC]
    visionLC.x = position.x + sin(heading - PI / 4) * visionM_range;
    visionLC.y = position.y - cos(heading - PI / 4) * visionM_range;

    // [C]
    visionC.x = position.x + sin(heading) * visionC_range;
    visionC.y = position.y - cos(heading) * visionC_range;

    // [RC]
    visionRC.x = position.x + sin(heading + PI / 4) * visionM_range;
    visionRC.y = position.y - cos(heading + PI / 4) * visionM_range;

    // [R]
    visionR.x = position.x + sin(heading + PI / 2) * visionS_range;
    visionR.y = position.y - cos(heading + PI / 2) * visionS_range;
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
