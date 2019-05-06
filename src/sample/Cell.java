package sample;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

public class Cell extends Parent {
    public double x, y;
    public Group root;

    public Cell(double x, double y, Group root) {
        this.root = root;
        this.x = x;
        this.y = y;
    }

    public class X extends Parent {
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }
    }

    public void draw(  ) {
        Polyline hexagon = new Polyline();

        double theta = 0;
        for(int i=0;i<=6;i++) {
            hexagon.getPoints().add( x + Game.LENGTH*Math.cos( theta ) );
            hexagon.getPoints().add( y + Game.LENGTH*Math.sin( theta ) );
            theta += Math.PI / 3;
        }

        hexagon.setFill(Color.AQUA);
        hexagon.setStroke(Color.BLACK);

        root.getChildren().addAll( hexagon );
    }
}
