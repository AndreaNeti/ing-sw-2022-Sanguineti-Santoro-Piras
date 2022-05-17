package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.*;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Enum.CLICommands;

import java.util.Map;

import static java.util.Map.entry;

public class App {
    static AbstractView view;

    public static void main(String[] args) {
        ControllerClient controllerClient = new ControllerClient();

        if (args[1].equals("gui"))
            view = new ViewGUI(controllerClient);
        else {
            view = new ViewCli(controllerClient);
        }

        controllerClient.addListener(view);
        controllerClient.setCommands(instantiateCommands(view));
        controllerClient.instantiateAllPhases();
        view.start();
    }

    private static Map<CLICommands, GameCommand> instantiateCommands(AbstractView view) {
        return Map.ofEntries(
                entry(CLICommands.CONNECT_SERVER, new ConnectServerCommand(view)),
                entry(CLICommands.SET_NICKNAME, new SetNicknameCommand(view)),
                entry(CLICommands.CREATE_MATCH, new CreateMatchCommand(view)),
                entry(CLICommands.JOIN_MATCH_BY_TYPE, new JoinMatchByTypeCommand(view)),
                entry(CLICommands.JOIN_MATCH_BY_ID, new JoinMatchByIdCommand(view)),
                entry(CLICommands.PLAY_CARD, new PlayCardCommand(view)),
                entry(CLICommands.MOVE_STUDENT, new MoveStudentCommand(view)),
                entry(CLICommands.MOVE_MOTHER_NATURE, new MoveMotherNatureCommand(view)),
                entry(CLICommands.MOVE_FROM_CLOUD, new MoveFromCloudCommand(view)),
                entry(CLICommands.TEXT_MESSAGE, new TextCommand(view)),
                entry(CLICommands.QUIT, new QuitCommand(view))
        );

    }
}
