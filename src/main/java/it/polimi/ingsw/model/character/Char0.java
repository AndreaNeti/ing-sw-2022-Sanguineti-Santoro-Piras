package it.polimi.ingsw.model.character;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

import java.util.List;

public class Char0 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) {
        List<Integer> inputs = game.getCharacterInputs();
        moveStudents(Color.values()[inputs.get(0)], 1, game.getIsland(inputs.get(1)));
        game.drawStudents(this, (byte) 1);
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char0)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
