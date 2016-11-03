ArrayList<PVector> road = new ArrayList<PVector>();

final int ROADMARGIN = 50;

void setup() {
  size(400, 1000);
  noStroke();

  generateRoad(0, 0.005);
}

void draw() {
  background(0);
  drawRoad();
}

void mousePressed() {
  generateRoad(0, 0.002);
}

void generateRoad(float seed, float frequency) {
  for (int row = 0; row < height; row++) {
    road.add(new PVector(map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));
  }
}

void drawRoad() {
  for (int row = 0; row < height; row++) {
    stroke(255);
    line(road.get(row).x, row, road.get(row).y, row);
  }
}
