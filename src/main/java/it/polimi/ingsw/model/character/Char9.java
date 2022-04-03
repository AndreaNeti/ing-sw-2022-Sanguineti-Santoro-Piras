package it.polimi.ingsw.model.character;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.ExpertGame;

import java.util.List;

public class Char9 implements CharacterCard {


    // switch the students between the lunch hall and the entrance hall one pair at a time
    @Override
    public void play(ExpertGame game) throws NotEnoughStudentsException, UnexpectedValueException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 and 2 are the colors of lunch hall students, input 1 and 3 are the colors of entrance hall students
        int lunchHallColor, entranceHallColor;
        for (int i = 0; i < inputs.size(); i += 2) {
            lunchHallColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);
            if (lunchHallColor < 0 || lunchHallColor >= Color.values().length || entranceHallColor < 0 || entranceHallColor > Color.values().length) {
                throw new UnexpectedValueException();
            }

            if (game.getCurrentPlayer().getEntranceHall().getStudents()[entranceHallColor] == 0 ||
                    game.getCurrentPlayer().getLunchHall().getStudents()[lunchHallColor] == 0) {
                throw new NotEnoughStudentsException();
            }
        }

        for (int i = 0; i < inputs.size(); i += 2) {
            lunchHallColor = inputs.get(i);
            entranceHallColor = inputs.get(i + 1);

            game.getCurrentPlayer().getLunchHall().moveStudents(Color.values()[lunchHallColor], (byte) 1, game.getCurrentPlayer().getEntranceHall());
            game.getCurrentPlayer().getEntranceHall().moveStudents(Color.values()[entranceHallColor], (byte) 1, game.getCurrentPlayer().getEntranceHall());
        }

    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public boolean canPlay(int nInput) {
        return (nInput == 2 || nInput == 4);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Char9)) return false;
        CharacterCard c = (CharacterCard) o;
        return getId() == c.getId();
    }
}

