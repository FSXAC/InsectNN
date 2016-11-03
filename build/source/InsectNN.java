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

  if (!isOnRoad(new PVector(mouseX, mouseY))) ellipse(mouseX, mouseY, 20, 20);
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

// returns true if a test point is on the road (within the center road)
public boolean isOnRoad(PVector point) {
  if (point.y < height) {
    return (point.x >= road.get(PApplet.parseInt(point.y)).x && point.x <= road.get(PApplet.parseInt(point.y)).y);
  } else {
    return false;
  }
}
class Insect {
  // insect properties
  private PVector position;
  private float heading;
  private float speed;

  // vision
  private final int vision_range = 50;
  private PVector[] visions = new PVector[5];

  Insect() {
    position = new PVector(width / 2, 900);
    heading = 0;
    speed = 1;

    // instantiate vision points
    for (int i = 0; i < 5; i++) {
      visions[i] = new PVector(0, 0);
    }
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
    for (PVector v:visions) {
      // visual cue to see if a vision is detecting off roads
      if (isOnRoad(v)) stroke(0, 255, 0);
      else stroke(255, 0, 0);

      ellipse(v.x, v.y, 5, 5);
      line(v.x, v.y, position.x, position.y);
    }
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
    float range_mult;
    for (int i = -2; i <= 2; i++) {
      range_mult = (i == 0) ? 1 : 1.0f / abs(i);
      visions[i + 2].x = position.x + sin(heading + (i * PI / 4)) * vision_range * range_mult;
      visions[i + 2].y = position.y - cos(heading + (i * PI / 4)) * vision_range * range_mult;
    }
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
