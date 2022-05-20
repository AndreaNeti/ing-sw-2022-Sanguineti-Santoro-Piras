package it.polimi.ingsw.Client.model.CharacterCard;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.CharacterCard;
import it.polimi.ingsw.Server.model.CharacterCardGame;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.List;

public class Char6 {
    @Override
    public String toString() {
        return "Puoi prendere fino a 3 Studenti da questa carta e scambiarli con altrettanti Studenti presenti nel tuo Ingresso";
    }
}
