package it.polimi.ingsw.client.model.CharacterClientLogic;

import it.polimi.ingsw.client.view.Cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.Gui.ViewGUI;
import it.polimi.ingsw.server.model.CharacterServerLogic.Char5;

import java.util.List;

/**
 * Char5Client class represents the character card on the client side and corresponds to the server class {@link Char5}.
 */
public class Char5Client implements CharacterClientLogicInterface {

    public Char5Client() {

    }

    @Override
    public String getDescription() {
        return "When resolving a Conquering on an Island, Towers do not count towards influence.";
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
        return "Centaur";
    }

}
