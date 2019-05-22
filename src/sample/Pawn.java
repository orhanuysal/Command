package sample;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Pawn {

    public Cell c;
    private ArrayList<Move> moves;
    public int direction;
    public int team;
    public ImageView pawnImage;
    public int speed = 0;
    private Image bluerobot = new Image(new FileInputStream("src//assets//bluerobot.png"));
    private Image redrobot = new Image(new FileInputStream("src//assets//redrobot.png"));
    private double height;
    private double width;

    public Pawn( Cell c, int team ) throws FileNotFoundException {
        speed = 0;
        this.c = c;
        direction = 2;
        this.team = team;
        c.setContains( team+1 );
        moves = new ArrayList<>();
    }

    public void addMove(Move.type type){
        moves.add(new Move(type));
    }

    public void executeMoves(){
        for (Move m: moves){
            m.executeMove();
            System.out.println("Move " + m.getType());
        }
        moves.clear();
    }

    public void relocate( Cell ccc ) {
        if (this.c.contains == Cell.PAWN  || c.contains == Cell.PAWN2) {
            this.c.setContains(0);
        }
        this.c = ccc;
        c.setContains( team+1 );

        double x = c.getX() - width/2;
        double y = c.getY() - height/2;
        pawnImage.setLayoutX(x);
        pawnImage.setLayoutY(y);
    }

    public void draw(Group pen) throws FileNotFoundException {
        System.out.println( "In Draw!!\n" );
        width = bluerobot.getWidth();
        height = bluerobot.getHeight();
        double x = c.getX() - width/2;
        double y = c.getY() - height/2;
        if(team == 1){
            pawnImage = new ImageView(bluerobot);
        }
        else{
            pawnImage = new ImageView(redrobot);
        }

        System.out.println("height: " + height + " width: " + width);
        System.out.println(x + " "+ y);

        pawnImage.setLayoutX(x);
        pawnImage.setLayoutY(y);
        pawnImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton() == MouseButton.SECONDARY){
                rotate();
            }
            else if( event.getButton() == MouseButton.PRIMARY ) {
                c.clicked();
            }
        });
        pen.getChildren().addAll(pawnImage);
    }

    public void rotate(){
        direction++;
        direction %= 6;
        pawnImage.setRotate(direction*60-90);
    }
}
