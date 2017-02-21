package pl.lukaszgo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(Settings.APP_NAME);
        primaryStage.setScene(new Scene(root, 266, 298));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
