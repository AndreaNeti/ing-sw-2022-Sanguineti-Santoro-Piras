package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;

public class Quit implements ToServerMessage {

    @Override
    public void execute(PlayerHandler playerHandler) {
        playerHandler.quit();
    }
}
