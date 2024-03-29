package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.view.cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.server.model.characterServerLogic.Char4;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char4Client class represents the character card on the client side and corresponds to the server class {@link Char4}.
 */
public class Char4Client implements CharacterClientLogicInterface {
    private final List<Integer> inputs;

    /**
     * Constructor Char4Client creates a new instance of Char4Client.
     */
    public Char4Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, put the No Entry tile back onto this card DO NOT calculate influence on that Island, or place any Towers.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        inputs.add(view.getIslandDestination("Select the island where you want to put a prohibition", false));
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty()) {
            viewGUI.enableIslands(setInput(viewGUI));
        }
    }

    /**
     * Method setInputs returns the event handler for a mouse event to add to a specific node of the GUI in order to obtain the card required inputs.
     *
     * @param viewGUI of type {@link ViewGUI} - client's GUI view from which the inputs are obtained.
     * @return {@code EventHandler}<{{@code MouseEvent}> - function that will be executed when the node that adds the
     * event handler is clicked.
     */
    private EventHandler<MouseEvent> setInput(ViewGUI viewGUI) {
        return mouseEvent -> {
            Node clicked = (Node) mouseEvent.getSource();
            int relativeId = (int) clicked.getProperties().get("relativeId");
            inputs.add(relativeId);
            viewGUI.repeatPhase();
        };
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 1;
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 1;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "Grandma weeds";
    }
}
