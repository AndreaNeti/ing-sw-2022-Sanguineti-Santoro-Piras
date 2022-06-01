package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientPhase {

    private final List<GameCommand> gameCommands;

    public ClientPhase() {
        this.gameCommands = new ArrayList<>();
    }

    public void playPhase(ViewCli viewCli) {
        try {
            viewCli.unsetPhase();
            System.out.println("You are in " + this);
            int index = viewCli.getIntInput(gameCommands.toArray(), "Select the command to play", true);

            gameCommands.get(index).playCLICommand(viewCli);
        } catch (SkipCommandException ignored) {
        }
    }

    public abstract void playPhase(ViewGUI viewGUI, SceneController sceneController);

    public void addCommand(GameCommand command) {
        gameCommands.add(command);
    }
}
