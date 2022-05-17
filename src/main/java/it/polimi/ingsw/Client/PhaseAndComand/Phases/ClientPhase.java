package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.ClientPhaseView;

import java.util.List;

public abstract class ClientPhase implements ClientPhaseView, ClientPhaseController {

    private final List<GameCommand> gameCommands;

    public ClientPhase(List<GameCommand> gameCommands) {
        this.gameCommands = gameCommands;
    }

    @Override
    public void playPhase(ViewCli viewCli) {
        viewCli.resetPhase();
        System.out.println("You are in " + this);

        getGameCommands().get(viewCli.getIntInput(gameCommands.toArray(), "Select the command to play")).playCLICommand();
    }

    @Override
    public List<GameCommand> getGameCommands() {
        return gameCommands;
    }

    @Override
    public void setPhaseInView(List<AbstractView> views) {
        for (AbstractView abstractView : views) {
            abstractView.setPhaseInView(this);
        }
    }

}
