package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

public class OK implements ToClientMessage{
    String ok;

    public OK() {
        ok = "Paolinok";
    }

    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.ok();
    }
}
