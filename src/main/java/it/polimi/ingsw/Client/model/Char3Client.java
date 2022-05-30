package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;

import java.util.List;

public class Char3Client implements CharacterCardClient {
    private boolean used;

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public String getDescription() {
        return "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.";
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

    @Override
    public String toString() {
        return "Magic postman";
    }
}