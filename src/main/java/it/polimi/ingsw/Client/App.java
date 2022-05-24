package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.CliPrinter;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.GameClientListener;

public class App {
    static AbstractView view;

    public static void main(String[] args) {

        ControllerClient controllerClient = new ControllerClient();

        view = new ViewCli(controllerClient);

        controllerClient.attachView(view);
        CliPrinter cliPrinter =new CliPrinter(view);
        controllerClient.addListener(cliPrinter);
        controllerClient.instantiateAllPhases();
        try {
            view.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
