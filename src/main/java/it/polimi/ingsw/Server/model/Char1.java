package it.polimi.ingsw.Server.model;

public class Char1 implements CharacterCard {

    @Override
    public void play(CharacterCardGame game) {
        //should set the boolean to true before the calculation
        game.setEqualProfessorCalculation();
    }

    @Override
    public byte getCost() {
        return 2;
    }

    @Override
    public byte getCharId() {
        return 1;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }

}