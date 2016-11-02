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
