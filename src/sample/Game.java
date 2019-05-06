package sample;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.ArrayList;
import java.io.FileNotFoundException;


public class Game extends Page {


    private Group pen;
    private final int MinX = 100;
    private final int MinY = 100;
    public static final int LENGTH = 40; // Side Lenght of a hexagon
    private final double H = Math.sqrt( 3 )*LENGTH/2; // Height of a hexagon
    private final int rows = 10;
    private final int columns = 10;
    private int turn;
    private int stage;
    private Player p0, p1;

    public int selectedX;
    public int selectedY;
    public Cell selected;

    private Pawn[] pawns;

    private Cell[][] cells;

    public Cell setSelect( int x, int y ) {

        selectedX = x;
        selectedY = y;
        selected = cells[selectedX][selectedY];
        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++) {
                if( i == selectedX && j == selectedY ) cells[i][j].setState( 1 );
                else cells[i][j].setState( 0 );
            }
        return selected;
    }

    public Game(GridPane root) {

        this.root = root;
        pen = new Group();
        root.add( pen, 0, 0 );

        cells = new Cell[rows][columns];
        turn = 0;
        stage = 0;


        //addButtons();

        //createPawns( getPlayer1Pawns(), getPlayer2Pawns() );
        createComponents();
        createRelations();
        draw();

        p0 = new Player();
        p1 = new Player();
        handleStage0();
    }

    /*private ArrayList<Pair<Integer,Integer>> getPlayer1Pawns() {
        ArrayList<Pair<Integer,Integer>> pawns = new ArrayList<>();
        // doldur
        return pawns;
    }

    private ArrayList<Pair<Integer,Integer>> getPlayer2Pawns() {
        ArrayList<Pair<Integer,Integer>> pawns = new ArrayList<>();
        // doldur
        return pawns;
    }*/

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

    }

    private void burn() {

    }

    private void speed() {
        //if( selectedCell ==  );
    }

    private void portal() {

    }

    private void rotate() {

    }

    private void range() {

    }

    private void proceed() {

    }

    private void handleStage0(){

        Button placePawn = createButon("Place Pawn", 40, event -> {
            if(turn == 0 && p0.pawnsToPlace != 0){
                try {
                    Pawn p = new Pawn(selected, 0);
                    p.draw(pen);
                    p0.pawnsToPlace--;
                    turn = 1;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(turn == 1 && p1.pawnsToPlace != 0){
                try {
                    Pawn p = new Pawn(selected, 1);
                    p.draw(pen);
                    p1.pawnsToPlace--;
                    turn = 0;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Stage0 completed");
                handleStage1();
            }
        });
        pen.getChildren().add(placePawn);
    }

    private void handleStage1() {

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
