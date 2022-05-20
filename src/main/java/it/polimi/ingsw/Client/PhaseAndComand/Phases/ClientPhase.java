package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientPhase implements ClientPhaseView, ClientPhaseController {

    private final List<GameCommand> gameCommands;
    private static Thread t;
    public ClientPhase() {
        this.gameCommands = new ArrayList<>();
    }

    @Override
    public void playPhase(ViewCli viewCli) {
        viewCli.clearConsole();
        System.out.println("You are in " + this);
        int index;
        try {
            viewCli.unsetPhase();
            index = viewCli.getIntInput(gameCommands.toArray(), "Select the command to play");
            gameCommands.get(index).playCLICommand();
        } catch (PhaseChangedException ignored) {
            System.out.println("Phase has changed");
        }
    }

    @Override
    public List<GameCommand> getGameCommands() {
        return gameCommands;
    }

    @Override
    public void setPhaseInView(AbstractView view, boolean notifyScanner) {
        view.setPhaseInView(this, notifyScanner);

    }
    public void addCommand(GameCommand command){
        gameCommands.add(command);
    }
}
