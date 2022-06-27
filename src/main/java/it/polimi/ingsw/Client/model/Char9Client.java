package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char9;
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
    private boolean used;
    private final List<Integer> inputs;

    /**
     * Constructor Char9Client creates a new instance of Char9Client.
     */
    public Char9Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public boolean containsStudents() {
        return false;
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

    private EventHandler<MouseEvent> setInput(ViewGUI viewGUI) {
        return mouseEvent -> {
            Node clicked = (Node) mouseEvent.getSource();
            inputs.add(((Color) clicked.getProperties().get("color")).ordinal());
            System.out.println(inputs);
            viewGUI.repeatPhase(false);
        };
    }

    @Override
    public boolean canPlay() {
        return (inputs.size() == 2 || inputs.size() == 4);
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 2 : 1);
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

    @Override
    public int getCharId() {
        return 9;
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
}

