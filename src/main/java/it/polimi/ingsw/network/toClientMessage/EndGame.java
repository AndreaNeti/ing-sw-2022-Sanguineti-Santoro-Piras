package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Server.model.Team;

import java.util.ArrayList;

public class EndGame implements ToClientMessage{
    ArrayList<Team> winners;

    public EndGame(ArrayList<Team> winners) {
        this.winners = winners;
    }

    @Override
    public void execute(ControllerClient controllerClient) {

    }
}
