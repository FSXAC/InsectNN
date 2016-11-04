class Insect {
  // insect properties
  private PVector position;
  private float heading;
  private float speed;

  // vision
  private final int vision_range = 50;
  private PVector[] visions = new PVector[5];

  // NeuralNetwork
  private NeuralNetwork nn = new NeuralNetwork(5, 2, 5);
  private Axon[] nnOut = new Axon[2];
  private int[] input = new int[5];

  Insect() {
    position = new PVector((road.get(height - 20).x + road.get(height - 20).y) / 2, height - 20);
    heading = 2 * PI;
    speed = 1;

    // instantiate vision points
    for (int i = 0; i < 5; i++) {
      visions[i] = new PVector(0, 0);
    }
  }

  void display() {
    noFill();
    if (isOnRoad(position.x, position.y)) stroke(0, 255, 0);
    else stroke(255, 0, 0);
    ellipse(position.x, position.y, 20, 20);

    // draw vision vector
    displayVision();

    displayInfo();
    update();
  }

  void displayVision() {
    for (PVector v:visions) {
      // visual cue to see if a vision is detecting off roads
      if (isOnRoad(v.x, v.y)) stroke(0, 255, 0);
      else stroke(255, 0, 0);

      ellipse(v.x, v.y, 5, 5);
      line(v.x, v.y, position.x, position.y);
    }
  }

  void displayInfo() {
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

    // change speed and heading based on NN
    nnOut = nn.run(input);
    changeSpeed(nnOut[0].get());
    changeHeading(nnOut[1].get());
  }

  private void updateVision() {
    // change vision vectors
    float range_mult;
    for (int i = -2; i <= 2; i++) {
      range_mult = (i == 0) ? 1 : 1.0 / abs(i);
      visions[i + 2].x = position.x + sin(heading + (i * PI / 4)) * vision_range * range_mult;
      visions[i + 2].y = position.y - cos(heading + (i * PI / 4)) * vision_range * range_mult;

      // input to neural network
      input[i + 2] = isOnRoad(visions[i + 2].x, visions[i + 2].y) ? 0 : 1;
    }
  }

  // TODO: MAKE A METHOD that collects all NN inputs

  public void changeSpeed(float d_speed) {
    speed += d_speed;
  }

  public void changeHeading(float d_heading) {
    heading += d_heading;
    if (heading > 2 * PI) heading -= 2 * PI;
    else if (heading < 0) heading = 2 * PI - heading;
  }
}
