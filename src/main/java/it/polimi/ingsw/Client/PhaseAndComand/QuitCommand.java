package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.AbstractView;

import java.awt.event.ActionEvent;

public class QuitCommand extends GameCommand{
    public QuitCommand(AbstractView view) {
        super(view);
    }

    @Override
    void playCLICommand() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
