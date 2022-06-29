package it.polimi.ingsw.network;

/**
 * PingPongInterface interface is used to send ping messages between the client and the server, reset the timeout timer and to close the connection if
 * the timeout expires. <br>
 * This interface is implemented by ClientHandler and ControllerClient, in order to be passed as a unique arguments to {@link PingMessage}.
 */
public interface PingPongInterface {
    /**
     * Method sendPingPong sends a {@link PingMessage} to the server.
     */
    void sendPingPong();

    /**
     * Method quit closes the connection between the client and the server.
     */
    void quit();

    /**
     * Method resetPing resets the time of the {@link PingPong} timer.
     */
    void resetPing();
}
