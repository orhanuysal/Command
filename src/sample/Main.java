package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Command!!");
        System.out.println("Start!!!\n");
        Game game = new Game( root );

        primaryStage.setScene(new Scene(root, game.sceneWidth, game.sceneHeight));
        //primaryStage.setFullScreen(true);


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
