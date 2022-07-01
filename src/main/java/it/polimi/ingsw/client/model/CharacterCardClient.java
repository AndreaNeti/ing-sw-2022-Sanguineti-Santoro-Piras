package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.model.CharacterClientLogic.CharacterClientLogicInterface;
import it.polimi.ingsw.client.view.cli.ViewForCharacterCli;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.server.model.characterServerLogic.CharacterServerLogicInterface;
import it.polimi.ingsw.utils.CharacterCardDataInterface;

import java.util.List;

/**
 * CharacterCard class represent a generic character card in "Eriantys", client side. <br>
 * This class contains both the data and the logic of a specific character card, of which it contains the instance. <br>
 * This class therefore contains all the methods required to use the character card in the client's game.
 */
public final class CharacterCardClient implements CharacterCardDataInterface, CharacterClientLogicInterface {
    private CharacterCardDataInterface characterData;
    private final CharacterClientLogicInterface characterCard;

    /**
     * Constructor CharacterCardClient creates a new instance of CharacterCardClient.
     *
     * @param characterCard of type {@link CharacterServerLogicInterface} - instance of the specific character card logic.
     * @param data of type {@link CharacterCardDataInterface} - info about the character card data.
     */
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
