package it.polimi.ingsw.Client.Controller;

import it.polimi.ingsw.Util.GamePhase;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener implements Runnable {
    private final ControllerClient controllerClient;
    private volatile boolean quit;
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
            } catch (IOException | ClassNotFoundException e) {
                synchronized (this) {
                    if (!quit) {
                        // server disconnection not caused by quit command
                        controllerClient.addMessage("Server connection lost");
                        quit = true;
                    } else
                        controllerClient.addMessage("Disconnected from server");
                }
                controllerClient.unsetModel();
                controllerClient.closeConnection();
                controllerClient.changePhase(GamePhase.INIT_PHASE, true, true);
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

    public synchronized void quit() {
        quit = true;
    }
}