package it.tommaso.planckbet.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/mainwindow.fxml")));
        primaryStage.setTitle("PlanckBet");
        primaryStage.setScene(new Scene(root, 1650, 820));
        primaryStage.show();
         primaryStage.getIcons().add(new Image("file:src/it/tommaso/planckbet/model/image/icona5.png"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
