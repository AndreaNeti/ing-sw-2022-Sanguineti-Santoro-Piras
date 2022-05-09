package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

public interface GameListener {
    void update(ToClientMessage m);
}
