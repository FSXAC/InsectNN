class Chunk {
  PVector location;
  float growth;
  float vegitation = 0;
  float maxveg;

  private final int low_cutoff = 55;
  private final int high_cutoff = 190;
  private final float default_grow_rate = 1;

  Chunk(float g, int x, int y) {
    location = new PVector(x * TERRAIN_SIZE, y * TERRAIN_SIZE);
    growth  = default_grow_rate;
    if (g < low_cutoff) maxveg = 0;
    else if (g > high_cutoff) maxveg = 255;
    else maxveg = g;
  }

  void display() {
    fill(vegitation);
    rect(location.x, location.y,
      location.x + TERRAIN_SIZE, location.y + TERRAIN_SIZE);
    update();
  }

  void update() {
    if (vegitation < maxveg) {
      vegitation += growth;
    }
  }
}
