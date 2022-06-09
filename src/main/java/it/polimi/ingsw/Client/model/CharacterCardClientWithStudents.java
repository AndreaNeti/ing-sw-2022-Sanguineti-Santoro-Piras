package it.polimi.ingsw.Client.model;

public abstract class CharacterCardClientWithStudents extends GameComponentClient implements CharacterCardClient {
    public CharacterCardClientWithStudents(int id) {
        super(id);
    }

    @Override
    public boolean containsStudents() {
        return true;
    }
}
