package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientPhase {

    private final List<GameCommand> gameCommands;

    public ClientPhase() {
        this.gameCommands = new ArrayList<>();
    }

    public void playPhase(ViewCli viewCli) {
        Integer index = null;
        try {
            viewCli.unsetPhase();
            boolean phaseChanged;
            do {
                phaseChanged = false;
                try {
                    System.out.println("You are in " + this);
                    index = viewCli.getIntInput(gameCommands.toArray(), "Select the command to play", true);
                } catch (RepeatCommandException e) {
                    phaseChanged = true;
                }
            } while (phaseChanged);
            gameCommands.get(index).playCLICommand(viewCli);
        } catch (ScannerException ignored) {
            // skipped input
        }
    }

    public abstract void playPhase(ViewGUI viewGUI, SceneController sceneController);

    public void addCommand(GameCommand command) {
        gameCommands.add(command);
    }
}
