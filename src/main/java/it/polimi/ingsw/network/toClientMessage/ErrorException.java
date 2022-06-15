package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;

/**
 * ErrorException class is used to send an error message to the client.
 */
public class ErrorException implements ToClientMessage {
    private final String error;

    /**
     * Constructor ErrorException creates a new instance of ErrorException.
     *
     * @param error of type {@code String} - error to send.
     */
    public ErrorException(String error) {
        this.error = error;
    }

    /**
     * Method toString returns the error.
     *
     * @return {@code String} - "Error: (error)"
     */
    @Override
    public String toString() {
        return "Error: " + error;
    }

    /**
     * Method execute uses the client controller to add the error message to its chat and handle the error, repeating the phase.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage("Server error: " + error);
        controllerClient.error();
    }
}
