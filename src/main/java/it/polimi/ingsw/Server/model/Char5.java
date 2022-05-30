package it.polimi.ingsw.Server.model;

public class Char5 implements CharacterCard {
    @Override
    public void play(CharacterCardGame game) {
        game.removeTowerInfluence();
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public byte getCharId() {
        return 5;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}
