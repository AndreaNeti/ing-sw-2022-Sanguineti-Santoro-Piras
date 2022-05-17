package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;

import java.awt.event.ActionEvent;

public class TextCommand extends GameCommand{
    public TextCommand(AbstractView view) {
        super(view);
    }

    @Override
    void playCLICommand() {

    }

    @Override
    public String toString() {
        return "Send message";
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
