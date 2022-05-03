package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;

public class TextMessageCS implements ToServerMessage {
    String message;

    public TextMessageCS(String message) {
        this.message = message;
    }

    @Override
    public void execute(PlayerHandler playerHandler) {
        playerHandler.getController().sendMessage(playerHandler.getNickName(), message);
    }
}
