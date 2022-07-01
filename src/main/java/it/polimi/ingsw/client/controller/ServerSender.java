package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class ServerSender is used by the client to send the messages to the server to execute the
 * respective command. It implements the ping pong interface so it can send
 */
public class ServerSender {
    private final ObjectOutputStream objOut;

    /**
     * Constructor ServerSender creates a new instance of ServerSender.
     *
     * @param socket of type {@code Socket} - socket connection between the server and the client.
     */
    public ServerSender(Socket socket) {
        try {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Method sendServerMessage sends a {@link ToServerMessage} to the server, to execute its command.
     *
     * @param command of type {@link ToServerMessage} - instance of the message to send to the server.
     */
    public void sendServerMessage(ToServerMessage command) {
        synchronized (objOut) {
            try {
                objOut.reset();
                objOut.writeObject(command);
                objOut.flush();
            } catch (IOException e) {
                System.err.println("Connection closed");
            }
        }
    }

    /**
     * Method closeStream closes the output stream from the server sender to the server.
     */
    public void closeStream() {
        try {
            objOut.close();
        } catch (IOException e) {
            System.err.println("Stream closed");
        }
    }
}
