package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.util.Pair;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.*;
import java.io.FileNotFoundException;


public class Game extends Page {


    private Group pen;
    private final int MinX = 100;
    private final int MinY = 100;
    public static final int LENGTH = 50; // Side Lenght of a hexagon
    private final double H = Math.sqrt( 3 )*LENGTH/2; // Height of a hexagon
    private final int rows = 9;
    private final int columns = 12;
    private int stage;
    private Player p0, p1;

    public double sceneWidth = columns*LENGTH*5/3 + WIDTH;
    public double sceneHeight = rows*LENGTH*2;
    public int selectedX;
    public int selectedY;
    public Cell selected;

    private int turn = 0;

    private Cell[][] cells;
    private int spell;
    private boolean debug = false;
    private Button placePawn, guardB, burnB, speedB, rotateB, redirectB, rangeB, portalB, finishTurnB;
    private Label guardL, burnL, speedL, rotateL, redirectL, rangeL, portalL, placePawnL, p1Health, p2Health;
    private ComboBox<Integer> comboBox;
    private HashMap<Move.type, Integer> moveCounts;
    private int rotationVal;
    private double btny = 40;

    public Game(GridPane root) {

        this.root = root;

        root.setMinWidth(sceneWidth);
        root.setMinHeight(sceneHeight);
        //root.setStyle("-fx-background-color: #000000;");
        pen = new Group();
        root.add( pen, 0, 0 );
        cells = new Cell[rows][columns];
        turn = 0;
        stage = 0;
        p0 = new Player();
        p1 = new Player();

        createComponents();
        createRelations();
        draw();
        initButtons();
        drawBases();

        handleStage0();
    }

