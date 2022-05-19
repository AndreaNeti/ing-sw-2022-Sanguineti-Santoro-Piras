package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.AbstractView;

import java.util.List;

public interface ClientPhaseController {
    void setPhaseInView(List<AbstractView> views, boolean notifyScanner);
    List<GameCommand> getGameCommands();
}
