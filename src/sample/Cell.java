package sample;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.HashMap;

public class Cell extends Parent {
    public double x, y;
    public Group root;

    public final int EMPTY = 0;
    public final int PAWN = 1;
    public final int PAWN2 = 2;
    public final int LAVA = 3;
    public final int BLOCK = 4;

    public int contains = 0;

    public HashMap< Integer, Cell > adj;

    public Cell(double x, double y, Group root) {
        adj = new HashMap<>();
        this.root = root;
        this.x = x;
        this.y = y;
    }

    void addAdj( int x, Cell c ) {
        adj.put( x, c );
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public class X extends Parent {
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }
    }

    public void draw(  ) {
        Polyline hexagon = new Polyline();

        double theta = 0;
        for(int i=0;i<=6;i++) {
            hexagon.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            hexagon.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            theta += Math.PI / 3;
        }


        if( contains == EMPTY ) hexagon.setFill(Color.WHITE);
        if( contains == PAWN ) hexagon.setFill(Color.WHEAT);
        if( contains == PAWN2 ) hexagon.setFill(Color.DARKGRAY);
        if( contains == LAVA ) hexagon.setFill(Color.AZURE);
        if( contains == BLOCK ) hexagon.setFill(Color.BLACK);

        hexagon.setStroke(Color.BLACK);

        root.getChildren().addAll( hexagon );
    }



}
