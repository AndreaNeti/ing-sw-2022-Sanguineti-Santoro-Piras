package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.ArrayList;
import java.util.List;

public class Char9Client implements CharacterCardClient {
    private final List<Integer> inputs;

    public Char9Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "You may exchange up to 2 Students between your Entrance and your Dining Room,";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws PhaseChangedException {
        if(inputs.size()%2==0)
            System.out.println("Select the color of the student from lunch hall");
        else
            System.out.println("Select the color of the student from your entrance ");

        inputs.add(view.getColorInput());
    }

    @Override
    public boolean canPlay() {
        return inputs.size()==2;
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public boolean isFull() {
        return inputs.size()==2;
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

