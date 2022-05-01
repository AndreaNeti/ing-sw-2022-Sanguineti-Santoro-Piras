package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Team;

import java.util.ArrayList;

public class EndGame implements ToClientMessage{
    ArrayList<Team> winners;

    public EndGame(ArrayList<Team> winners) {
        this.winners = winners;
    }

}
