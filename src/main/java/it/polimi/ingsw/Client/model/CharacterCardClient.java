package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.PhaseChangedException;

import java.util.List;

public interface CharacterCardClient {
    String getDescription();

    //return the next input needed for the character card, null if it can be played
    void setNextInput(ViewForCharacterCli view) throws PhaseChangedException;

    boolean canPlay();

    boolean isFull();
    void resetInput();
    List<Integer> getInputs();

}
