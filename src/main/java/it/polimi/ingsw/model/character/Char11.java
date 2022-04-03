package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.Player;

public class Char11 implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws UnexpectedValueException, NotEnoughStudentsException {
        int color = game.getCharacterInputs().get(0);
        if (color < 0 || color >= Color.values().length) throw new UnexpectedValueException();
        for (Player p : game.getPlayers()) {
            p.getLunchHall().moveStudents(Color.values()[color], (byte) 3, game.getBag());
        }
    }

    @Override
    public byte getCost() {
        return 3;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char11)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

