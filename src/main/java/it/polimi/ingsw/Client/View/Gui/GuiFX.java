package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Util.GamePhase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GuiFX extends Application {
    private static Stage primaryStage;
    private static SceneController boardController;
    private static SceneController menuController;
    private static Scene menuScene;
    private static Scene boardScene;
    private static SceneController activeSceneController;
    private static ViewGUI viewGUI;
    private static ControllerClient controller;

    @Override
    public void start(Stage primaryStage) {
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
        if (boardScene == null) {
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));

            try {
                boardScene = new Scene(loader.load(), 1920, 1080);
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setScene(boardScene);
            primaryStage.setFullScreen(false);
            primaryStage.setResizable(true);
            primaryStage.show();
            boardController = loader.getController();
            controller.addListener(boardController);
            boardController.setViewGUI(viewGUI);

        } else {
            activeSceneController = boardController;
            primaryStage.setScene(boardScene);
            primaryStage.show();
        }
    }

    public static void goToMenuScene() {
        if (menuScene == null) {
            primaryStage.setResizable(false);
            primaryStage.setTitle("Eryantis");
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/menu.fxml"));
            try {
                menuScene = new Scene(loader.load(), 700, 700);
            } catch (IOException e) {
                e.printStackTrace();
            }

            menuController = loader.getController();
            menuController.setViewGUI(viewGUI);
            controller.addListener(menuController);
        } else {
            activeSceneController = menuController;
            primaryStage.setScene(menuScene);
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
