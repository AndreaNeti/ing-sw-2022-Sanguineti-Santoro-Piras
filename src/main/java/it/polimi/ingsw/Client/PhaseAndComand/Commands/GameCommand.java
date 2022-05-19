package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionListener;

public abstract class GameCommand implements ActionListener {
    private final AbstractView view;

    public GameCommand(AbstractView view) {
        this.view = view;
    }

   public abstract void playCLICommand() throws PhaseChangedException;

    protected AbstractView getView() {
        return view;
    }
    public abstract String toString();

}
