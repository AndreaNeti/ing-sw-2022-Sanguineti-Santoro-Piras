package it.polimi.ingsw.Server.model;

public class Char3 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) {
        game.setExtraSteps();
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 3;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}