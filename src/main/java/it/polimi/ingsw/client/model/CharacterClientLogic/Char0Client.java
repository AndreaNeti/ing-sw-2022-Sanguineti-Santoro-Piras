package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.view.Cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.Gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.server.model.CharacterServerLogic.Char0;
import it.polimi.ingsw.utils.Color;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char0Client class represents the character card on the client side and corresponds to the server class {@link Char0}.
 */
public class Char0Client implements CharacterClientLogicInterface {
    private final List<Integer> inputs;
    private final Byte id;

    /**
     * Constructor Char0Client creates a new instance of Char0Client.
     */
    public Char0Client(Byte id) {
        this.id = id;
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Take 1 Student from this card and place it on an Island of your choice. Then, draw a new Student from the Bag and place it on this card.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        inputs.add(view.getColorInput(false).ordinal());
        inputs.add(view.getIslandDestination("Select the island where you want to put the student", false));
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {

        if (inputs.size() == 0) {
            //this card has id -10 (it's a gameComponent)
            viewGUI.enableStudentsOnCharacter(id, setInput(viewGUI));
        }//else-> it means that color has already been chosen, now I need to set island
        else if (inputs.size() == 1) {
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
            Color c = (Color) clicked.getProperties().get("color");
            if (c != null) {
                inputs.add(c.ordinal());
            } else {
                int id;
                if (clicked.getProperties().get("relativeId") == null)
                    id = Integer.parseInt(clicked.getId());
                else
                    id = (int) clicked.getProperties().get("relativeId");
                inputs.add(id);
            }
            viewGUI.repeatPhase();
        };
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 2;
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 2;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    @Override
    public String toString() {
        return "Monk";
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }
}
