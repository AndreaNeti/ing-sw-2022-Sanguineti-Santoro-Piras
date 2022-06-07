package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

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
