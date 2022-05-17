package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class InitPhase extends ClientPhase {
    public InitPhase(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "Init Phase";
    }
}
