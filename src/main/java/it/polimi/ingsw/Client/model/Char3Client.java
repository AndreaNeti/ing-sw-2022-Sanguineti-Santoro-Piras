package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;

import java.util.List;

public class Char3Client implements CharacterCardClient{

    @Override
    public String getDescription() {
        return "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws RepeatCommandException {
    }

    @Override
    public boolean canPlay() {
        return true;
    }

    @Override
    public byte getCost() {
        return 1;
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