package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ControllerClient;
import it.polimi.ingsw.client.view.Cli.ViewCli;

/**
 * AppCLI is the game's CLI application.
 */
public class AppCLI {
    static ViewCli view;

    public static void main(String[] args) {
        ControllerClient controllerClient = new ControllerClient();
        view = new ViewCli(controllerClient);
        controllerClient.attachView(view);
        try {
            view.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
