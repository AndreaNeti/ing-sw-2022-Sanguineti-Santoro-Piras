package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char11;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char11Client class represents the character card on the client side and corresponds to the server class {@link Char11}.
 */
public class Char11Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private boolean used;

    /**
     * Constructor Char11Client creates a new instance of Char11Client.
     */
    public Char11Client() {
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
        return "Choose a type of Student every player (including yourself) must return 3 Students of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color and everyone will put three students of that color from lunch hall to bag");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty()) {
            viewGUI.enableColorBox(setInput(viewGUI));
        }
    }
    private EventHandler<MouseEvent> setInput(ViewGUI viewGUI) {
        return mouseEvent -> {
            Node clicked = (Node) mouseEvent.getSource();
            inputs.add(((Color) clicked.getProperties().get("color")).ordinal());
            viewGUI.repeatPhase(false);
        };
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 1;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 4 : 3);
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 1;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Thief";
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public int getCharId() {
        return 11;
    }
}

