package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Util.Color;

import java.util.Arrays;
import java.util.Objects;

/**
 * GameComponentClient abstract class represents the game components on the client side and corresponds
 * to the server class {@link GameComponent}. <br>
 * All client components that can contain students extend this abstract class.
 */
public class GameComponentClient {
    private final byte[] students;
    private int id;

    /**
     * Constructor GameComponentClient creates a new instance of GameComponentClient.
     *
     * @param id of type {@code int} - unique ID to assign to the game component.
     */
    public GameComponentClient(int id) {
        students = new byte[5];
        this.id = id;
    }

    /**
     * Method getStudents returns the number of student for each color in the component.
     *
     * @return {@code byte[]} - copy of the array of students in the component.
     */
    public byte[] getStudents() {
        return Arrays.copyOf(students, students.length);
    }

    /**
     * Method toString returns the name of the component and its students.
     *
     * @return {@code String} - "(game component name) | Students: (RED = X, BLUE = Y, ...)".
     */
    @Override
    public String toString() {
        return getNameOfComponent() + " | Students: " + studentsToString();
    }

    /**
     * Method studentToString returns the students of the component.
     *
     * @return {@code String} - "RED = X, BlUE = Y, YELLOW = Z, GREEN = W, PINK = U".
     */
    private String studentsToString() {
        StringBuilder ret = new StringBuilder();
        for (Color c : Color.values())
            ret.append(c).append(" = ").append(students[c.ordinal()]).append(", ");
        return ret.toString();
    }

    /**
     * Method getId returns the unique ID of the game component.
     *
     * @return {@code int} - unique ID of the game component.
     */
    public int getId() {
        return id;
    }

    /**
     * Method howManyStudents returns the total number of students present in the component.
     *
     * @return {@code byte} - total number of students in the component.
     */
    // returns the global number of students
    public byte howManyStudents() {
        byte sum = 0;
        for (Color c : Color.values()) {
            sum += students[c.ordinal()];
        }
        return sum;
    }

    /**
     * Method howManyStudents returns the number of students of a specific color present in the component.
     *
     * @param color of type {@link Color} - color of which we want to check the number of students.
     * @return {@code byte} - number of students of the selected color in the component.
     */
    public byte howManyStudents(Color color) {
        return students[color.ordinal()];
    }

    /**
     * Method modifyGameComponent updates the students and the ID of the component to match the ones of the game component provided.
     *
     * @param gameComponent of type {@link GameComponent} - instance of the game component.
     */
    protected void modifyGameComponent(GameComponent gameComponent) {
        for (Color c : Color.values()) {
            this.students[c.ordinal()] = gameComponent.howManyStudents(c);
        }
        this.id = gameComponent.getId();
    }

    /**
     * Method getNameOfComponent returns the name of the component based on its unique ID. <br>
     * ID < 10 -> Character chard. <br>
     * ID < 0 -> Cloud. <br>
     * ID is even -> EntranceHall. <br>
     * ID is odd -> LunchHall. <br>
     *
     * @return {@code String} - "Character card|Cloud|EntranceHall|LunchHall".
     */
    public String getNameOfComponent() {
        if (id < -10) {
            return "Character card";
        } else if (id < 0)
            return "Cloud";
        else {
            if (id % 2 == 0)
                return "EntranceHall";
            else
                return "LunchHall";
        }
    }

    /**
     * Method equals is used to compare two GameComponentClients, based on their unique ID.
     *
     * @param o of type {@code Object} - instance of the other Object.
     * @return {@code boolean} - true if the other object is a GameComponentClient and has the same ID of the component.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameComponentClient that)) return false;
        return getId() == that.getId();
    }

    /**
     * Method hasCode returns the hash code obtained from the component's unique ID.
     *
     * @return {@code int} - hash code of the component's ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
