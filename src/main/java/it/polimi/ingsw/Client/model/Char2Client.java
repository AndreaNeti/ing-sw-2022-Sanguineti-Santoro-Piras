package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.ArrayList;
import java.util.List;

public class Char2Client implements CharacterCardClient {
    private final List<Integer> inputs;

    public Char2Client() {
        inputs = new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Choose an Island and resolve the Island as if Mother Nature had ended her movement there. Mother Nature will still move and the Island where she ends her movement will also be resolved.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws PhaseChangedException {
        inputs.add(view.getIslandDestination("Select the island where you want to calculate influence"));
    }

    @Override
    public boolean canPlay() {
        return inputs.size() == 1;
    }

    @Override
    public byte getCost() {
        return 3;
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
    public List<Integer> getInputs() {
        return inputs;
    }

    @Override
    public String toString() {
        return "Herald";
    }
}