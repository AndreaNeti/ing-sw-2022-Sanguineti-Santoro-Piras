package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ServerListener implements Runnable {
    private final ControllerClient controllerClient;
    private boolean quit;
    private final ObjectInputStream objIn;

    private final Socket socket;

    public ServerListener(Socket socket, ControllerClient controllerClient) {
        this.socket = socket;
        try {
            objIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.controllerClient = controllerClient;
        quit = false;
    }

    @Override
    public void run() {
        ToClientMessage received;
        do {
            try {
                received = (ToClientMessage) objIn.readObject();
                received.execute(controllerClient);
            } catch (SocketException e) {
                System.err.println("Server connection lost");
                controllerClient.changePhase(GamePhase.INIT_PHASE);
                return;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } while (!quit);
        try {
            objIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void quit() {
        quit = true;
    }
}