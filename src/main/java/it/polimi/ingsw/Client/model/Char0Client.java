package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char0;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char0Client class represents the character card on the client side and corresponds to the server class {@link Char0}.
 */
public class Char0Client extends CharacterCardClientWithStudents {
    private final List<Integer> inputs;

    private CharacterCardDataInterface data;

    /**
     * Constructor Char0Client creates a new instance of Char0Client.
     */
    public Char0Client(CharacterCardDataInterface data) {
        super(-10);
        this.data = data;
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
            viewGUI.enableStudentsOnCharacter(getId(), setInput(viewGUI));
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
                // TODO mh
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
    public void setData(CharacterCardDataInterface data) {
        this.data = data;
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

    @Override
    public String getNameOfComponent() {
        return "CharacterCard";
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
}
