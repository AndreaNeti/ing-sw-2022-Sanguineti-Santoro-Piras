package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.awt.event.ActionEvent;

public class UndoCommands extends GameCommand {
    public UndoCommands(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws ScannerException {
        getView().addMessage("Reset all input");
        getView().getCurrentCharacterCard().resetInput();
        getView().unsetCurrentCharacterCard();
        getView().goToOldPhase();
    }

    @Override
    public String toString() {
        return "Undo";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
