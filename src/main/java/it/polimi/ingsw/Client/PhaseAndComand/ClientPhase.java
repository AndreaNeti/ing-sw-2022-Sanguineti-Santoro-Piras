package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
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
        System.out.println("You are in " + this);
        int i = 0;
        for (GameCommand gC : getGameCommands()) {
            viewCli.print("[" + i + "]:" + gC);
            i++;
        }
        viewCli.print("What do you want to do?");
        //getGameCommands().get(viewCli.getIntInput()).playCLICommand();
    }

    public List<GameCommand> getGameCommands() {
        return gameCommands;
    }

    @Override
    public void setView(List<AbstractView> views) {
        for (AbstractView abstractView : views) {
            abstractView.setView(this);
        }
    }

}
