package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

public interface CharacterCardClient {
    String getDescription();

    //return the next input needed for the character card, null if it can be played
    void setNextInput(ViewForCharacterCli view) throws SkipCommandException;

    //void setHandler(ViewGUI viewGUI);

    boolean canPlay();

    byte getCost();

    boolean isFull();

    void resetInput();

    List<Integer> getInputs();

    int getCharId();

    void setUsed();

    boolean containsStudents();
}
