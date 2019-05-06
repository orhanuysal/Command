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

//    public static final int EMPTY = 0;
//    public static final int SELECTED = 1;
//    public static final int POSSIBLE = 2;

    public int isSelected = 0;
    public int isPossible = 0;
    public Polygon hexagon;
    public Game game;

    public HashMap< Integer, Cell > adj;

    public Cell(double x, double y, int idx, int idy, Group root, Game game) {
        this.game = game;
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

    public class X extends Parent {
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }
    }

    void setState( int val ) {
        isSelected = val;

        if( isPossible == 0 ) hexagon.setFill(new Color( 0.0, 0.0, 0.0, 0.0 ));
        if( isPossible == 1 ) hexagon.setFill(new Color( 0.2, 0.5, 0.5, 0.1 ));

        if( isSelected == 1 ) hexagon.setFill(new Color( 0.0, 0.9, 0.0, 0.1 ));

    }

    public void draw(  ) {
        hexagon = new Polygon();
        Polyline borders = new Polyline();

        double theta = 0;
        for(int i=0;i<=6;i++) {
            hexagon.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            hexagon.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            borders.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            borders.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            theta += Math.PI / 3;
        }

        hexagon.setFill( Color.BROWN );
        hexagon.setStroke( Color.BLACK );

        hexagon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.printf( "Clicked!! %d %d\n", idx, idy );
                game.setSelect( idx, idy );
            }
        });


        root.getChildren().addAll( hexagon );
    }



}
