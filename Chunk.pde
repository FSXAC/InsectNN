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

  void display() {
    fill(map(vegitation, 0, area, 0, 255));
    rect(startPt.x, startPt.y, endPt.x, endPt.y);
    update();
  }

  void update() {
    if (vegitation < area) {
      vegitation += growth;
    }
  }
}
