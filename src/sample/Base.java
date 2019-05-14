package sample;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Base {
    private final int width = 30;
    private final int height = 30;
    private int x;
    private int y;
    public Cell cell;
    public int health;
    public Rectangle shape;

    public Base(){
        health = 3;
        x -= width/2;
        y -= height/2;
        shape = new Rectangle(x, y, width, height);
        shape.setFill(Paint.valueOf("BLACK"));
    }

    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
}
