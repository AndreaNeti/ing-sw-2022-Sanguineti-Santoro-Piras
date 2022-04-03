package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

import java.util.List;

public class Char0 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws NotEnoughStudentsException, UnexpectedValueException, EndGameException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 is the color chosen, input 1 is the island id chosen
        int color = inputs.get(0), idIsland = inputs.get(1);
        if (color < 0 || color >= Color.values().length || idIsland < 0 || idIsland >= game.getIslands().size())
            throw new UnexpectedValueException();
        moveStudents(Color.values()[color], (byte) 1, game.getIslands().get(idIsland));
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
