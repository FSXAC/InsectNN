void generateRoad(long seed, float width_freq, float center_freq) {
  noiseSeed(seed);
  for (int row = 0; row < height; row++) {
    // road.add(new PVector(
    //   map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    //   map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));

    // get width of the road
    float roadwidth  = map(noise(row * width_freq), 0, 1, MIN_ROADWIDTH, MAX_ROADWIDTH);

    // get center of the road
    float roadcenter = map(noise(row * center_freq), 0, 1, MIN_ROADCENTER, MAX_ROADCENTER);

    road.add(new PVector(
      roadcenter - roadwidth / 2, roadcenter + roadwidth / 2
      ));

    // road.add(new PVector(
    //   map(noise(row * frequency), 0, 1, ROADMARGIN, width / 2),
    //   map(noise(row * frequency + 1), 0, 1, width / 2, width - ROADMARGIN)));
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
  if (point.y < height && point.y >= 0) {
    return (point.x >= road.get(int(point.y)).x && point.x <= road.get(int(point.y)).y);
  } else {
    return false;
  }
}
