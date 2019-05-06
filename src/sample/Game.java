package sample;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;


public class Game extends Page {


    private Group pen;
    private final int MinX = 100;
    private final int MinY = 100;
    public static final int LENGTH = 40; // Side Lenght of a hexagon
    private final double H = Math.sqrt( 3 )*LENGTH/2; // Height of a hexagon
    private final int rows = 10;
    private final int columns = 10;

    public int selectedX;
    public int selectedY;

    private Pawn[] pawns;

    private int turn = 0;

    private Cell[][] cells;
    private int spell;
    private boolean debug = true;

    public void setSelect( int x, int y ) {

        selectedX = x;
        selectedY = y;
        Cell c = cells[selectedX][selectedY];
        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++) {
                if( i == selectedX && j == selectedY ) cells[i][j].setState( 1 );
                else cells[i][j].setState( 0 );
            }
        if( spell > 0 ) {
            if( c.isPossible == 1 ) {
                if( spell == Cell.BLOCK ) c.setContains( Cell.BLOCK );
                if( spell == Cell.LAVA ) c.setContains( Cell.LAVA );
            }
            for(int i=0;i<rows;i++)
                for(int j=0;j<columns;j++)
                    cells[i][j].setIsPossible( 0 );
        }
    }

    public Game(GridPane root) {

        this.root = root;
        pen = new Group();
        root.add( pen, 0, 0 );

        cells = new Cell[rows][columns];

        addButtons();

        createPawns( getPlayer1Pawns(), getPlayer2Pawns() );
        createComponents();
        createRelations(  );
        draw();

//        try {
//            Pawn p = new Pawn(cells[9][9], 0);
//            p.draw(pen);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    private ArrayList<Pair<Integer,Integer>> getPlayer1Pawns() {
        ArrayList<Pair<Integer,Integer>> pawns = new ArrayList<>();
        // doldur
        return pawns;
    }

    private ArrayList<Pair<Integer,Integer>> getPlayer2Pawns() {
        ArrayList<Pair<Integer,Integer>> pawns = new ArrayList<>();
        // doldur
        return pawns;
    }

    private void createPawns(ArrayList<Pair<Integer,Integer>> locs, ArrayList<Pair<Integer,Integer>> locs2 ) {



    }

    private void addButtons() {
        pen.getChildren().addAll(   createButon("Guard", 40, event -> {guard();}),
                                    createButon("Burn", 80, event -> {burn();}),
                                    createButon("Speed", 120, event -> {speed();}),
                                    createButon("Portal", 160, event -> {portal();}),
                                    createButon("Rotate", 200, event -> {rotate();}),
                                    createButon("Range", 240, event -> {range();}),
                                    createButon("Finsih turn", 280, event -> {proceed();})
        );
    }


    private void guard() {

        Cell selected = cells[selectedX][selectedY];
        int p = Cell.PAWN;
        if( turn == 1 ) p = Cell.PAWN2;
        if( debug || selected.contains == p ) {

            for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() )
                if( c.getValue().contains == Cell.EMPTY )
                    c.getValue(  ).setIsPossible( 1 );

            spell = Cell.BLOCK;
        }
    }

    private void burn() {

        Cell selected = cells[selectedX][selectedY];
        int p = Cell.PAWN;
        if( turn == 1 ) p = Cell.PAWN2;
        if( debug || selected.contains == p ) {
            for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() )
                if( c.getValue().contains == Cell.EMPTY )
                    c.getValue(  ).setIsPossible( 1 );

            spell = Cell.LAVA;
        }
    }

    private void speed() {

        Cell selected = cells[selectedX][selectedY];
        int p = Cell.PAWN;
        if( turn == 1 ) p = Cell.PAWN2;

        if (debug || selected.contains == p) {

        }
    }

    private void portal() {
        Cell selected = cells[selectedX][selectedY];
        int p = Cell.PAWN;
        if( turn == 1 ) p = Cell.PAWN2;

        if (debug || selected.contains == p) {

        }
    }

    private void rotate() {
        Cell selected = cells[selectedX][selectedY];
        int p = Cell.PAWN;
        if( turn == 1 ) p = Cell.PAWN2;

        if (debug || selected.contains == p) {
            if( selected.adj.size() == 6 ) {
                System.out.println("On Rotate!!\n");
                ArrayList<Integer> hold = new ArrayList<>();
                int beg = -1;
                for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() ) {
                    if( beg == -1 ) beg = c.getValue().contains;
                    else hold.add(c.getValue().contains);
                }
                hold.add( beg );
                beg = 0;
                for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() )
                    c.getValue().setContains( hold.get( beg++ ) );

            }
        }
    }

    private void range() {

    }

    private void proceed() {

        turn ^= 1;
    }



    private void createRelations() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if( j % 2 == 0 ) {
                    if (j + 1 < columns) connect(cells[i][j], cells[i][j + 1], 0);
                    if (j + 1 < columns && i + 1 < rows) connect(cells[i][j], cells[i + 1][j + 1], 1);
                    if (i + 1 < rows) connect(cells[i][j], cells[i + 1][j], 2);
                } else {
                    if (i > 0 && j + 1 < columns) connect(cells[i][j], cells[i-1][j + 1], 0);
                    if (j + 1 < columns) connect(cells[i][j], cells[i][j + 1], 1);
                    if (i + 1 < rows) connect(cells[i][j], cells[i + 1][j], 2);
                }
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
                cells[i][j] = new Cell( x, y, i, j, pen, this );
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
