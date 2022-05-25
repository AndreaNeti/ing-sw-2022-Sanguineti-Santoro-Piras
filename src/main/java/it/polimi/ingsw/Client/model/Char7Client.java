package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;

import java.util.List;

public class Char7Client implements CharacterCardClient {


    @Override
    public String getDescription() {
        return "During the influence calculation this turn, you count as having 2 more influence.";
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
        return 2;
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
