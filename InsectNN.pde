ArrayList<PVector> road = new ArrayList<PVector>();

final int ROADMARGIN = 50;

Insect DUT;

void setup() {
  size(400, 1000);
  noStroke();

  generateRoad(31415, 0.005);

  DUT = new Insect();
}

void draw() {
  background(0);
  drawRoad();

  DUT.display();
}

void generateRoad(long seed, float frequency) {
  noiseSeed(seed);
  for (int row = 0; row < height; row++) {
    road.add(new PVector(map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));
  }
}

void drawRoad() {
  for (int row = 0; row < height; row++) {
    stroke(255);
    point(road.get(row).x, row);
    point(road.get(row).y, row);
  }
}

// returns true if a test point is on the road (within the center road)
boolean isOnRoad(PVector point) {
  if (point.y < height) {
    return (point.x >= road.get(int(point.y)).x && point.x <= road.get(int(point.y)).y);
  } else {
    return false;
  }
}
