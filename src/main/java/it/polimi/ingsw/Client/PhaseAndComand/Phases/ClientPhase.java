package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.ClientPhaseView;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientPhase implements ClientPhaseView, ClientPhaseController {

    private final List<GameCommand> gameCommands;

    public ClientPhase() {
        this.gameCommands = new ArrayList<>();
    }

    @Override
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
            gameCommands.get(index).playCLICommand();
        } catch (ScannerException ignored) {
        }
    }

    @Override
    public List<GameCommand> getGameCommands() {
        return gameCommands;
    }

    @Override
    public void setPhaseInView(AbstractView view, boolean forceScannerSkip) {
        view.setPhaseInView(this, forceScannerSkip);

    }

    public void addCommand(GameCommand command) {
        gameCommands.add(command);
    }
}
