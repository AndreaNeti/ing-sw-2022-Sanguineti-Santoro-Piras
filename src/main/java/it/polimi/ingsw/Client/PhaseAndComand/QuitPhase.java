package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class QuitPhase extends ClientPhase {

    public QuitPhase(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "quit phase";
    }
}
