package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

public class Char10 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws NotEnoughStudentsException, EndGameException {
        moveStudents(Color.values()[game.getCharacterInputs().get(0)], (byte) 1, game.getCurrentPlayer().getLunchHall());
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
        if (!(o instanceof Char10)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

