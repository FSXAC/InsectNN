public ArrayList<PVector> road = new ArrayList<PVector>();
final int MIN_ROADWIDTH = 50;
final int MAX_ROADWIDTH = 130;
final int MIN_ROADCENTER = 100;
final int MAX_ROADCENTER = 300;
public Insect DUT;

void setup() {
  size(400, 1000);
  noFill();

  generateRoad(31415, 0.005, 0.005);

  DUT = new Insect();
}

void draw() {
  background(0);
  drawRoad();

  DUT.display();
}

// keyboard inputs
void keyPressed() {
  if      (key == 'a') DUT.changeHeading(-0.1);
  else if (key == 'd') DUT.changeHeading(0.1);

  if      (key == 'w') DUT.changeSpeed(0.1);
  else if (key == 's') DUT.changeSpeed(-0.1);
}
