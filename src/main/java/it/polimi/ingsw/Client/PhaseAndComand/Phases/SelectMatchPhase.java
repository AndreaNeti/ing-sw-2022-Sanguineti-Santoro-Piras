package it.polimi.ingsw.Client.PhaseAndComand.Phases;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class SelectMatchPhase extends ClientPhase {
    public SelectMatchPhase() {
        super();
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }
    public String toString(){
        return "Select match phase";
    }
}
