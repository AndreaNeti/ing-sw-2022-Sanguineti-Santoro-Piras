package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiFX extends Application {
    private static Stage primaryStage;
    private static Scene activeScene;
    private static SceneController sceneController;

    @Override
    public void start(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Eryantis");
        FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/menu.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 700, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
        sceneController = loader.getController();
        ControllerClient controllerClient = new ControllerClient();
        ViewGUI viewGUI = new ViewGUI(controllerClient);
        controllerClient.attachView(viewGUI);
        try {
            viewGUI.start();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static void goToBoardScene() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Eryantis");
        FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
        sceneController = loader.getController();
    }

    public static void goToMenuScene() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Eryantis");
        FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/menu.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(loader.load(), 700, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
        sceneController = loader.getController();
    }

    private static void setPrimaryStage(Stage primaryStage1) {
        if (primaryStage == null) primaryStage = primaryStage1;
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static Scene getActiveScene() {
        return activeScene;
    }

    public static void main(String[] args) {
        launch();
    }
}
