package it.polimi.ingsw.server.model.gameComponents;

import it.polimi.ingsw.utils.Color;

/**
 * Cloud class represents the game's clouds, used to add students to the player's EntranceHalls at the end of the action phase.
 */
public class Cloud extends GameComponent {
    /**
     * Constructor Cloud creates a new instance of Cloud.
     *
     * @param numOfStudents of type {@code int} - maximum number of students placeable on the cloud.
     * @param idGameComponent of type {@code byte} - unique ID to assign to the cloud.
     */
    public Cloud(int numOfStudents, byte idGameComponent) {
        super(numOfStudents, idGameComponent);
    }

    /**
     * Method canAddStudents checks if the cloud can receive enough students.
     *
     * @param color  of type {@link Color}, <b>IGNORED</b> - color of the students.
     * @param number of type {@code byte} - number of students.
     * @return {@code boolean} - true if the cloud can receive the specified number of students, false else.
     */
    @Override
    protected boolean canAddStudents(Color color, byte number) {
        return howManyStudents() + number <= getMaxStudents();
    }
}
