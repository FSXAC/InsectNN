ArrayList<Chunk> land = new ArrayList<Chunk>();

int TERRAIN_SIZE = 50;
int landwidth;
int landlength;

void setup() {
  size(1000, 1000);
  noStroke();

  landwidth = width / TERRAIN_SIZE;
  landlength = height / TERRAIN_SIZE;

  generateLand(0, 0.15);
}

void draw() {
  background(0);
  displayLand();
}

void generateLand(float seed, float frequency) {
  noiseDetail(2);
  for (int y = 0; y < landlength; y++) {
    for (int x = 0; x < landwidth; x++) {
      land.add(new Chunk(noise(x * frequency, y * frequency) * 255, x, y));
    }
  }
}

void displayLand() {
  for (int y = 0; y < landlength; y++) {
    for (int x = 0; x < landwidth; x++) {
      land.get(y * landwidth + x).display();
    }
  }
}
