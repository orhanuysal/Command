package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Light;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Cell extends Parent {
    public double x, y;
    public int idx, idy;
    public Group root;

    public static final int EMPTY = 0;
    public static final int PAWN = 1;
    public static final int PAWN2 = 2;
    public static final int LAVA = 3;
    public static final int BLOCK = 4;
    public static final int BASE = 5;

    public int contains = 0;
    public int isSelected = 0;
    public int isPossible = 0;
    public Polygon hexagon;
    public Game game;

    public HashMap< Integer, Cell > adj;
    public boolean isRotatable;

    public Cell(double x, double y, int idx, int idy, Group root, Game game) {
        this.game = game;
        isRotatable = false;
        this.idx = idx;
        this.idy = idy;
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

    void setState( int val ) {
        isSelected = val;
        decideColor();
    }

    void setIsPossible( int c ) {
        isPossible = c;
        decideColor();
    }

    void setContains( int val ) {
        contains = val;
        decideColor();
    }

    void decideColor() {

        if( isPossible == 0 ) hexagon.setFill(new Color( 1 , 1, 1, 0.9));
        if( isPossible == 1 ) hexagon.setFill(new Color( 0.0, 0.9, 0.0, 0.2 ));

        if( isSelected == 1 ) hexagon.setFill(new Color( 0.2, 0.5, 0.5, 0.3 ));

        if( contains == BASE) hexagon.setFill(Color.BLACK);
        if( contains == LAVA ) hexagon.setFill( Color.RED );
        if( contains == BLOCK ) hexagon.setFill( Color.GRAY );
    }

    public void draw(  ) {
        hexagon = new Polygon();
        Polyline borders = new Polyline();

        double theta = Math.PI / 6;
        for(int i=0;i<=6;i++) {
            hexagon.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            hexagon.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            borders.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            borders.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            theta += Math.PI / 3;
        }

        hexagon.setFill( Color.BROWN );
        hexagon.setStroke( Color.BLACK );

        hexagon.setOnMouseClicked(event -> {
            System.out.printf( "Clicked!! %d %d\n", idx, idy );
            clicked();
        });
        root.getChildren().addAll( hexagon );
    }

    public void clicked() {
        game.setSelect( idx, idy );
    }

}
