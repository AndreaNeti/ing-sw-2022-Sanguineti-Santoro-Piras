package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char4;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char4Client class represents the character card on the client side and corresponds to the server class {@link Char4}.
 */
public class Char4Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private CharacterCardDataInterface data;

    /**
     * Constructor Char4Client creates a new instance of Char4Client.
     */
    public Char4Client(CharacterCardDataInterface data) {
        this.data = data;
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
            System.out.println("input for grandma :" + inputs);
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
