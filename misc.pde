void generateRoad(long seed, float width_freq, float center_freq) {
  noiseSeed(seed);
  for (int row = 0; row < height; row++) {
    // get width of the road
    float roadwidth  = map(noise(row * width_freq), 0, 1, MIN_ROADWIDTH, MAX_ROADWIDTH);

    // get center of the road
    float roadcenter = map(noise((row + seed) * center_freq), 0, 1, MIN_ROADCENTER, MAX_ROADCENTER);

    road.add(new PVector(
      roadcenter - roadwidth / 2, roadcenter + roadwidth / 2
      ));
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
boolean isOnRoad(float x, float y) {
  if (y < height && y >= 0) {
    return (x >= road.get(int(y)).x && x <= road.get(int(y)).y);
  } else {
    return false;
  }
}
