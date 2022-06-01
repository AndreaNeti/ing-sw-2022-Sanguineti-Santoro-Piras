package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

public class Char5Client implements CharacterCardClient {
    private boolean used;

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public String getDescription() {
        return "When resolving a Conquering on an Island, Towers do not count towards influence.";
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
        return (byte) (used ? 4 : 3);
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
        return 5;
    }

    @Override
    public String toString() {
        return "Centaur";
    }
}
