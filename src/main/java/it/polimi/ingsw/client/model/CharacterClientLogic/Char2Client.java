package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.client.View.Gui.ViewGUI;
import it.polimi.ingsw.server.model.CharacterServerLogic.Char2;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char2Client class represents the character card on the client side and corresponds to the server class {@link Char2}.
 */
public class Char2Client implements CharacterClientLogicInterface {
    private final List<Integer> inputs;

    /**
     * Constructor Char2Client creates a new instance of Char2Client.
     */
    public Char2Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        inputs.add(view.getIslandDestination("Select the island where you want to calculate influence", false));
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty())
            viewGUI.enableIslands(setInput(viewGUI));
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
        return "Herald";
    }

}