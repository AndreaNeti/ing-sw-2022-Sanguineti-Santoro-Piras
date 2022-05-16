/*package it.polimi.ingsw.Client.PhaseAndComand;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.Controller.GamePhaseController;
import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.View.GamePhaseView;

import java.util.List;

public abstract class GamePhase implements GamePhaseView, GamePhaseController {
    List<GameCommand> gameCommands;
    ControllerClient controllerClient;
    public void GamePhase(List<GameCommand> gameCommands, ControllerClient controllerClient){
        this.gameCommands=gameCommands;
        this.controllerClient=controllerClient;
    };

    public List<GameCommand> getGameCommands() {
        return gameCommands;
    }
    @Override
    public void setView(List<GameClientListener> gameClientListeners) {
        for (GameClientListener gL : gameClientListeners) {
            gL.setView(this);
        }
    }
}
*/