package sample;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.FileNotFoundException;


public class Game extends Page {


    private Group pen;
    private final int MinX = 100;
    private final int MinY = 100;
    public static final int LENGTH = 50; // Side Lenght of a hexagon
    private final double H = Math.sqrt( 3 )*LENGTH/2; // Height of a hexagon
    private final int rows = 10;
    private final int columns = 10;


    private Cell[][] cells;

    public Game(GridPane root) {
        this.root = root;
        pen = new Group();
        root.add( pen, 0, 0 );

        cells = new Cell[rows][columns];

        createComponents();
        createRelations(  );
        draw();

        try {
            Pawn p = new Pawn(cells[9][9], 0);
            p.draw(pen);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createRelations() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if( j+1 < columns ) connect( cells[i][j], cells[i][j+1], 0 );
                if( j+1 < columns && i+1 < rows ) connect( cells[i][j], cells[i+1][j+1], 1 );
                if( i+1 < rows ) connect( cells[i][j], cells[i+1][j], 2 );
            }
        }
    }

    private void connect(Cell a, Cell b, int num) {
        a.addAdj( num, b );
        b.addAdj( num+3, a );
    }

    private void createComponents() {
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                double x = MinX + 1.5*LENGTH*j;
                double y = MinY + 2*H*i;
                if( j%2 == 1 ) y -= H;
                cells[i][j] = new Cell( x, y, pen );
            }
        }

    }

    public void draw() {
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                cells[i][j].draw();
            }
        }
    }
}
