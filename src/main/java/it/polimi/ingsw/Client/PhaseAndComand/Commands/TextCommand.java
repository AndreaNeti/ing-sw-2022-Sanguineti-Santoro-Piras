package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionEvent;

public class TextCommand extends GameCommand{
    public TextCommand(AbstractView view) {
        super(view);
    }

    @Override
    public void playCLICommand() throws PhaseChangedException {

    }

    @Override
    public String toString() {
        return "Send message";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
