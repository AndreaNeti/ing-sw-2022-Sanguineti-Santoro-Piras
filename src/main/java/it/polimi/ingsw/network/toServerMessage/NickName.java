package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.Server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.GameException;

public class NickName implements ToServerMessage{
    String nickName;

    public NickName(String nickName) {
        this.nickName = nickName;
    }
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        clientHandler.setNickName(nickName);
    }
}
