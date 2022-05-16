package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.Controller.GamePhaseController;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.GamePhaseView;

import java.util.List;

public abstract class GamePhase implements GamePhaseView, GamePhaseController {
    private List<GameCommand> gameCommands;
    public GamePhase(List<GameCommand> gameCommands){
        this.gameCommands=gameCommands;
    }
    @Override
    public void playPhase(ViewCli viewCli) {
        System.out.println("You are in "+this);
        int i=0;
        for (GameCommand gC : getGameCommands()) {
            viewCli.print("["+i+"]:"+gC);
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
