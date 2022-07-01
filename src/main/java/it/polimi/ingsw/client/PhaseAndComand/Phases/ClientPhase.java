package it.polimi.ingsw.client.PhaseAndComand.Phases;

import it.polimi.ingsw.client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.View.Cli.ViewCli;
import it.polimi.ingsw.client.View.Gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * ClientPhase abstract class represents the phase in which a client can be. <br>
 * Each phase class extend this class and has its own commands available to be executed by the client.
 */
public abstract class ClientPhase {

    private final List<GameCommand> gameCommands;

    /**
     * Constructor ClientPhase creates a new instance of ClientPhase.
     */
    public ClientPhase() {
        this.gameCommands = new ArrayList<>();
    }

    /**
     * Method playPhase refreshes the phase in the client's view (CLI), lists all the available commands and waits for the
     * user to select one and then executes it.
     *
     * @param viewCli of type {@link ViewCli} - instance of the client's view (CLI).
     */
    public void playPhase(ViewCli viewCli) {
        try {
            viewCli.unsetPhase();
            System.out.println("You are in " + this);
            int index = viewCli.getIntInput(gameCommands.toArray(), "Select the command to play", true);

            gameCommands.get(index).playCLICommand(viewCli);
        } catch (SkipCommandException ignored) {
        }
    }

    /**
     * Method playPhase gets the scene controller from the client's view (GUI) and modifies the scene based on the phase.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     */
    public abstract void playPhase(ViewGUI viewGUI);

    /**
     * Method addCommand adds the provided command to the list of available ones in the game phase.
     *
     * @param command of type {@link GameCommand} - command to add to the phase.
     */
    public void addCommand(GameCommand command) {
        if (!gameCommands.contains(command))
            gameCommands.add(command);
    }

    /**
     * Method getGameCommands returns all the game commands available in the game phase.
     *
     * @return {@code List}<{@link GameCommand}> - list of the available game commands.
     */
    List<GameCommand> getGameCommands() {
        return gameCommands;
    }
}
