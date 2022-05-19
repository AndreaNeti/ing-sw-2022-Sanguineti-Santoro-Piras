package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.ClientPhaseView;

import java.util.Arrays;
import java.util.List;

public abstract class ClientPhase implements ClientPhaseView, ClientPhaseController {

    private final List<GameCommand> gameCommands;

    public ClientPhase(List<GameCommand> gameCommands) {
        this.gameCommands = gameCommands;
    }

    @Override
    public void playPhase(ViewCli viewCli) {
        viewCli.clearConsole();
        System.out.println("You are in " + this);
        int index = viewCli.playOptions(gameCommands.toArray(), "Select the command to play");
        if(index != -1)
            gameCommands.get(index).playCLICommand();
        viewCli.unsetPhase();
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
