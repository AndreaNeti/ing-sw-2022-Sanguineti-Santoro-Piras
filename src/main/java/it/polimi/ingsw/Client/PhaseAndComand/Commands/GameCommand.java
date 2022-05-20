package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.Controller.ClientPhaseController;
import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.awt.event.ActionListener;
import java.util.List;

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

    public void attachToAPhase(List<ClientPhaseController> clientPhases) {
        for (ClientPhaseController clientPhase : clientPhases) {
            clientPhase.addCommand(this);
        }
    }
}
