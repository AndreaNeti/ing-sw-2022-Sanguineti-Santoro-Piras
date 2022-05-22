package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.GameComponent;

public abstract class CharacterCardClientWithStudents extends GameComponentClient implements CharacterCardClient  {
    public CharacterCardClientWithStudents(int id) {
        super(id);
    }
}
