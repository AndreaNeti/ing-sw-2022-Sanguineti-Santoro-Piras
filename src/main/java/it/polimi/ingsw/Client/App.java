package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.*;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.UpdateCli;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Enum.CLICommands;

import java.util.Map;

import static java.util.Map.entry;

public class App {
    static AbstractView view;

    public static void main(String[] args) {

        ControllerClient controllerClient = new ControllerClient();

        view = new ViewCli(controllerClient);

        controllerClient.attachView(view);
        GameClientListener updateCLI=new UpdateCli((ViewCli) view);
        controllerClient.addListener(updateCLI);

        controllerClient.instantiateAllPhases();
        try {
            view.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
