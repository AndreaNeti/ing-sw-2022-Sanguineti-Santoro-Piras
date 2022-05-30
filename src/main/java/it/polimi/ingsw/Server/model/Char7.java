package it.polimi.ingsw.Server.model;

public class Char7 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) {
        game.setExtraInfluence();
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public byte getCharId() {
        return 7;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}
