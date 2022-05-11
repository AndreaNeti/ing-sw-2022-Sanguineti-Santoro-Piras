package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.network.toClientMessage.ErrorException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerListener implements Runnable {
    private final ControllerClient controllerClient;
    private boolean quit;
    private final ObjectInputStream objIn;

    public ServerListener(Socket socket, ControllerClient controllerClient) {

        try {
            objIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.controllerClient = controllerClient;
        quit=false;
    }

    @Override
    public void run() {
        ToClientMessage received;
        do {
            try {
                //TODO catch exception when server is down
                received = (ToClientMessage) objIn.readObject();
                received.execute(controllerClient);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } while (!quit);
    }

    public void quit() {
        this.quit = true;
    }
}