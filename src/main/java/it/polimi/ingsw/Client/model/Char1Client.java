package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char1;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * Char1Client class represents the character card on the client side and corresponds to the server class {@link Char1}.
 */
public class Char1Client implements CharacterCardClient {

    private CharacterCardDataInterface data;

    public Char1Client(CharacterCardDataInterface data) {
        this.data = data;
    }

    @Override
    public String getDescription() {
        return "During this turn, you take control of any number of Professors even if you have the same number of Students as the player who currently controls them.";
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
    public void setData(CharacterCardDataInterface data) {
        this.data = data;
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

    /**
     * Method toString returns the name of the character card.
     *
     * @return {@code String} - character card name.
     */
    @Override
    public String toString() {
        return "Farmer";
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