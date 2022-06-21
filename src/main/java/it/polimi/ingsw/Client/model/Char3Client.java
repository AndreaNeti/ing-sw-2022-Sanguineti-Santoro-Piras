package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char3;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * Char3Client class represents the character card on the client side and corresponds to the server class {@link Char3}.
 */
public class Char3Client implements CharacterCardClient {
    private boolean used;

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
        return "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
    }

    @Override
    public boolean canPlay() {
        return true;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 2 : 1);
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public void resetInput() {

    }

    @Override
    public List<Integer> getInputs() {
        return null;
    }

    @Override
    public int getCharId() {
        return 3;
    }

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Magic postman";
    }
}