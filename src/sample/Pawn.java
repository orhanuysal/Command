package sample;

public class Pawn {

    public Cell c;
    public int direction = 0;
    public int team;

    public Pawn( Cell c, int team ) {
        this.c = c;
        direction = 0;
        this.team = team;
    }

    public void draw() {
        double x = c.getX();
        double y = c.getY();

    }

}
