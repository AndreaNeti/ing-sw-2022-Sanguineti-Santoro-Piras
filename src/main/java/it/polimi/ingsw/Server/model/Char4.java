package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

public class Char4 implements CharacterCard {


    @Override
    public void play(ExpertGame game) throws UnexpectedValueException, NotAllowedException {
        //TODO check this idIsland
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland>2*game.getPlayerSize()+12||idIsland<2* game.getPlayerSize())
            throw new UnexpectedValueException();
        game.setProhibition((Island) game.getComponentById(idIsland));
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public byte getCharId() {
        return 4;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char4)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}