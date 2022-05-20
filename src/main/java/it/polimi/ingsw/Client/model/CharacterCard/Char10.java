package it.polimi.ingsw.Client.model.CharacterCard;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.CharacterCard;
import it.polimi.ingsw.Server.model.CharacterCardGame;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

public class Char10 {
    @Override
    public String toString() {
        return "Prendi 1 Studente da questa carta e piazzalo nella tua Sala. Poi pesca un nuovo Studente dal sacchetto e posizionalo su questa carta.";
    }
}

