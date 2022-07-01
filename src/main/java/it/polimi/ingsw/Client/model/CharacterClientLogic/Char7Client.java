package it.polimi.ingsw.Client.model.CharacterClientLogic;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Server.model.CharacterServerLogic.Char7;

import java.util.List;

/**
 * Char7Client class represents the character card on the client side and corresponds to the server class {@link Char7}.
 */
public class Char7Client implements CharacterClientLogicInterface {

    public Char7Client() {

    }

    @Override
    public String getDescription() {
        return "During the influence calculation this turn, you count as having 2 more influence.";
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) {

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
        return "Knight";
    }

}
