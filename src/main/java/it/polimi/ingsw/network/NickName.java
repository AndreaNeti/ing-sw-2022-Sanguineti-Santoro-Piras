package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.PlayerHandler;
import it.polimi.ingsw.exceptions.GameException;

public class NickName implements ToServerMessage{
    String nickName;

    public NickName(String nickName) {
        this.nickName = nickName;
    }
    @Override
    public void execute(PlayerHandler playerHandler) throws GameException {
        playerHandler.nickName(nickName);
    }
}
