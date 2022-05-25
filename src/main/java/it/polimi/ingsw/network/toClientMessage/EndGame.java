package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;

import java.util.List;

public class EndGame implements ToClientMessage {
    List<HouseColor> winners;

    public EndGame(List<HouseColor> winners) {
        this.winners = winners;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        // someone is disconnected
        if (winners == null || winners.isEmpty()) {
            // return to select match phase, do not lose connection to server nor nickName set
            controllerClient.setQuit(true);
            controllerClient.addMessage("Someone disconnected from match");
        } else {
            controllerClient.notifyWinners(winners);
            controllerClient.changePhase(GamePhase.WAIT_PHASE, true, true);
        }
    }
}
