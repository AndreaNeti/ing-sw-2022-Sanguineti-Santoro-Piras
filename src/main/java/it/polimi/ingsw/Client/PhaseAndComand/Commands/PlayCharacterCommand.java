package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;

import java.awt.event.ActionEvent;

public class PlayCharacterCommand extends GameCommand{
    public PlayCharacterCommand(AbstractView view) {
        super(view);
    }

    @Override
    void playCLICommand() {

    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
