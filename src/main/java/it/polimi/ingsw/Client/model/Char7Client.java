package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.Char7;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * Char7Client class represents the character card on the client side and corresponds to the server class {@link Char7}.
 */
public class Char7Client implements CharacterCardClient {
    private CharacterCardDataInterface data;

    public Char7Client(CharacterCardDataInterface data) {
        this.data = data;
    }

    @Override
    public String getDescription() {
        return "During the influence calculation this turn, you count as having 2 more influence.";
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
    public String toString() {
        return "Knight";
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
