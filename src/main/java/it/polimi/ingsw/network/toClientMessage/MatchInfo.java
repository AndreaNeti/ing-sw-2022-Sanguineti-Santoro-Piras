package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.model.Team;

import java.util.List;

public class MatchInfo implements ToClientMessage {
    private final Wizard yourWizard;
    private final MatchType matchType;
    private final long matchId;

    private final List<Team> teams;

    public MatchInfo(MatchType matchType, long matchId, List<Team> teams, Wizard yourWizard) {
        this.matchType = matchType;
        this.matchId = matchId;
        this.teams = teams;
        this.yourWizard = yourWizard;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.setMatchInfo(matchType, teams, yourWizard);
        System.out.println("Match info: " + matchType + ", ID: " + matchId);
        System.out.println("Your wizard is: " + yourWizard);
    }
}
