package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class PlanificationPhase extends ClientPhase {
    public PlanificationPhase(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "Planification Phase";
    }
}
