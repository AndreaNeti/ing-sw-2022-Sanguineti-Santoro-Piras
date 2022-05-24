package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class UndoCommands extends GameCommand {
    public UndoCommands(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        System.out.println("Reset all input");
        getView().getCurrentCharacterCard().resetInput();
        getView().unsetCurrentCharacterCard();
        getView().goToOldPhase(true);
    }

    @Override
    public String toString() {
        return "Undo";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
