package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Server.controller.Controller;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.network.toClientMessage.ErrorException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender {
    private final ObjectOutputStream objOut;


    public ServerSender(Socket socket){
        try {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void sendServerMessage(ToServerMessage command){
        synchronized (objOut) {
            try {
                objOut.writeObject(command);
                objOut.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
