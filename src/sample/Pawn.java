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

public class Pawn {

    public Cell c;
    public int direction = 0;
    public int team;
    public ImageView pawnImage;
    private Image bluerobot = new Image(new FileInputStream("src\\assets\\bluerobot.png"));
    private Image redrobot = new Image(new FileInputStream("src\\assets\\redrobot.png"));
    private double height;
    private double width;

    public Pawn( Cell c, int team ) throws FileNotFoundException {
        this.c = c;
        direction = 0;
        this.team = team;

    }

    public void draw(Group pen) throws FileNotFoundException {
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
        });
        pen.getChildren().addAll(pawnImage);
    }

    public void rotate(){
        direction++;
        pawnImage.setRotate(direction*60);
    }
}
