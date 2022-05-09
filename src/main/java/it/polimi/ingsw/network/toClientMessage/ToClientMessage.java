package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

import java.io.Serializable;

public interface ToClientMessage extends Serializable {
    void execute(ControllerClient controllerClient);
}
