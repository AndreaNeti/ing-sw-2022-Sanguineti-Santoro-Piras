package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Server.controller.MatchType;

public class MatchInfo implements ToClientMessage {
    MatchType matchType;
    long matchId;

    public MatchInfo(MatchType matchType, long matchId) {
        this.matchType = matchType;
        this.matchId = matchId;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.setMatchType(matchType);
        System.out.println("Match info: " + matchType + ", ID: " + matchId);
    }
}
