package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.util.ArrayList;
import java.util.List;

public class Char11Client implements CharacterCardClient {
    private final List<Integer> inputs;

    private boolean used;

    public Char11Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public void setUsed() {
        this.used = true;
    }

    @Override
    public String getDescription() {
        return "Choose a type of Student every player (including yourself) must return 3 Students of that type from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as many Students as they have.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws ScannerException {
        System.out.println("Select the color and everyone will put three students of that color from lunch hall to bag");
        inputs.add(view.getColorInput(false));
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 1;
    }

    @Override
    public byte getCost() {
        return (byte) (used ? 4 : 3);
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 1;
    }

    @Override
    public void resetInput() {
        inputs.clear();
    }

    @Override
    public String toString() {
        return "Thief";
    }

    @Override
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public int getCharId() {
        return 11;
    }
}

