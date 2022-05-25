package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.util.ArrayList;
import java.util.List;

public class Char0Client extends CharacterCardClientWithStudents {
    private final List<Integer> inputs;
    private boolean used;

    @Override
    public void setUsed() {
        this.used = true;
    }

    public Char0Client() {
        super(-10);
        inputs = new ArrayList<>();
        used = false;
    }

    @Override
    public String getDescription() {
        return "Take 1 Student from this card and place it on an Island of your choice. Then, draw a new Student from the Bag and place it on this card.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws ScannerException {
        if (inputs.size() == 0) {
            inputs.add(view.getColorInput(false));
        } else {
            inputs.add(view.getIslandDestination("Select the island where you want to put the student", false));
        }
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 2;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 2 : 1);
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 2;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    @Override
    public String toString() {
        return "Monk";
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public int getCharId() {
        return 0;
    }

}
