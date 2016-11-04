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

public ArrayList<PVector> road = new ArrayList<PVector>();

final int MIN_ROADWIDTH = 50;
final int MAX_ROADWIDTH = 80;
final int MIN_ROADCENTER = MAX_ROADWIDTH / 2;
final int MAX_ROADCENTER = 400 - MIN_ROADCENTER;
public Insect DUT;

public void setup() {
  
  noFill();
  background(0);

  generateRoad(31415, 0.008f, 0.003f);

  DUT = new Insect();
}

public void draw() {
  background(0);

  drawRoad();
  // drawTrail();

  DUT.display();
}

// keyboard inputs
public void keyPressed() {
  if      (key == 'a') DUT.changeHeading(-0.1f);
  else if (key == 'd') DUT.changeHeading(0.1f);

  if      (key == 'w') DUT.changeSpeed(0.1f);
  else if (key == 's') DUT.changeSpeed(-0.1f);
}

public void mousePressed() {
  DUT = new Insect();
}
// WARNING: THE FOLLOWING CODE IS EXTREMELY INEFFICIENT
// I'D RATHER USE LINER ALGEBRA BUT I DON'T KNOW HOW TO
// CONTINUE IF YOU WANT YOUR EYES TO BLEED

class NeuralNetwork {
  // size of nodes
  private int input_size, output_size;
  private int hidden_size;

  // size of the connections
  private int to_hidden;
  private int to_output;

  // nodes / axons
  private Axon[] hidden;
  private Axon[] output;

  // connections
  private float[] w0;
  private float[] w1;

  private final float weight_range = 1;
  private final float bias_range = 1;

  NeuralNetwork(int isize, int osize, int hsize) {
    // axon sizes
    input_size  = isize;
    hidden_size = hsize;
    output_size = osize;

    // connection sizes
    to_hidden = isize * hsize;
    to_output = hsize * osize;

    // set up array for each layer of the nodes
    hidden = new Axon[hsize];
    output = new Axon[osize];

    // set up array for connections
    w0 = new float[to_hidden];
    w1 = new float[to_output];

    // initalize
    initializeLayers();
  }

  public void initializeLayers() {
    // assign random bias for axons
    for (int i = 0; i < hidden_size; i++) {
      hidden[i] = new Axon(random(-bias_range, bias_range));
    }
    for (int i = 0; i < output_size; i++) {
      output[i] = new Axon(random(-bias_range, bias_range));
    }

    // assign random weights for connections
    for (int i = 0; i < to_hidden; i++) {
      w0[i] = random(-weight_range, weight_range);
    }
    for (int i = 0; i < to_output; i++) {
      w1[i] = random(-weight_range, weight_range);
    }
  }

  // forward prop a bunch of times
  public Axon[] run(float[] in) {
    float sum;
    for (int i = 0; i < hidden_size; i++) {
      sum = 0;
      for (int j = 0; j < input_size; j++) {
        sum += in[j] * w0[i * hidden_size + j];
      }
      hidden[i].forward(sum);
    }

    // take the hidden, and prop it to output
    for (int i = 0; i < output_size; i++) {
      sum = 0;
      for (int j = 0; j < hidden_size; j++) {
        sum += hidden[i].get() * w1[i * output_size + j];
      }
      output[i].forward(sum);
    }

    displayNN(in);
    return output;
  }

  public void displayNN(float[] in) {
    pushMatrix();
    translate(220, 30);

    // draw rectangle
    noStroke();
    fill(0, 0, 50, 100);
    rect(-20, -20, 180, max(input_size, hidden_size, output_size) * 25 + 20);
    noFill();

    // draw input layer
    for (int i = 0; i < input_size; i++) {
      fill(map(in[i], 0, 1, 0, 255));
      ellipse(0, i * 25, 20, 20);
      fill(0, 0, 255);
      text(in[i], 0, i * 25);
    }

    // connection 1
    strokeWeight(2);
    for (int j = 0; j < hidden_size; j++) {
      for (int i = 0; i < input_size; i++) {
        stroke(map(w0[j * hidden_size + i], -1, 1, 0, 255), 150);
        line(0, i * 25, 50, j * 25);
      }
    }
    noStroke();
    strokeWeight(1);

    // draw hidden layer
    for (int i = 0; i < hidden_size; i++) {
      fill(map(hidden[i].get(), -1, 1, 0, 255));
      ellipse(50, i * 25, 20, 20);
      fill(0, 0, 255);
      text(hidden[i].get(), 50, i * 25);
    }

    // connection 2
    strokeWeight(2);
    for (int j = 0; j < output_size; j++) {
      for (int i = 0; i < hidden_size; i++) {
        stroke(map(w1[j * output_size + i], -1, 1, 0, 255), 150);
        line(50, i * 25, 100, j * 25);
      }
    }
    noStroke();
    strokeWeight(1);

    // draw output layer
    for (int i = 0; i < output_size; i++) {
      fill(map(output[i].get(), -1, 1, 0, 255));
      ellipse(100, i * 25, 20, 20);
      fill(0, 0, 255);
      text(output[i].get(), 100, i * 25);
    }
    popMatrix();
  }
}

