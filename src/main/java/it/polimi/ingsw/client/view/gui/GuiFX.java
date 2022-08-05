package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.controller.ControllerClient;
import it.polimi.ingsw.client.view.gui.SceneController.SceneController;
import it.polimi.ingsw.utils.GamePhase;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class GuiFX extends Application {
    private static Stage primaryStage;
    //true if I'm in menu scene, false in board scene
    private static boolean inMenuScene;
    private static SceneController activeSceneController;
    private static ViewGUI viewGUI;
    private static ControllerClient controller;

    public static Window getPrimaryStage() {
        return primaryStage;
    }

    public static HostServices hostServices;

    @Override
    public void start(Stage primaryStage) {
        inMenuScene = false;
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(GuiFX.class.getResourceAsStream("/Graphical_Assets/icon.png"))));
        setPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        goToMenuScene();
        primaryStage.show();
        primaryStage.setTitle("Eriantys");
        viewGUI.setPhaseInView(GamePhase.INIT_PHASE, false);
        hostServices = getHostServices();
    }

    /**
     * Method goToBoardScene loads and set the menu scene
     */
    public static void goToMenuScene() {
        if (!inMenuScene) {
            if (activeSceneController != null)
                activeSceneController.stop();
            controller.removeListeners();
            primaryStage.setResizable(false);
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

    /**
     * Method goToBoardScene loads and set the main game scene
     */
    public static void goToBoardScene() {
        if (inMenuScene) {
            GuiFX.getActiveSceneController().getElementById("#loading").setVisible(true);
            controller.removeListeners();
            activeSceneController.stop();
            Task<Parent> task = new Task<>() {
                @Override
                protected Parent call() {
                    Parent p = null;
                    FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));
                    try {
                        p = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SceneController boardController = loader.getController();
                    controller.addListener(boardController);
                    activeSceneController = boardController;
                    try {
                        boardController.setViewGUI(viewGUI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return p;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(workerStateEvent -> {
                Parent finalParent = task.getValue();
                System.out.println("son qui");
                inMenuScene = false;
                primaryStage.setAlwaysOnTop(true);
                primaryStage.setFullScreen(true);
                primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                primaryStage.setResizable(false);
                primaryStage.setScene(new Scene(finalParent, 1920, 1080, false, SceneAntialiasing.BALANCED));
                viewGUI.play();
                primaryStage.show();
            });
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

    /**
     * ShowInputError is used in the GUI client when a command input is empty or invalid, showing an alert box.
     */
    public static void showError(String title, String contentText, String headerText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(headerText);
            alert.setTitle(title);
            alert.setContentText(contentText);
            alert.initOwner(GuiFX.getPrimaryStage());
            alert.showAndWait();
        });
    }

    /**
     * ShowInputError is used in the GUI client when a command input is empty or invalid, showing an alert box, you can pass also the graphic you want to put in alert box.
     */
    public static void showError(String title, String contentText, String headerText, Node node) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setResizable(true);
            alert.setHeaderText(headerText);
            alert.setTitle(title);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setHeight(500);
            Label label = new Label(contentText);
            VBox content = new VBox(10, label, node);
            content.setAlignment(Pos.CENTER);
            alert.getDialogPane().setContent(content);
            alert.getDialogPane().setMinHeight(350);
            alert.getDialogPane().setMinWidth(400);
            alert.setContentText(contentText);
            alert.initOwner(GuiFX.getPrimaryStage());
            alert.setGraphic(node);
            alert.showAndWait();
        });
    }

    public static boolean isInMenuScene() {
        return inMenuScene;
    }
}