    private void initButtons(){
        guardB = new Button("Guard");
        guardB.setVisible(false);
        guardB.setOnAction(event -> {
            guard();
        });
        guardL = new Label();
        guardL.setVisible(false);

        burnB = new Button("Burn");
        burnB.setVisible(false);
        burnB.setOnAction(event -> {
            burn();
        });
        burnL = new Label();
        burnL.setVisible(false);


        speedB = new Button("Speed");
        speedB.setVisible(false);
        speedB.setOnAction(event -> {
            speed();
        });
        speedL = new Label();
        speedL.setVisible(false);

        rotateB = new Button("Rotate");
        rotateB.setVisible(false);
        rotateB.setOnAction(event -> {
            rotate();
        });
        rotateL = new Label();
        rotateL.setVisible(false);

        redirectB = new Button("Redirect");
        redirectB.setVisible(false);
        redirectB.setOnAction(event -> {
            redirect();
        });
        redirectL = new Label();
        redirectL.setVisible(false);

        rangeB = new Button("Range");
        rangeB.setVisible(false);
        rangeB.setOnAction(event -> {
            range();
        });
        rangeL = new Label();
        rangeL.setVisible(false);

        portalB = new Button("Portal");
        portalB.setVisible(false);
        portalB.setOnAction(event -> {
            portal();
        });
        portalL = new Label();
        portalL.setVisible(false);

        finishTurnB = new Button("Finish Turn");
        finishTurnB.setVisible(false);
        finishTurnB.setOnAction(event -> {
            proceed();
        });

        comboBox = new ComboBox<>();
        comboBox.getItems().addAll(1, 2, 3, 4, 5, 6);
        comboBox.setVisible(false);
        comboBox.setMaxWidth(30);
        comboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                rotationVal = newValue;
            }
        });

        p1Health = new Label();
        p2Health = new Label();

        p1Health.setLayoutY(500);
        p1Health.setText("Red base health: " + p0.base.health);

        p2Health.setLayoutY(550);
        p2Health.setText("Blue base health: " + p1.base.health);

        pen.getChildren().addAll(guardB, burnB, speedB, rotateB, redirectB, rangeB, portalB, comboBox,
                guardL, burnL, speedL, rotateL, redirectL, rangeL, portalL, p1Health, p2Health);
        //root.setMinWidth(1800);
        //System.out.println("penx: " + pen.getLayoutX() + "rootx: " + root.getMinWidth());
    }

    public Cell setSelect( int x, int y ) {

        selectedX = x;
        selectedY = y;
        Cell c = cells[selectedX][selectedY];
        selected = cells[selectedX][selectedY];
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
        return selected;
    }

    private void clearButtons(){
        for(Node node : pen.getChildren()){
            if(node.getClass() == guardB.getClass()){
                node.setVisible(false);
            }
        }
    }

    private void clearLabels(){
        for(Node node : pen.getChildren()){
            if(node.getClass() == guardL.getClass()){
                node.setVisible(false);
            }
        }
    }

    private void showButton(Button btn, Label lbl){
        btn.setLayoutY(btny); lbl.setLayoutY(btny);
        btn.setLayoutX(45); lbl.setLayoutX(btn.getLayoutX() + btn.getWidth());
        btn.setDisable(false);
        btny += HEIGHT + 10;
        btn.setVisible(true); lbl.setVisible(true);
    }

    private void fillMoveCount(ArrayList<Move> buttonsToShow){
        moveCounts = new HashMap<>();
        moveCounts.put(Move.type.REDIRECT, getFreq(buttonsToShow, Move.type.REDIRECT));
        moveCounts.put(Move.type.BURN, getFreq(buttonsToShow, Move.type.BURN));
        moveCounts.put(Move.type.GUARD, getFreq(buttonsToShow, Move.type.GUARD));
        moveCounts.put(Move.type.PORTAL, getFreq(buttonsToShow, Move.type.PORTAL));
        moveCounts.put(Move.type.RANGE, getFreq(buttonsToShow, Move.type.RANGE));
        moveCounts.put(Move.type.ROTATE, getFreq(buttonsToShow, Move.type.ROTATE));
        moveCounts.put(Move.type.SPEED, getFreq(buttonsToShow, Move.type.SPEED));
    }

    private int getFreq(ArrayList<Move> buttonsToShow, Move.type t){
        int res = 0;
        for(Move m : buttonsToShow){
            if(m.getType() == t){
                res++;
            }
        }
        return res;
    }

    private void fillLabels(){
        redirectL.setText("" + moveCounts.get(Move.type.REDIRECT));
        burnL.setText("" + moveCounts.get(Move.type.BURN));
        guardL.setText("" + moveCounts.get(Move.type.GUARD));
        portalL.setText("" + moveCounts.get(Move.type.PORTAL));
        rangeL.setText("" + moveCounts.get(Move.type.RANGE));
        rotateL.setText("" + moveCounts.get(Move.type.ROTATE));
        speedL.setText("" + moveCounts.get(Move.type.SPEED));
    }

    private void addButtons() {
        Player currentPlayer = (turn == 0) ? p0 : p1;
        System.out.println("Turn: " + turn);

        clearButtons();
        clearLabels();
        showButton(finishTurnB, new Label());

        ArrayList<Move> buttonsToShow = currentPlayer.getPossibleMoves();
        fillMoveCount(buttonsToShow);
        fillLabels();

        for (Move bts : buttonsToShow){
            switch (bts.getType()){
                case REDIRECT:
                    showButton(redirectB, redirectL);
                    comboBox.setLayoutY(redirectB.getLayoutY());
                    comboBox.setVisible(true);
                    break;
                case GUARD:
                    showButton(guardB, guardL);
                    break;
                case BURN:
                    showButton(burnB, burnL);
                    break;
                case SPEED:
                    showButton(speedB, speedL);
                    break;
                case PORTAL:
                    showButton(portalB, portalL);
                    break;
                case ROTATE:
                    showButton(rotateB, rotateL);
                    break;
                case RANGE:
                    showButton(rangeB, rangeL);
                    break;
                default:
                    System.out.println("button type: " + bts.getType());
            }
        }
    }

    private void redirect(){
        Player currentPlayer = (turn == 0) ? p0 : p1;
        System.out.println(moveCounts.values());
        if (moveCounts.get(Move.type.REDIRECT) > 0) {
            for (Pawn p : currentPlayer.pawns) {
                if (p.c == selected) {
                    for (int i = 0; i < rotationVal; i++){
                        p.rotate();
                    }
                }
            }
            moveCounts.put(Move.type.REDIRECT, moveCounts.get(Move.type.REDIRECT)-1);
            if(moveCounts.get(Move.type.REDIRECT) == 0){
                redirectB.setDisable(true);
            }
        }
        fillLabels();
        System.out.println("On Redirect!!");
    }

    private void guard() {
        System.out.println("On Guard!!");
        if (moveCounts.get(Move.type.GUARD) > 0) {
            int p = Cell.PAWN;
            if( turn == 1 ) p = Cell.PAWN2;
            if( debug || selected.contains == p ) {

                for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() )
                    if( c.getValue().contains == Cell.EMPTY )
                        c.getValue(  ).setIsPossible( 1 );

                spell = Cell.BLOCK;
            }
            moveCounts.put(Move.type.GUARD, moveCounts.get(Move.type.GUARD)-1);
            if(moveCounts.get(Move.type.GUARD) == 0){
                guardB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void burn() {
        System.out.println("On Burn!!");
        if (moveCounts.get(Move.type.BURN) > 0) {
            int p = Cell.PAWN;
            if( turn == 1 ) p = Cell.PAWN2;
            if( debug || selected.contains == p ) {
                for(Map.Entry<Integer, Cell> c: selected.adj.entrySet() )
                    if( c.getValue().contains == Cell.EMPTY )
                        c.getValue(  ).setIsPossible( 1 );

                spell = Cell.LAVA;
            }
            moveCounts.put(Move.type.BURN, moveCounts.get(Move.type.BURN)-1);
            if(moveCounts.get(Move.type.BURN) == 0){
                burnB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void speed() {
        System.out.println("On Speed!!");
        if (moveCounts.get(Move.type.SPEED) > 0) {
            int p = Cell.PAWN;
            if( turn == 1 ) p = Cell.PAWN2;

            if (debug || selected.contains == p) {

            }
            moveCounts.put(Move.type.SPEED, moveCounts.get(Move.type.SPEED)-1);
            if(moveCounts.get(Move.type.SPEED) == 0){
                speedB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void portal() {
        System.out.println("On Portal!!");
        if (moveCounts.get(Move.type.PORTAL) > 0) {
            Cell selected = cells[selectedX][selectedY];
            int p = Cell.PAWN;
            if( turn == 1 ) p = Cell.PAWN2;

            if (debug || selected.contains == p) {

            }
            moveCounts.put(Move.type.PORTAL, moveCounts.get(Move.type.PORTAL)-1);
            if(moveCounts.get(Move.type.PORTAL) == 0){
                portalB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void rotate() {
        System.out.println("On Rotate!!");
        if (moveCounts.get(Move.type.PORTAL) > 0) {
            int p = Cell.PAWN;
            if( turn == 1 ) p = Cell.PAWN2;

            if (debug || selected.contains == p) {
                if( selected.adj.size() == 6 ) {
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
            moveCounts.put(Move.type.ROTATE, moveCounts.get(Move.type.ROTATE)-1);
            if(moveCounts.get(Move.type.ROTATE) == 0){
                rotateB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void range() {
        System.out.println("On Range!!");
        if(moveCounts.get(Move.type.RANGE) > 0){
            moveCounts.put(Move.type.RANGE, moveCounts.get(Move.type.RANGE)-1);
            if(moveCounts.get(Move.type.RANGE) == 0){
                rangeB.setDisable(true);
            }
        }
        fillLabels();
    }

    private void proceed() {
        System.out.println("On Proceed!!\n");

        Player pl = p0, en = p1;
        if( turn == 1 ) {pl = p1;en = p0;}

        int ownPawn = 1;
        if( turn == 1 ) ownPawn = 2;

        for(int i = 0; i< pl.pawns.size(); i++) {
            Pawn p = pl.pawns.get(i);

            int d = p.direction;
            Cell nex = p.c.adj.get( d );
            if( nex != null && nex.contains != Cell.BLOCK && nex.contains != ownPawn ) {
                if( nex.contains == (turn^1)+1 ) {
                    en.erase( nex );
                }
                else if( nex.contains == Cell.LAVA ) {
                    p.relocate( nex );
                    pl.erase( nex );
                    i--;
                }
                else if( nex.contains == Cell.BASE){
                    pl.erase(p.c);
                    en.base.health--;
                }
                if( p != null ) p.relocate( nex );
            }

        }
        endTurn();
        switchTurn();
    }

    private void endTurn(){
        if(p0.base.health == 0 || p1.base.health == 0){
            String winner = (turn == 0) ? "Red" : "Blue";
            Alert endGame = new Alert(Alert.AlertType.INFORMATION);
            endGame.setContentText(winner + " Won!");
            endGame.setTitle("Game Over");
            endGame.showAndWait();
        }
    }

    private void switchTurn() {
        btny = 40;
        turn ^= 1;
        addButtons();
    }

    private void handleStage0(){

         placePawn = createButon("Place Pawn", 40, event -> {
            if (turn == 0 && p0.pawnsToPlace != 0) {
                if( selected.isPossible == 1 )
                    try {
                        Pawn p = new Pawn(selected, 0);
                        p.draw(pen);
                        p0.pawnsToPlace--;
                        p.rotate();p.rotate();p.rotate();p.rotate();
                        turn = 1;
                        p0.addPawn( p );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            } else if (turn == 1 && p1.pawnsToPlace != 0) {
                if( selected.isPossible == 1 )
                    try {
                        Pawn p = new Pawn(selected, 1);
                        p.draw(pen);
                        p1.pawnsToPlace--;

                        if( p1.pawnsToPlace == 0 ) {
                            System.out.println("Stage0 completed");
                            clear();
                            handleStage1();
                        }
                        p.rotate();
                        turn = 0;
                        p1.addPawn( p );
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
            }
            if(p1.pawnsToPlace != 0) checkPlaces();
        });
        pen.getChildren().add(placePawn);
        checkPlaces();
    }

    public void clear() {

        for(int i=0;i<rows;i++)
            for(int j=0;j<columns;j++)
                cells[i][j].setIsPossible(0);
    }

    private void checkPlaces() {
        if( turn == 0 ) {
            clear();
            for(int i=0;i<rows;i++)
                for(int j=0;j<2;j++)
                    cells[i][j].setIsPossible(1);
        } else {
            clear();
            for(int i=columns-2;i<columns;i++)
                for(int j=0;j<rows;j++)
                    cells[j][i].setIsPossible(1);
        }
        cells[rows/2][0].setIsPossible(0);
        cells[rows/2][columns-1].setIsPossible(0);
    }

    private void handleStage1() {
        placePawn.setVisible( false );
        pen.getChildren().add(finishTurnB);
        addButtons();
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
                cells[i][j] = new Cell( x+90, y+50, i, j, pen, this );
            }
        }
    }

    private void drawBases(){
        p0.base.shape.setLayoutX(cells[rows/2][0].x);
        p0.base.shape.setLayoutY(cells[rows/2][0].y);
        p1.base.shape.setLayoutX(cells[rows/2][columns-1].x);
        p1.base.shape.setLayoutY(cells[rows/2][columns-1].y);
        pen.getChildren().addAll(p0.base.shape, p1.base.shape);

        p0.base.cell = cells[rows/2][0];
        p1.base.cell = cells[rows/2][columns-1];

        cells[rows/2][0].setContains(Cell.BASE);
        cells[rows/2][columns-1].setContains(Cell.BASE);
    }

    public void draw() {
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                cells[i][j].draw();
            }
        }
    }

}
