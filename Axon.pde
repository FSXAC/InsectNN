// WARNING: THE FOLLOWING CODE IS EXTREMELY INEFFICIENT
// I'D RATHER USE LINER ALGEBRA BUT I DON'T KNOW HOW TO
// CONTINUE IF YOU WANT YOUR EYES TO BLEED

class NeuralNetwork {
  private int input_size, output_size;
  // private int hidden_layers;
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

  void initializeLayers() {
    // assign random bias for axons
    for (int i = 0; i < hidden_size; i++) {
      hidden[i] = new Axon(random(-1, 1));
    }
    for (int i = 0; i < output_size; i++) {
      output[i] = new Axon(random(-1, 1));
    }

    // assign random weights for connections
    for (int i = 0; i < to_hidden; i++) {
      w0[i] = random(-1, 1);
    }
    for (int i = 0; i < to_output; i++) {
      w1[i] = random(-1, 1);
    }
  }

  // forward prop a bunch of times
  public Axon[] run(int[] in) {
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

  void displayNN(int[] in) {
    pushMatrix();
    translate(220, 30);

    // draw rectangle
    noStroke();
    fill(0, 0, 50, 100);
    rect(-20, -20, 180, max(input_size, hidden_size, output_size) * 25 + 20);
    noFill();

    // draw input layer
    for (int i = 0; i < input_size; i++) {
      fill(map(in[i], -1, 1, 0, 255));
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
    value = new_value * bias;
  }

  public float get() {
    return value;
  }
}
