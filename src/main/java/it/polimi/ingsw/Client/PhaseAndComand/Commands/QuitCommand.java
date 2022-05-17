package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;

import java.awt.event.ActionEvent;

public class QuitCommand extends GameCommand{

    public QuitCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() {
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
