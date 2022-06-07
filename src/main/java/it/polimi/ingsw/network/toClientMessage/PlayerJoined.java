package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Server.model.Player;

public class PlayerJoined implements ToClientMessage {
    private final Player playerJoined;
    private final HouseColor teamColor;

    public PlayerJoined(Player playerJoined, HouseColor teamColor) {
        this.playerJoined = playerJoined;
        this.teamColor = teamColor;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMember(playerJoined, teamColor);
    }
}
