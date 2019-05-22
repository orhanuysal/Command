package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    public boolean isRotatable = false;
    public Polygon hexagon;
    public Game game;
    private Image lava, block;

    {
        try {
            lava = new Image(new FileInputStream("resources/lava.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HashMap< Integer, Cell > adj;

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
        if( contains == EMPTY ) {
            this.hexagon.setStroke( Color.BLACK );
            this.hexagon.setStrokeWidth(1);
            this.toBack();
        }
        if( contains == BASE) hexagon.setFill(Color.BLACK);
        if( contains == LAVA ) {
            hexagon.setFill( new ImagePattern(lava));
            this.hexagon.setStrokeWidth(3);
            this.hexagon.setStroke( new Color((float)137/255, 0.0, 0.0, 1) );
            this.hexagon.toFront();
        }
        if( contains == BLOCK ) {
            hexagon.setFill( Color.GRAY );
            this.hexagon.setStroke( Color.BLACK );
            this.hexagon.setStrokeWidth(5);
            this.hexagon.toFront();
        }
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
            if(event.getButton() == MouseButton.SECONDARY && isRotatable){
                rotate();
            }
        });
        root.getChildren().addAll( hexagon );
    }

    public void rotate() {
        if( this.adj.size() == 6 ) {
            ArrayList<Integer> hold = new ArrayList<>();
            int beg = -1;
            for(Map.Entry<Integer, Cell> c: this.adj.entrySet() ) {
                if( beg == -1 ) beg = c.getValue().contains;
                else hold.add(c.getValue().contains);
            }
            hold.add( beg );
            beg = 0;
            for(Map.Entry<Integer, Cell> c: this.adj.entrySet() )
                c.getValue().setContains( hold.get( beg++ ) );

        }
    }

    public void clicked() {
        game.setSelect( idx, idy );
    }

}
