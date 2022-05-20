package it.polimi.ingsw.Client.model.CharacterCard;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.CharacterCard;
import it.polimi.ingsw.Server.model.CharacterCardGame;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public class Char11 {
    @Override
    public String toString() {
        return "Scegli un colore di Studente ogni giocat (incluso te) deve rimettere nel sacchetto 3 Student quel colore presenti nella sua Sala. Chi avesse meno di 3 Studenti di quel colore, rimetterà tutti quelli che ha";
    }
}

