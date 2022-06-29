package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char2;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Char2Client class represents the character card on the client side and corresponds to the server class {@link Char2}.
 */
public class Char2Client implements CharacterCardClient {
    private boolean used;
    private final List<Integer> inputs;

    /**
     * Constructor Char2Client creates a new instance of Char2Client.
     */
    public Char2Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        inputs.add(view.getIslandDestination("Select the island where you want to calculate influence", false));
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        if (inputs.isEmpty())
            viewGUI.enableIslands(setInput(viewGUI));
    }

    private EventHandler<MouseEvent> setInput(ViewGUI viewGUI) {
        return mouseEvent -> {
            Node clicked = (Node) mouseEvent.getSource();
            int relativeId = (int) clicked.getProperties().get("relativeId");
            inputs.add(relativeId);
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

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public int getCharId() {
        return 2;
    }

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Herald";
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public boolean containsStudents() {
        return false;
    }
}