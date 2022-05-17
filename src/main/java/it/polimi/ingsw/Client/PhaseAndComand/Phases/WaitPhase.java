package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class WaitPhase extends ClientPhase {
    public WaitPhase(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }


    @Override
    public String toString() {
        return " wait phase";
    }
}
