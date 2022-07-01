package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.client.Controller.ControllerClient;

/**
 * TextMessageCS class is used to send a text message from the server to the client.
 */
public class TextMessageSC implements ToClientMessage {
    private final String message;

    /**
     * Constructor TextMessageSC creates a new instance of TextMessageSC.
     *
     * @param message of type {@code String} - text message to send to the client.
     */
    public TextMessageSC(String message) {
        this.message = message;
    }

    /**
     * Method toString returns the message to send.
     *
     * @return {@code String} - "Message from (message)".
     */
    @Override
    public String toString() {
        return "Message from " + message;
    }

    /**
     * Method execute uses the client controller to add the message to its chat.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.addMessage(message);
    }
}
