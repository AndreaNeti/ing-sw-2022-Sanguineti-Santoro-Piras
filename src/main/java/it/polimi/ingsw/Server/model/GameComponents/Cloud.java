package it.polimi.ingsw.Server.model.GameComponents;

/**
 * Cloud class represents the game's clouds, used to add students to the player's EntranceHalls at the end of the action phase.
 */
public class Cloud extends GameComponent {
    /**
     * Constructor Cloud creates a new instance of Cloud.
     *
     * @param numOfStudents of type int - maximum number of students placeable on the cloud.
     * @param idGameComponent of type byte - unique ID to assign to the cloud.
     */
    public Cloud(int numOfStudents, byte idGameComponent) {
        super(numOfStudents, idGameComponent);
    }
}
