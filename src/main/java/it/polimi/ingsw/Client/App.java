package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Server.controller.Controller;

public class App {
    public static void main(String[] args) {
        ControllerClient controllerClient=new ControllerClient();
        ViewCli viewCli=new ViewCli(controllerClient);
        controllerClient.addListener(viewCli);
        viewCli.run();
    }
}
