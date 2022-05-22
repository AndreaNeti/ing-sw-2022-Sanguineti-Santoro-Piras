package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.ArrayList;
import java.util.List;

public class Char8Client implements CharacterCardClient {
    private final List<Integer> inputs;

    public Char8Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Choose a color of Student: during the influence calculation this turn, that color adds no influence.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws PhaseChangedException {
        System.out.println("Select the color you want to ignore while calculating the influence");
        inputs.add(view.getColorInput());
    }

    @Override
    public boolean canPlay() {
        return inputs.size()==1;
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public boolean isFull() {
        return inputs.size()==1;
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
    public String toString() {
        return "Mushroom man";
    }
}
