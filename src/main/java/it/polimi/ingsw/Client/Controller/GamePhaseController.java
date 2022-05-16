package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Client.PhaseAndComand.GamePhase;
import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.GameClientListener;

import java.util.List;

public interface GamePhaseController {
    void setView(List<AbstractView> views);
}
