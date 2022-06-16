package it.polimi.ingsw.Server.model.GameComponents;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.serverExceptions.NotEnoughStudentsException;

import java.io.Serializable;

/**
 * GameComponent class represents a game piece where students can be placed in "Eriantys".
 * All components that can contain students extend this abstract class.
 */
public abstract class GameComponent implements Serializable {
    private final byte[] students;
    private transient final int maxStudents;
    private final byte idGameComponent;

    /**
     * Private constructor GameComponent used by public constructor to create a new instance of GameComponent.
     *
     * @param maxStudents of type {@code int} - maximum number of students placeable on this component.
     * @param studentsPerColor of type {@code byte} - number of students per color to add when the component is created.
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    private GameComponent(int maxStudents, byte studentsPerColor, byte idGameComponent) {
        if (maxStudents < 0 || studentsPerColor < 0)
            throw new IllegalArgumentException("Cannot create a gameComponent that contains negative students");
        this.students = new byte[Color.values().length];
        for (byte i = 0; i < students.length; i++) {
            students[i] = studentsPerColor;
        }
        this.maxStudents = maxStudents;
        this.idGameComponent = idGameComponent;
    }

    /**
     * Constructor GameComponent creates a new instance of GameComponent with no students on it.
     *
     * @param maxStudents of type {@code int} - maximum number of students placeable on this component.
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    public GameComponent(int maxStudents, byte idGameComponent) {
        this(maxStudents, (byte) 0, idGameComponent);
    }

    /**
     * Constructor GameComponent creates a new instance of GameComponent with the maximum amount of students
     * placeable already on the component.
     *
     * @param studentsPerColor of type {@code byte} - number of students per color to add when the component is created.
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    public GameComponent(byte studentsPerColor, byte idGameComponent) {

        this(Color.values().length * studentsPerColor, studentsPerColor, idGameComponent);
    }

    /**
     * Constructor GameComponent creates a new instance of GameComponent with no students on it and no upper limit on the
     * number of students placeable.
     *
     * @param idGameComponent of type {@code byte} - unique ID to assign to the component.
     */
    public GameComponent(byte idGameComponent) {
        this(Integer.MAX_VALUE, (byte) 0, idGameComponent);
    }

    /**
     * Method addStudents adds students of a selected color to the component.
     *
     * @param color of type {@link Color} - color of the students to add.
     * @param number of type {@code byte} - number of students to add.
     * @throws NotEnoughCoinsException if the component is a LunchHall and there are no more coins in the game
     * to give to the player after placing 3, 6 or 9 students of the same color.
     */
    protected void addStudents(Color color, byte number) throws NotEnoughCoinsException {
        if (color == null) throw new IllegalArgumentException("Null color");
        if (number < 0) throw new IllegalArgumentException("Cannot add negative students");
        students[color.ordinal()] += number;
    }

    /**
     * Method removeStudents removes students of a selected color from the component.
     *
     * @param color of type {@link Color} - color of the students to add.
     * @param number of type {@code byte} - number of students to add.
     * @throws NotEnoughStudentsException if there are not enough students of the selected color to remove from the component.
     */
    protected void removeStudents(Color color, byte number) throws NotEnoughStudentsException {
        if (color == null) throw new IllegalArgumentException("Null color");
        if (number < 0) throw new IllegalArgumentException("Cannot add negative students");
        if (students[color.ordinal()] >= number)
            students[color.ordinal()] -= number;
        else
            throw new NotEnoughStudentsException();
    }

    /**
     * Method moveStudents moves students of a selected color from the component to another one.
     *
     * @param color of type {@link Color} - color of the students to add.
     * @param number of type {@code byte} - number of students to add.
     * @param destination of type {@link GameComponent} - instance of the target GameComponent.
     * @throws GameException if the target GameComponent cannot receive all the students of the selected color.
     */
    public void moveStudents(Color color, byte number, GameComponent destination) throws GameException {
        if (color == null) throw new IllegalArgumentException("Null color");
        if (number < 0) throw new IllegalArgumentException("Cannot add negative students");

        if (destination.canAddStudents(color, number)) this.removeStudents(color, number);
        else throw new NotAllowedException("Can't add more students to this component");
        destination.addStudents(color, number);

    }

