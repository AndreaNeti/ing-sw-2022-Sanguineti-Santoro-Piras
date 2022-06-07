package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Util.GamePhase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiFX extends Application {
    private static Stage primaryStage;
    private static Scene boardScene;
    private static Scene menuScene;
    private static SceneController sceneController;
    private static ViewGUI viewGUI;
    private static ControllerClient controller;

    @Override
    public void start(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        goToMenuScene();
        primaryStage.show();
        viewGUI.setPhaseInView(GamePhase.INIT_PHASE, true, false);
    }

    public static void goToBoardScene() {
        if (boardScene == null) {
            primaryStage.setFullScreen(true);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Eryantis");
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));
            try {
                boardScene = new Scene(loader.load(), 1920, 1080);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sceneController = loader.getController();
            sceneController.setViewGUI(viewGUI);
            controller.addListener(sceneController);
        }
        primaryStage.setScene(boardScene);
        primaryStage.show();
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
            sceneController = loader.getController();
            sceneController.setViewGUI(viewGUI);
            controller.addListener(sceneController);
        }
        primaryStage.setScene(menuScene);
        primaryStage.show();

    }

    private static void setPrimaryStage(Stage primaryStage1) {
        if (primaryStage == null) primaryStage = primaryStage1;
    }

    public static SceneController getSceneController() {
        return sceneController;
    }

    public static void main(String[] args) {
        controller = new ControllerClient();
        viewGUI = new ViewGUI(controller);
        controller.attachView(viewGUI);
        launch();
    }
}
