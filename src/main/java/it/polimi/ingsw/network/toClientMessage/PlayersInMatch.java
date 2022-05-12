package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.model.Player;

import java.util.HashMap;

public class PlayersInMatch implements ToClientMessage{

    private final HashMap<Player, HouseColor> members;

    public PlayersInMatch(HashMap<Player, HouseColor> members) {
        this.members = members;
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMembers(members);
    }
}
