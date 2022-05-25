package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.RepeatCommandException;
import it.polimi.ingsw.exceptions.clientExceptions.ScannerException;

import java.util.ArrayList;
import java.util.List;

public class Char6Client extends CharacterCardClientWithStudents {
    private final List<Integer> inputs;

    public Char6Client() {
        super(-11);
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "You may take up to 3 Students from this card and replace them with the same number of Students from your Entrance.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws ScannerException {
        if (inputs.size() % 2 == 0)
            System.out.println("Select the color of the student from this card " + super.toString());
        else
            System.out.println("Select the color of the student from your entrance ");

        inputs.add(view.getColorInput(false));
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 2;
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public boolean isFull() {
        return inputs.size() == 6;
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
        return 6;
    }

    @Override
    public String toString() {
        return "Jester";
    }
}
