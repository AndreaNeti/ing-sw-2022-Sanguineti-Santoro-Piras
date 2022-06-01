package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

public class Char1Client implements CharacterCardClient {
    private boolean used;

    @Override
    public String getDescription() {
        return "During this turn, you take control of any number of Professors even if you have the same number of Students as the player who currently controls them.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {

    }

    @Override
    public boolean canPlay() {
        return true;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 3 : 2);
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
        return 1;
    }

    @Override
    public void setUsed() {
        used = true;
    }

    @Override
    public String toString() {
        return "Farmer";
    }
}