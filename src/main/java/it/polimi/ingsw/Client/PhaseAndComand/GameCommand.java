package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.View.AbstractView;

import java.awt.event.ActionListener;

public abstract class GameCommand implements ActionListener {
    private final AbstractView view;

    public GameCommand(AbstractView view) {
        this.view = view;
    }

    abstract void playCLICommand();

    protected AbstractView getView() {
        return view;
    }
}
