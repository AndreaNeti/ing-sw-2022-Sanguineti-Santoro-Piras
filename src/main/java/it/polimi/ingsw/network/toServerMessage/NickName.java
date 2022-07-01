package it.polimi.ingsw.network.toServerMessage;

import it.polimi.ingsw.server.controller.ClientHandler;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;

/**
 * NickName class is used to set the nickname of the client's player.
 */
public class NickName implements ToServerMessage{
    private final String nickName;

    /**
     * Constructor NickName creates a new instance of NickName.
     *
     * @param nickName of type {@code String} - nickname of the player.
     */
    public NickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Method execute calls the {@link ClientHandler#setNickName(String)} method to assign the selected
     * nickname for the player.
     *
     * @param clientHandler of type {@link ClientHandler} - instance of the client handler that sends the message.
     * @throws GameException if the selected nickname is already taken.
     */
    @Override
    public void execute(ClientHandler clientHandler) throws GameException {
        clientHandler.setNickName(nickName);
    }
}
