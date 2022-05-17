package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.ConnectServerCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.QuitCommand;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.SetNicknameCommand;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Enum.CLICommands;
import it.polimi.ingsw.Enum.GamePhase;

import java.util.*;

import static java.util.Map.entry;

public class App {
    static AbstractView view;

    public static void main(String[] args) {
        ControllerClient controllerClient=new ControllerClient();

        if(args[1].equals("gui"))
            view=new ViewGUI(controllerClient);
        else {
            view=new ViewCli(controllerClient);
        }
        controllerClient.setCommands(instantiateCommands(view));
        controllerClient.addListener(view);
        view.start();
    }
    private static Map<CLICommands,GameCommand> instantiateCommands(AbstractView view){
        return Map.ofEntries(
                entry(CLICommands.CONNECT_SERVER, new ConnectServerCommand(view)),
                entry(CLICommands.SET_NICKNAME, new SetNicknameCommand(view)),
                entry(CLICommands.QUIT, new QuitCommand(view))

         );

    }
}
