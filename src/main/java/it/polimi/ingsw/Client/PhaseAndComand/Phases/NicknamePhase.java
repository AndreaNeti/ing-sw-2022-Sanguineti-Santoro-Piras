package it.polimi.ingsw.Client.PhaseAndComand.Phases;
import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import java.util.List;

public class NicknamePhase extends ClientPhase {

    public NicknamePhase(List<GameCommand> gameCommands) {
        super(gameCommands);
    }

    public void playPhase(ViewGUI viewGUI) {

    }

    @Override
    public String toString() {
        return "Nickname Phase";
    }
}