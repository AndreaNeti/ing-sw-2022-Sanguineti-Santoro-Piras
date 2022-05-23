package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.ArrayList;
import java.util.List;

public class Char10Client extends CharacterCardClientWithStudents {

    private final List<Integer> inputs;
    public Char10Client(){
        super(-12);
        inputs =new ArrayList<>();
    }

    @Override
    public String getDescription() {
        return "Take 1 Student from this card and place it in your Dining Room. Then, draw a new Student from the Bag and place it on this card.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws PhaseChangedException {
        System.out.println("Select the color of the student from this card "+super.toString());
        inputs.add(view.getColorInput(false));
    }

    @Override
    public boolean canPlay() {
        return inputs.size()==1;
    }

    @Override
    public byte getCost() {
        return 2;
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
    public int getCharId() {
        return 10;
    }

    @Override
    public String toString() {
        return "Spoiled princess";
    }
}

