package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;

import java.util.List;

public class Char7Client implements CharacterCardClient {
    private boolean used;

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public String getDescription() {
        return "During the influence calculation this turn, you count as having 2 more influence.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) {

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
        return 7;
    }

    @Override
    public String toString() {
        return "Knight";
    }
}
