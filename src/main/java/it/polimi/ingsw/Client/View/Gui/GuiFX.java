package it.polimi.ingsw.Client.View.Gui;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Util.GamePhase;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.awt.color.*;

import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;

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
            /*Thread start = new Thread(() -> */
            System.exit(0);
            // start.start();
        });
        goToMenuScene();
        primaryStage.show();
        viewGUI.setPhaseInView(GamePhase.INIT_PHASE, true, false);

    }

    public static void goToBoardScene() {
        if (inMenuScene) {
            primaryStage.setTitle("Eryantis");
            JFrame loadingWindow = new LoadingWindow().getInitWindow();
            //pb.setProgress(0);
            FXMLLoader loader = new FXMLLoader(GuiFX.class.getResource("/board.fxml"));

            try {
                int width = (int) Screen.getPrimary().getBounds().getWidth();
                int height = (int) Screen.getPrimary().getBounds().getHeight();
                primaryStage.setScene(new Scene(loader.load(), width, height, false, SceneAntialiasing.BALANCED));
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
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setResizable(false);
            primaryStage.show();
            loadingWindow.dispose();
            loadingWindow.dispose();
        }
    }

    private static class LoadingWindow {
        private JFrame getInitWindow() {

            JFrame loadingFrame = new JFrame();
            ImageIcon icon = new ImageIcon("C:\\Users\\Emanu\\Desktop\\ing-sw-2022-Sanguineti-Santoro-Piras\\src\\main\\resources\\Graphical_Assets\\loading.gif");
            //icon = new ImageIcon(icon.getImage().getScaledInstance(700, 700, Image.SCALE_DEFAULT));
            JLabel label = new JLabel(icon);
            //loadingFrame.setIconImage(new ImageIcon(getClass().getResource("/images/logo.png").getPath()).getImage());
            loadingFrame.setUndecorated(true);
            loadingFrame.setBackground(new Color(0f, 0f, 0f, 0f));

            /*Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            loadingFrame.setLocation((int) ((screenBounds.getWidth() -800) / 2), (int) ((screenBounds.getHeight() - 500) / 2));*/
            loadingFrame.getContentPane().add(label);
            loadingFrame.pack();
            loadingFrame.setLocationRelativeTo(null);
            loadingFrame.setAlwaysOnTop(true);
            loadingFrame.setVisible(true);
            return loadingFrame;
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
