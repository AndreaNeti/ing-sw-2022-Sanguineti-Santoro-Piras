package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.PlayerHandler;

public class Quit implements ToServerMessage {

    @Override
    public void execute(PlayerHandler playerHandler) {
        playerHandler.quit();
    }
}
