package it.polimi.ingsw.Client.model;

/**
 * CharacterCardClientWithStudents abstract class represents the character cards in "Eriantys" that can contain students
 * on themselves. <br>
 * This class is extended by all character card classes that contain students ({@link Char0Client}, {@link Char6Client}, {@link Char10Client}).
 */
public abstract class CharacterCardClientWithStudents extends GameComponentClient implements CharacterCardClient {

    /**
     * Constructor CharacterCardClientWithStudents creates a new instance of CharacterCardClientWithStudents.
     *
     * @param id of type {@code int} - unique ID to assign to the character card.
     */
    public CharacterCardClientWithStudents(int id) {
        super(id);
    }

    /**
     * Method containsStudents checks if the character card is one of the 3 ({@link Char0Client}, {@link Char6Client}, {@link Char10Client})
     * that can contain students.
     *
     * @return {@code boolean} - true.
     */
    @Override
    public boolean containsStudents() {
        return true;
    }
}
