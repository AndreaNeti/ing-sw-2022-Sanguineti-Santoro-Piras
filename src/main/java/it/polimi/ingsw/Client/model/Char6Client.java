package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char6;
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
public class Char6Client extends CharacterCardClientWithStudents {
    private boolean used;
    private final List<Integer> inputs;

    /**
     * Constructor Char6Client creates a new instance of Char6Client.
     */
    public Char6Client() {
        super(-11);
        inputs = new ArrayList<>();
    }

    @Override
    public void setUsed() {
        this.used = true;
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
        if (inputs.size() %2== 0 && !isFull())
            viewGUI.enableStudentsOnCharacter(getId(), setInput(viewGUI));
        else if (inputs.size() %2== 1 && !isFull())
            viewGUI.enableEntrance(setInput(viewGUI));
    }

    private EventHandler<MouseEvent> setInput(ViewGUI viewGUI) {
        return mouseEvent -> {
            Node clicked = (Node) mouseEvent.getSource();
            inputs.add(((Color) clicked.getProperties().get("color")).ordinal());
            System.out.println("input for jester :" + inputs);
            viewGUI.repeatPhase(false);
        };
    }

    @Override
    public boolean canPlay() {
        return inputs.size() % 2 == 0 && inputs.size() > 0 && inputs.size() <= 6;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 2 : 1);
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
    public int getCharId() {
        return 6;
    }

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Jester";
    }
}
