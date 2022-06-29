package it.polimi.ingsw.network.toClientMessage;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.network.GameDelta;

/**
 * DeltaUpdate class is used to update the client with a new game delta.
 */
public class DeltaUpdate implements ToClientMessage {
    GameDelta gameDelta;

    /**
     * Constructor DeltaUpdate creates a new instance of DeltaUpdate.
     *
     * @param gameDelta of type {@link GameDelta} - instance of the game delta to send to the client.
     */
    public DeltaUpdate(GameDelta gameDelta) {
        this.gameDelta = gameDelta;
    }

    /**
     * Method execute uses the client controller to update the game based on the game delta info.
     *
     * @param controllerClient of type {@link ControllerClient} - instance of the client controller that receives the message.
     */
    @Override
    public void execute(ControllerClient controllerClient) {
        controllerClient.changeGame(gameDelta);
    }

    /**
     * Method toString returns the game delta.
     *
     * @return String - output of {@link GameDelta#toString()} method
     */
    @Override
    public String toString() {
        return gameDelta.toString();
    }
}
