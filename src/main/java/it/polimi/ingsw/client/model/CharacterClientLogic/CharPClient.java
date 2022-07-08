package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.view.cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.server.model.characterServerLogic.CharP;

import java.util.List;

/**
 * CharPClient class represents the "Paolino" character card on the client side and corresponds to the server class {@link CharP}.
 */
public class CharPClient implements CharacterClientLogicInterface {

    /**
     * Constructor CharPClient creates a new instance of Char11Client.
     */
    public CharPClient() {

    }

    @Override
    public String getDescription() {
        return "Instantly win the game, all other players have to keep living their lives knowing they got destroyed by Paolino's mighty will.";
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
    public String toString() {
        return "Paolino";
    }

    @Override
    public List<Integer> getInputs() {
        return null;
    }

}
