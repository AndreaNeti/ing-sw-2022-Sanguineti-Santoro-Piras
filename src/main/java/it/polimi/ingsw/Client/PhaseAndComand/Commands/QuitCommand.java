package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class QuitCommand extends GameCommand{

    public QuitCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {
        //TODO implement
    }

    @Override
    public String toString() {
        return "Quit";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
