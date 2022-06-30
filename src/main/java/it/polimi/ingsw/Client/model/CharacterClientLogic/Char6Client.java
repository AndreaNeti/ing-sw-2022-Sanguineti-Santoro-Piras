package it.polimi.ingsw.Client.model.CharacterClientLogic;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Server.model.CharacterServerLogic.Char6;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char6Client class represents the character card on the client side and corresponds to the server class {@link Char6}.
 */
public class Char6Client implements CharacterClientLogicInterface {
    private final List<Integer> inputs;
    private final Byte id;

    /**
     * Constructor Char6Client creates a new instance of Char6Client.
     */
    public Char6Client(Byte id) {
        this.id = id;
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color of the student from this card");
        inputs.add(view.getColorInput(false).ordinal());
        System.out.println("Select the color of the student from your entrance");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.size() % 2 == 0 && !isFull())
            viewGUI.enableStudentsOnCharacter(id, setInput(viewGUI));
        else if (inputs.size() % 2 == 1 && !isFull())
            viewGUI.enableEntrance(setInput(viewGUI));
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
            inputs.add(((Color) clicked.getProperties().get("color")).ordinal());
            System.out.println("input for jester :" + inputs);
            viewGUI.repeatPhase();
        };
    }

    @Override
    public boolean canPlay() {
        return inputs.size() % 2 == 0 && inputs.size() > 0 && inputs.size() <= 6;
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 6;
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
        return "Jester";
    }

}
