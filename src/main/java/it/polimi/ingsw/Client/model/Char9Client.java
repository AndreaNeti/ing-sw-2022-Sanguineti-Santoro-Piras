package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.ArrayList;
import java.util.List;

public class Char9Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private boolean used;

    public Char9Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public String getDescription() {
        return "You may exchange up to 2 Students between your Entrance and your Dining Room.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        System.out.println("Select the color of the student from Lunch Hall");
        inputs.add(view.getColorInput(false).ordinal());
        System.out.println("Select the color of the student from your Entrance Hall");
        inputs.add(view.getColorInput(false).ordinal());
    }

    @Override
    public boolean canPlay() {
        return (inputs.size() == 2 || inputs.size() == 4);
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 2 : 1);
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 4;
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
        return 9;
    }

    @Override
    public String toString() {
        return "Minstrel";
    }
}

