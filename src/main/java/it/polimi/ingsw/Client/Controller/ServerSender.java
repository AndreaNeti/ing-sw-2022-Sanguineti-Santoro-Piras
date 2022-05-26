package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
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
    public void closeStream(){
        try {
            objOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
