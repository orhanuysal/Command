package sample;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public abstract class Page {
    protected GridPane root;
    public final int WIDTH = 50;
    public final int HEIGHT = 30;
    public final int SIZE = 15;

    protected Button createButon(String text, double y, EventHandler handler) {
        Button btn = new Button(text);
        btn.setOnMouseClicked(handler);
        btn.setLayoutY( y );
        btn.setMinSize(WIDTH, HEIGHT);
        btn.setFont(new Font(SIZE));
        btn.getStyleClass().add("record-sales");
        return btn;
    }
}
