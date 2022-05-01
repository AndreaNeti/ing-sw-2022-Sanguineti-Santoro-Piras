package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.ToClientMessage;

public interface GameListener {
    void update(ToClientMessage m);
}
