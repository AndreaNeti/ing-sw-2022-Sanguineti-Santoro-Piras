package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;
import it.polimi.ingsw.model.GameComponent;

import java.util.List;

public class Char6 extends GameComponent implements CharacterCard {

    @Override
    public void play(ExpertGame game) throws UnexpectedValueException, NotEnoughStudentsException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 and 2 and 4 are the colors of this character, input 1 and 3 and 5 are the colors of entrance hall students
        int characterColor, entranceHallColor;
        for (int i = 0; i < inputs.size(); i += 2) {
            characterColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            if (characterColor < 0 || characterColor >= Color.values().length || entranceHallColor < 0 || entranceHallColor > Color.values().length) {
                throw new UnexpectedValueException();
            }

            if (game.getCurrentPlayer().getEntranceHall().getStudents()[entranceHallColor] == 0 ||
                    getStudents()[characterColor] == 0) {
                throw new NotEnoughStudentsException();
            }

        }
        for (int i = 0; i < inputs.size(); i += 2) {
            characterColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);

            game.getCurrentPlayer().getEntranceHall().moveStudents(Color.values()[entranceHallColor], (byte) 1,this);
            this.moveStudents(Color.values()[characterColor], (byte) 1, game.getCurrentPlayer().getEntranceHall());
        }
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 6 || nInput == 4 || nInput == 2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char6)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}
