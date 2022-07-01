package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.client.View.Gui.ViewGUI;
import it.polimi.ingsw.server.model.CharacterServerLogic.Char1;

import java.util.List;

/**
 * Char1Client class represents the character card on the client side and corresponds to the server class {@link Char1}.
 */
public class Char1Client implements CharacterClientLogicInterface {

    public Char1Client() {
    }

    @Override
    public String getDescription() {
        return "During this turn, you take control of any number of Professors even if you have the same number of Students as the player who currently controls them.";
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
        return "Farmer";
    }

}