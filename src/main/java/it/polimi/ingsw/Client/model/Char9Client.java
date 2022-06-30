package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char9;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char9Client class represents the character card on the client side and corresponds to the server class {@link Char9}.
 */
public class Char9Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private CharacterCardDataInterface data;

    /**
     * Constructor Char9Client creates a new instance of Char9Client.
     */
    public Char9Client(CharacterCardDataInterface data) {
        this.data = data;
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "You may exchange up to 2 Students between your Dining and your Entrance Room.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color of the student from Lunch Hall");
        inputs.add(view.getColorInput(false).ordinal());
        System.out.println("Select the color of the student from your Entrance Hall");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.size() % 2 == 0 && !isFull()) {
            viewGUI.enableStudentsLunchHall(setInput(viewGUI));
        } else if (inputs.size() % 2 == 1 && !isFull()) {
            viewGUI.enableEntrance(setInput(viewGUI));
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
            inputs.add(((Color) clicked.getProperties().get("color")).ordinal());
            System.out.println(inputs);
            viewGUI.repeatPhase();
        };
    }

    @Override
    public boolean canPlay() {
        return (inputs.size() == 2 || inputs.size() == 4);
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 4;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Minstrel";
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

