package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Server.model.Team;

import java.util.ArrayList;

public class EndGame implements ToClientMessage {
    ArrayList<Team> winners;

    public EndGame(ArrayList<Team> winners) {
        this.winners = winners;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        // someone is disconnected
        if (winners == null) {
            // return to select match phase, do not lose connection to server nor nickName set
            controllerClient.changePhase(GamePhase.SELECT_MATCH_PHASE);
            controllerClient.setQuit();
        }
    }
}
