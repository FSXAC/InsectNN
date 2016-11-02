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

int MIN_SIZE = 10;
int MAX_SIZE = 100;

public void setup() {
  
  noStroke();
  for (int i = 0; i  < 10; i++) {
    land.add(new Chunk(random(0.1f, 3)));
  }
}

public void draw() {
  background(0);
  for (int i = 0; i < 10; i++) {
    land.get(i).display();
  }
}

class Chunk {
  PVector startPt, endPt;
  float growth;
  float area;
  float vegitation = 0;

  Chunk(float g) {
    startPt = new PVector(random(0, width - MIN_SIZE), random(0, height - MIN_SIZE));
    endPt   = new PVector(random(startPt.x, startPt.x + MAX_SIZE),
                      random(startPt.y, startPt.y + MAX_SIZE));
    area = (startPt.x - endPt.x) * (startPt.y - endPt.y);
    growth = g * area / 1000;
  }

  Chunk(float g, int x1, int y1, int x2, int y2) {
    startPt = new PVector(x1, y1);
    endPt   = new PVector(x2, y2);
    area = (startPt.x - endPt.x) * (startPt.y - endPt.y);
    growth  = g * area / 1000;
  }

  public void display() {
    fill(map(vegitation, 0, area, 0, 255));
    rect(startPt.x, startPt.y, endPt.x, endPt.y);
    update();
  }

  public void update() {
    if (vegitation < area) {
      vegitation += growth;
    }
  }
}
  public void settings() {  size(1000, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#272727", "--stop-color=#cccccc", "InsectNN" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
