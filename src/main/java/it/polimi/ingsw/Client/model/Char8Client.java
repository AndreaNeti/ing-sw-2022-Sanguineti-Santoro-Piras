package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char8;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char8Client class represents the character card on the client side and corresponds to the server class {@link Char8}.
 */
public class Char8Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private CharacterCardDataInterface data;

    /**
     * Constructor Char8Client creates a new instance of Char8Client.
     */
    public Char8Client(CharacterCardDataInterface data) {
        this.data = data;
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Choose a color of Student: during the influence calculation this turn, that color adds no influence.";
    }


    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color you want to ignore while calculating the influence");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty()) {
            viewGUI.enableColorBox(setInput(viewGUI));
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

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Mushroom man";
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
