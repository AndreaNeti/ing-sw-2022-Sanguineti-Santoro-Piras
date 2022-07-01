package it.polimi.ingsw.client.Controller;

import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Class ServerListener represents the thread that listens to any server message sent to the client and execute the
 * respective command using the client's controller.
 */
public class ServerListener implements Runnable {
    private final ControllerClient controllerClient;
    private volatile boolean quit;
    private final ObjectInputStream objIn;
    private final Socket socket;

    /**
     * Constructor ServerListener creates a new instance of ServerListener.
     *
     * @param socket of type {@code Socket} - socket connection between the server and the client.
     * @param controllerClient of type {@link ControllerClient} - instance of the client's controller.
     */
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

    /**
     * Method run waits for a server's command ({@link ToClientMessage}) to be received via socket connection and then executes it to
     * modify the game through the client's controller. <br>
     * This method will end only after the client quits the game while not in a game or if there's an error with
     * the socket connection, closing the socket, relative data streams and resetting the controller's model and
     * phase. <br>
     */
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
                controllerClient.changePhase(GamePhase.INIT_PHASE, true);
            }
        } while (!quit);
        try {
            objIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method quit closes the connection between the server listener and the server.
     */
    public synchronized void quit() {
        quit = true;
    }
}