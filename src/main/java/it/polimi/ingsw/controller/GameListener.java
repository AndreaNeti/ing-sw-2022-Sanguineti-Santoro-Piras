package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

public interface GameListener {
    void update(ToClientMessage m);
}
