package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.Controller.ControllerClient;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.HouseColor;

/**
 * PlayerJoined class is used to send info about the player that just joined the match to the client.
 */
public class PlayerJoined implements ToClientMessage {
    private final Player playerJoined;
    private final HouseColor teamColor;

    /**
     * Constructor PlayerJoined creates a new instance of PlayerJoined
     *
     * @param playerJoined of type {@link Player} - instance of the player that joined the match.
     * @param teamColor of type {@link HouseColor} - house color of the player's team.
     */
    public PlayerJoined(Player playerJoined, HouseColor teamColor) {
        this.playerJoined = playerJoined;
        this.teamColor = teamColor;
    }

    /**
     * Method execute uses the client controller to add the player to its player list.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMember(playerJoined, teamColor);
    }
}
