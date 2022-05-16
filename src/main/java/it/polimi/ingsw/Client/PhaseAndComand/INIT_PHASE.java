package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class INIT_PHASE extends GamePhase {
    public INIT_PHASE(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    @Override
    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "INIT_PHASE";
    }
}
