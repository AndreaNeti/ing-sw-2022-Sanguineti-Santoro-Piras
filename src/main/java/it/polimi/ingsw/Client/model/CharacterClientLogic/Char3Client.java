package it.polimi.ingsw.Client.model.CharacterClientLogic;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Server.model.CharacterServerLogic.Char3;
import it.polimi.ingsw.Server.model.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * Char3Client class represents the character card on the client side and corresponds to the server class {@link Char3}.
 */
public class Char3Client implements CharacterClientLogicInterface {
    public Char3Client() {
    }

    @Override
    public String getDescription() {
        return "You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
    }

    @Override
    public boolean canPlay() {
        return true;
    }

    @Override
    public boolean isFull() {
        return true;
    }

    @Override
    public void resetInput() {
    }

    @Override
    public List<Integer> getInputs() {
        return null;
    }

    @Override
    public String toString() {
        return "Magic postman";
    }

}