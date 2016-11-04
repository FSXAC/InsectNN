public ArrayList<PVector> road = new ArrayList<PVector>();
public ArrayList<PVector> trail = new ArrayList<PVector>();

final int SCREEN_WIDTH = 400;
final int SCREEN_HEIGHT = 1000;

final int MIN_ROADWIDTH = 50;
final int MAX_ROADWIDTH = 80;
final int MIN_ROADCENTER = MAX_ROADWIDTH / 2;
final int MAX_ROADCENTER = SCREEN_WIDTH - MIN_ROADCENTER;
public Insect DUT;

void setup() {
  size(400, 1000);
  noFill();
  background(0);

  generateRoad(31415, 0.008, 0.003);

  DUT = new Insect();
}

void draw() {
  background(0);

  drawRoad();
  // drawTrail();

  DUT.display();
}

// keyboard inputs
void keyPressed() {
  if      (key == 'a') DUT.changeHeading(-0.1);
  else if (key == 'd') DUT.changeHeading(0.1);

  if      (key == 'w') DUT.changeSpeed(0.1);
  else if (key == 's') DUT.changeSpeed(-0.1);
}

void mousePressed() {
  DUT = new Insect();
}