    /**
     * Method swapStudents swaps two students between the component and a target one.
     *
     * @param toGive of type {@link Color} - color of the students to give.
     * @param toGet of type {@link Color} - color of the students to receive.
     * @param other of type {@link GameComponent} - instance of the other GameComponent.
     * @throws GameException if the swap will fail (one of the two components doesn't have the requested student color to give
     * or cannot receive any more students of the selected color).
     */
    public void swapStudents(Color toGive, Color toGet, GameComponent other) throws GameException {
        if (toGive == null || toGet == null || other == null) throw new IllegalArgumentException("Null argument");
        // swapping between same gameComponent or same color
        if (this == other || toGive == toGet) return;
        removeStudents(toGive, (byte) 1);
        try {
            other.moveStudents(toGet, (byte) 1, this);
        } catch (GameException e) {
            // can't do move, revert removeStudents
            addStudents(toGive, (byte) 1);
            throw e;
        }
        // have to check after the move
        if (other.canAddStudents(toGive, (byte) 1))
            other.addStudents(toGive, (byte) 1);
        else {
            // re-add the removed student, cannot swap
            addStudents(toGive, (byte) 1);
            throw new NotAllowedException("Cannot swap these students");
        }
    }

    /**
     * Method moveAll moves all students present on the component to a target component.
     *
     * @param destination of type {@link GameComponent} - the instance of the target component.
     * @throws NotAllowedException if the function is called on a Bag, EntranceHall or LunchHall.
     */
    public void moveAll(GameComponent destination) throws NotAllowedException {
        if (howManyStudents() > destination.getMaxStudents() - destination.howManyStudents())
            throw new NotAllowedException("Exceeded destination max student limit");
        if (howManyStudents() != 0) {
            for (Color c : Color.values()) {
                try {
                    moveStudents(c, howManyStudents(c), destination);
                } catch (GameException ignored) {
                }
            }
        } else {
            System.out.println("Bravo, ti sei impegnato per fare questa mossa. Trovati qualcos'altro da fare");
        }
    }

    /**
     * Method canAddStudents checks if the component can receive enough students of a selected color.
     *
     * @param color of type {@link Color} - color of the students.
     * @param number of type {@code byte} - number of students.
     * @return {@code boolean} - true if the component can receive the specified number of students of the selected color, false else.
     */
    protected boolean canAddStudents(Color color, byte number) {
        return howManyStudents(color) + number <= maxStudents;
    }


    /**
     * Method howManyStudents returns the number of students of a specific color present in the component.
     *
     * @param color of type {@link Color} - color of which we want to check the number of students.
     * @return {@code byte} - number of students of the selected color in the component.
     */
    public byte howManyStudents(Color color) {
        if (color == null) throw new IllegalArgumentException("Null color");
        return students[color.ordinal()];
    }

    /**
     * Method howManyStudents returns the total number of students present in the component.
     *
     * @return {@code byte} - total number of students in the component.
     */
    public byte howManyStudents() {
        byte sum = 0;
        for (Color c : Color.values()) {
            sum += students[c.ordinal()];
        }
        return sum;
    }


    /**
     * Method getMaxStudents returns the maximum amount of students placeable on the component.
     *
     * @return {@code int} - number of max students.
     */
    public int getMaxStudents() {
        return maxStudents;
    }

    /**
     * Method equals is used to compare two GameComponents, based on their unique ID.
     *
     * @param o of type {@code Object} - instance of the other Object.
     * @return {@code boolean} - true if the other object is a GameComponent and has the same ID of the component.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameComponent that = (GameComponent) o;
        return idGameComponent == that.idGameComponent;
    }

    /**
     * Method getId returns the unique ID of the component.
     *
     * @return {@code byte} - ID of the component.
     */
    public byte getId() {
        return idGameComponent;
    }
}
