package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

public class App {
    public static void main(String[] args) {
        ControllerClient controllerClient=new ControllerClient();
        AbstractView viewCli=new ViewCli(controllerClient);
        controllerClient.addListener(viewCli);
        viewCli.start();
    }
}
