package it.polimi.ingsw.client.view.Gui;

import it.polimi.ingsw.client.controller.ControllerClient;
import it.polimi.ingsw.client.view.Gui.SceneController.SceneController;
import it.polimi.ingsw.utils.GamePhase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

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

    @Override
    public void start(Stage primaryStage) {
        inMenuScene = false;
        setPrimaryStage(primaryStage);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            /*Thread start = new Thread(() -> */
            System.exit(0);
            // start.start();
        });
        goToMenuScene();
        primaryStage.show();
        viewGUI.setPhaseInView(GamePhase.INIT_PHASE, false);

    }

    /**
     * Method goToBoardScene loads and set the main game scene
     */
    public static void goToBoardScene() {
        if (inMenuScene) {
            controller.removeListeners();
            primaryStage.setTitle("Eriantys");
            //JFrame loadingWindow = new LoadingWindow().getInitWindow();
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));

            try {
                /*int width = (int) Screen.getPrimary().getBounds().getWidth();
                int height = (int) Screen.getPrimary().getBounds().getHeight();*/
                primaryStage.setScene(new Scene(loader.load(), 1920, 1080, false, SceneAntialiasing.BALANCED));
            } catch (IOException e) {
                e.printStackTrace();
            }
            SceneController boardController = loader.getController();
            controller.addListener(boardController);
            activeSceneController = boardController;
            boardController.setViewGUI(viewGUI);
            inMenuScene = false;
            primaryStage.setFullScreen(true);
            //primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setResizable(false);
            //loadingWindow.dispose();
            primaryStage.show();
        }
    }

    /*private static class LoadingWindow {
        private JFrame getInitWindow() {

            JFrame loadingFrame = new JFrame();
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(GuiFX.class.getResource("/loading.gif")));
            //icon = new ImageIcon(icon.getImage().getScaledInstance(700, 700, Image.SCALE_DEFAULT));
            JLabel label = new JLabel(icon);
            //loadingFrame.setIconImage(new ImageIcon(getClass().getResource("/images/logo.png").getPath()).getImage());
            loadingFrame.setUndecorated(true);
            loadingFrame.setBackground(new Color(0f, 0f, 0f, 0f));

            /*Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            loadingFrame.setLocation((int) ((screenBounds.getWidth() -800) / 2), (int) ((screenBounds.getHeight() - 500) / 2));
            loadingFrame.getContentPane().add(label);
            loadingFrame.pack();
            loadingFrame.setLocationRelativeTo(null);
            loadingFrame.setAlwaysOnTop(true);
            loadingFrame.setVisible(true);
            return loadingFrame;
        }

    }*/

    /**
     * Method goToBoardScene loads and set the menu scene
     */
    public static void goToMenuScene() {
        if (!inMenuScene) {
            controller.removeListeners();
            primaryStage.setResizable(false);
            primaryStage.setTitle("Eriantys");
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
            alert.setHeaderText(headerText);
            alert.setTitle(title);
            alert.setContentText(contentText);
            alert.initOwner(GuiFX.getPrimaryStage());
            alert.setGraphic(node);
            alert.showAndWait();
        });
    }
}
