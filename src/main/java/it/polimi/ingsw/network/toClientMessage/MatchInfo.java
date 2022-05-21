package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchType;

public class MatchInfo implements ToClientMessage {
    Wizard yourWizard;
    MatchType matchType;
    long matchId;

    public MatchInfo(MatchType matchType, long matchId, Wizard wizard) {
        this.matchType = matchType;
        this.matchId = matchId;
        this.yourWizard = wizard;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.setMatchTypeAndWizard(matchType, yourWizard);
        System.out.println("Match info: " + matchType + ", ID: " + matchId);
        System.out.println("Your wizard is: " + yourWizard);
    }
}