class Axon {
  private float bias;
  private float value;

  Axon(float b) {
    bias = b;
    value = 0;
  }

  // get new value
  public void forward(float new_value) {
    value = sigmoid(new_value * bias);
  }

  public float get() {
    return value;
  }

  private float sigmoid(float x) {
    return (2 / (1 + pow(3, -x)) - 1);
  }
}
class Insect {
  // insect movement properties
  private PVector position;
  private float heading;
  private float speed;
  private float max_speed = 5;

  // vision
  private final int vision_range = 50;
  private PVector[] visions = new PVector[5];

  // NeuralNetwork
  private NeuralNetwork nn = new NeuralNetwork(7, 2, 3); // (in, out, hidden)
  private Axon[] nnOut = new Axon[2];
  private float[] input = new float[7];

  Insect() {
    position = new PVector((road.get(height - 20).x + road.get(height - 20).y) / 2, height - 20);
    heading = 2 * PI;
    speed = 0;

    // instantiate vision points
    for (int i = 0; i < 5; i++) {
      visions[i] = new PVector(0, 0);
    }
  }

  public void display() {
    noFill();
    if (isOnRoad(position.x, position.y)) {
      stroke(0, 255, 0);
      max_speed = 6;
    } else {
      stroke(255, 0, 0);
      max_speed = 1;
    }
    ellipse(position.x, position.y, 20, 20);

    // draw vision vector
    displayVision();

    displayInfo();
    update();
  }

  public void displayVision() {
    for (PVector v:visions) {
      // visual cue to see if a vision is detecting off roads
      if (isOnRoad(v.x, v.y)) stroke(0, 255, 0);
      else stroke(255, 0, 0);

      ellipse(v.x, v.y, 5, 5);
      line(v.x, v.y, position.x, position.y);
    }
  }

  public void displayInfo() {
    fill(255);
    text("FPS: " + frameRate, 5, 40);
    text("X: " + position.x + ", Y: " + position.y, 5, 10);
    text("Heading: " + (heading / PI) + "PI, " + (heading / PI * 180) + "deg", 5, 20);
    text("Speed: " + speed, 5, 30);
  }

  private void update() {
    float next_x = position.x + sin(heading) * speed;
    float next_y = position.y - cos(heading) * speed;

    if (next_x > 0 && next_x < width && next_y > 0 && next_y < height) {
      // move the insect in a direction at a certain speed
      position.x = next_x;
      position.y = next_y;
    }

    // update vision points
    updateVision();

    // get input
    getInput();

    // change speed and heading based on NN
    nnOut = nn.run(input);
    changeSpeed(nnOut[0].get());
    changeHeading(nnOut[1].get());
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

  private void getInput() {
    for (int i = 0; i < 5; i++) {
      // input to neural network
      input[i] = isOnRoad(visions[i].x, visions[i].y) ? 0 : 1;
    }
    // add the vehicle itself
    input[5] = isOnRoad(position.x, position.y) ? 0 : 1;

    // add 2 more inputs as x or y
    input[6] = map(position.y, 0, height, 0, 1);
  }

  public void changeSpeed(float d_speed) {
    float new_speed = speed + d_speed;
    if (new_speed > max_speed) speed = max_speed;
    else if (new_speed < -max_speed) speed = -max_speed;
    else speed += d_speed;
  }

  public void changeHeading(float d_heading) {
    heading += d_heading;
    if (heading > 2 * PI) heading -= 2 * PI;
    else if (heading < 0) heading = 2 * PI - heading;
  }
}
public void generateRoad(long seed, float width_freq, float center_freq) {
  noiseSeed(seed);
  for (int row = 0; row < height; row++) {
    // get width of the road
    float roadwidth  = map(noise(row * width_freq), 0, 1, MIN_ROADWIDTH, MAX_ROADWIDTH);

    // get center of the road
    float roadcenter = map(noise((row + seed) * center_freq), 0, 1, MIN_ROADCENTER, MAX_ROADCENTER);

    road.add(new PVector(
      roadcenter - roadwidth / 2, roadcenter + roadwidth / 2
      ));
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
public boolean isOnRoad(float x, float y) {
  if (y < height && y >= 0) {
    return (x >= road.get(PApplet.parseInt(y)).x && x <= road.get(PApplet.parseInt(y)).y);
  } else {
    return false;
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
