// WARNING: THE FOLLOWING CODE IS EXTREMELY INEFFICIENT
// I'D RATHER USE LINER ALGEBRA BUT I DON'T KNOW HOW TO
// CONTINUE IF YOU WANT YOUR EYES TO BLEED

class NeuralNetwork {
  private int input_size, output_size;
  // private int hidden_layers;
  private int hidden_size;

  // nodes / axons
  private Axon[] input;
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
    input  = new Axon[isize];
    hidden = new Axon[hsize];
    output = new Axon[osize];

    // set up array for connections
    w0 = new float[];
    w1 = new float[];
  }

  void initializeLayers() {
    // assign random bias for axons
    for (int i = 0; i < input_size; i++) {
      input[i] = new Axon(random(0, 1));
    }
    for (int i = 0; i < hidden_size; i++) {
      hidden[i] = new Axon(random(0, 1));
    }
    for (int i = 0; i < input_size; i++) {
      output[i] = new Axon(random(0, 1));
    }

    // assign random weights for connections
    for (int i = 0; i < to_hidden; i++) {
      w0[i] = random(0, 1);
    }
    for (int i = 0; i < to_output; i++) {
      w1[i] = random(0, 1);
    }
  }

  // forward prop a bunch of times
  public float[] run(float[] input) {
    // take the input, multiply it to first hidden layer of axons
    for (int i = 0; i < input_size; i++) {
      for (int j = 0; j < hidden_size; j++) {
        // bias is automatically taken care of
        hidden[j].forward(input[i].get() * w0[i * input_size + hidden_size]);
      }
    }

    // take the hidden, and prop it to output
    for (int i = 0; i < hidden_size; i++) {
      for (int j = 0; j < output_size; j++) {
        output[j].forward(hidden[i].get() * w1[i * hidden_size + output_size]);
      }
    }
  }
}

class Axon() {
  private float bias;
  private float value;

  Axon(float b) {
    bias = 0;
    value = 0;
  }

  public void forward(float new_value) {
    value = new_value * bias;
  }

  public void get() {
    return value;
  }
}
