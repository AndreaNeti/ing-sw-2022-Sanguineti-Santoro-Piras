package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class DeleteLastInputCommand extends GameCommand {
    public DeleteLastInputCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand()  {
        getView().repeatPhase(false);
    }

    @Override
    public String toString() {
        return "Delete last input";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
