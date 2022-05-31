package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Cli.ViewCli;

import java.awt.event.ActionEvent;

public class UndoCommands extends GameCommand {

    @Override
    public void playCLICommand(ViewCli viewCli) {
        if (viewCli.getCurrentCharacterCard() != null) {
            viewCli.addMessage("Reset all input");
            viewCli.getCurrentCharacterCard().resetInput();
            viewCli.unsetCurrentCharacterCard();
        }
        viewCli.goToOldPhase();
    }

    @Override
    public String toString() {
        return "Undo";
    }

}
