package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char10;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char10Client class represents the character card on the client side and corresponds to the server class {@link Char10}.
 */
public class Char10Client extends CharacterCardClientWithStudents {
    private final List<Integer> inputs;

    private CharacterCardDataInterface data;

    /**
     * Constructor Char10Client creates a new instance of Char10Client.
     */
    public Char10Client(CharacterCardDataInterface data) {
        super(-12);
        this.data = data;
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the Bag and place it on this card.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color of the student from this card");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty()) viewGUI.enableStudentsOnCharacter(getId(), setInput(viewGUI));

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
        return "Spoiled princess";
    }

    @Override
    public byte getCost() {
        return data.getCost();
    }

    @Override
    public byte getCharId() {
        return data.getCharId();
    }

    @Override
    public boolean isUsed() {
        return data.isUsed();
    }

    @Override
    public boolean hasStudents() {
        return data.hasStudents();
    }

    @Override
    public void setUsed() {
        data.setUsed();
    }

    @Override
    public void setData(CharacterCardDataInterface data) {
        this.data = data;
    }
}

