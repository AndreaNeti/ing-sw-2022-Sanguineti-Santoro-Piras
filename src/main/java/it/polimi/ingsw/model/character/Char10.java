package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

public class Char10 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) {
        moveStudents(Color.values()[game.getCharacterInputs().get(0)], 1, game.getCurrentPlayer().getLunchHall());
        game.drawStudents(this, (byte) 1);
    }

    @Override
    public byte getCost() {
        return 2;
    }


    @Override
    public int getId() {
        return 10;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

