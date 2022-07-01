package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.Cli.ViewForCharacterCli;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterClientLogic.CharacterClientLogicInterface;
import it.polimi.ingsw.Util.CharacterCardDataInterface;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;

import java.util.List;

/**
 * CharacterCardClient interface represents the character cards in "Eriantys", available in the expert game mode. <br>
 * This interface is used client side to access the info of the character cards sent by the server.
 * There are a total of 12 client classes that implement this interface.
 */
public final class CharacterCardClient implements CharacterCardDataInterface, CharacterClientLogicInterface {
    private CharacterCardDataInterface characterData;
    private final CharacterClientLogicInterface characterCard;

    public CharacterCardClient(CharacterClientLogicInterface characterCard, CharacterCardDataInterface data) {
        this.characterCard = characterCard;
        this.characterData = data;
    }

    @Override
    public String getDescription() {
        return characterCard.getDescription();
    }

    @Override
    public void setNextInput(ViewForCharacterCli view) throws SkipCommandException {
        characterCard.setNextInput(view);
    }

    @Override
    public void setHandler(ViewGUI viewGUI) {
        characterCard.setHandler(viewGUI);
    }

    @Override
    public boolean canPlay() {
        return characterCard.canPlay();
    }

    @Override
    public boolean isFull() {
        return characterCard.isFull();
    }

    @Override
    public void resetInput() {
        characterCard.resetInput();

    }

    @Override
    public List<Integer> getInputs() {
        return characterCard.getInputs();
    }

    @Override
    public byte getCost() {
        return characterData.getCost();
    }

    @Override
    public byte getCharId() {
        return characterData.getCharId();
    }

    @Override
    public boolean isUsed() {
        return characterData.isUsed();
    }

    @Override
    public boolean hasStudents() {
        return characterData.hasStudents();
    }

    @Override
    public void setUsed() {
        characterData.setUsed();
    }

    @Override
    public String toString() {
        return characterCard.toString();
    }

    public void setData(CharacterCardDataInterface data) {
        this.characterData = data;
    }

    @Override
    public boolean hasProhibitions() {
        return characterData.hasProhibitions();
    }
}
