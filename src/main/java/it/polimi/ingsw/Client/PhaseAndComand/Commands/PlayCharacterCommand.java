package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class PlayCharacterCommand extends GameCommand{
    public PlayCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {

    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
