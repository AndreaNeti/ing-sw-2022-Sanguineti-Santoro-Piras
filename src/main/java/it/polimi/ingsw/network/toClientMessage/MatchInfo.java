package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.Controller.ControllerClient;
import it.polimi.ingsw.server.model.Team;
import it.polimi.ingsw.util.MatchConstants;
import it.polimi.ingsw.util.MatchType;
import it.polimi.ingsw.util.Wizard;

import java.util.List;

/**
 * MatchInfo class is used to sent info about the game to the client when it joins a match.
 */
public class MatchInfo implements ToClientMessage {
    private final Wizard yourWizard;
    private final MatchType matchType;
    private final MatchConstants constants;
    private final long matchId;

    private final List<Team> teams;

    /**
     * Constructor MatchInfo creates a new instance of MatchInfo.
     *
     * @param matchType  of type {@link MatchType} - type of the match joined.
     * @param matchId    of type {@code long} - unique ID of the match.
     * @param teams      of type {@code List}<{@link Team}> - list of teams in the match.
     * @param yourWizard of type {@link Wizard} - wizard assigned to the player.
     */
    public MatchInfo(MatchType matchType, MatchConstants constants, long matchId, List<Team> teams, Wizard yourWizard) {
        this.matchType = matchType;
        this.constants = constants;
        this.matchId = matchId;
        this.teams = teams;
        this.yourWizard = yourWizard;
    }

    /**
     * Method execute uses the client controller to set the match info with the selected data and add a message to its chat
     * with this info.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage(matchType + " (ID: " + matchId + ")");
        controllerClient.setNextClientPhase();
        controllerClient.setMatchInfo(matchType, constants, teams, yourWizard);
    }
}
