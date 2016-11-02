ArrayList<Chunk> land = new ArrayList<Chunk>();

int MIN_SIZE = 10;
int MAX_SIZE = 100;

void setup() {
  size(1000, 800);
  noStroke();
  for (int i = 0; i  < 10; i++) {
    land.add(new Chunk(random(0.1, 3)));
  }
}

void draw() {
  background(0);
  for (int i = 0; i < 10; i++) {
    land.get(i).display();
  }
}

void saveLand(int min, int max, int n) {
  String entries = "";
  float x1, y1;
  for (int i = 0; i < n; i++) {
    entries += str(random(0.1, 3)); // saving g

    // pvector 1
    x1 = random(0, width - min);
    y1 = random(0, height - min);
    entries += ',' + str(x1);
    entires += ',' + str(y1);

    // pvector 2
    entires += ',' + str(x1 + random(max));
    entries += ',' + str(y1 + random(max));

    // TODO: continue here
  }
}
