package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Util.GamePhase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class GuiFX extends Application {
    private static Stage primaryStage;
    //true if i'm in menu scene, false in board scene
    private static boolean inMenuScene;
    private static SceneController activeSceneController;
    private static ViewGUI viewGUI;
    private static ControllerClient controller;

    public static Window getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        inMenuScene = false;
        setPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            Thread start = new Thread(() -> System.exit(0));
            start.start();
        });
        goToMenuScene();
        primaryStage.show();
        viewGUI.setPhaseInView(GamePhase.INIT_PHASE, true, false);
    }

    public static void goToBoardScene() {
        if (inMenuScene) {
            primaryStage.setTitle("Eryantis");

            //pb.setVisible(true);
            //pb.setProgress(0);
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));

            try {
                primaryStage.setScene(new Scene(loader.load(), 1920, 1080, false, SceneAntialiasing.BALANCED));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //pb.setProgress(60);
            SceneController boardController = loader.getController();
            controller.addListener(boardController);
            activeSceneController = boardController;
            //pb.setProgress(80);
            boardController.setViewGUI(viewGUI);
            //pb.setProgress(100);
            inMenuScene = false;
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(true);
            primaryStage.show();
        }
    }

    public static void goToMenuScene() {
        if (!inMenuScene) {
            primaryStage.setResizable(false);
            primaryStage.setTitle("Eryantis");
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/menu.fxml"));
            try {
                primaryStage.setScene(new Scene(loader.load(), 700, 700, false, SceneAntialiasing.BALANCED));
                primaryStage.show();
                SceneController menuController = loader.getController();
                menuController.setViewGUI(viewGUI);
                controller.addListener(menuController);
                activeSceneController = menuController;
                inMenuScene = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setPrimaryStage(Stage primaryStage1) {
        if (primaryStage == null) primaryStage = primaryStage1;
    }

    public static SceneController getActiveSceneController() {
        return activeSceneController;
    }

    public static void main(String[] args) {
        controller = new ControllerClient();
        viewGUI = new ViewGUI(controller);
        controller.attachView(viewGUI);
        launch();
    }
}
