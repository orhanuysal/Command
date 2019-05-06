package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pawn {

    public Cell c;
    public int direction = 0;
    public int team;
    public ImageView pawnImage;

    public Pawn( Cell c, int team ) {
        this.c = c;
        direction = 0;
        this.team = team;
    }

    public void draw(GridPane root) throws FileNotFoundException {
        double x = c.getX();
        double y = c.getY();
        if(team == 1){
            pawnImage = new ImageView(new Image(new FileInputStream("C:\\Users\\alper\\IdeaProjects\\Command\\assets\\bluerobot.png")));
        }
        else{
            pawnImage = new ImageView(new Image(new FileInputStream("C:\\Users\\alper\\IdeaProjects\\Command\\assets\\redrobot.png")));
        }

        root.getChildren().addAll(pawnImage);
    }
}
