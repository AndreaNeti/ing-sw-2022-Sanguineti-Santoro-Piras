package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.io.Serializable;
import java.util.Arrays;

public abstract class GameComponent implements Serializable {
    private final byte[] students;
    private transient final int maxStudents;
    private final byte idGameComponent;
    private GameComponent(int maxStudents, byte studentsPerColor, byte idGameComponent) {
        this.students = new byte[Color.values().length];
        for (byte i = 0; i < students.length; i++) {
            students[i] = studentsPerColor;
        }
        this.maxStudents = maxStudents;
        this.idGameComponent=idGameComponent;
    }

    public GameComponent(int maxStudents,byte idGameComponent) {
        this(maxStudents, (byte) 0,idGameComponent);
    }

    public GameComponent(byte studentsPerColor,byte idGameComponent) {

        this(Color.values().length * studentsPerColor, studentsPerColor, idGameComponent);
    }

    public GameComponent(byte idGameComponent) {
        this(Integer.MAX_VALUE, (byte) 0,idGameComponent);
    }

    private void addStudents(Color color, byte number) {
        students[color.ordinal()] += number;
    }

    private void removeStudents(Color color, byte number) throws NotEnoughStudentsException {
        if (students[color.ordinal()] >= number)
            students[color.ordinal()] -= number;
        else
            throw new NotEnoughStudentsException();
    }

    public void moveStudents(Color color, byte number, GameComponent destination) throws GameException {
        if (number != 0) {
            if (destination.canAddStudents(color, number)) this.removeStudents(color, number);
            else throw new NotAllowedException("Can't add more students to this component");
            destination.addStudents(color, number);
        }
    }

    public void swapStudents(Color toGive, Color toGet, GameComponent other) throws GameException {
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

    //the exception is for the subclasses like bag,entranceHall and lunchHall that can't use this function
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

    public boolean canAddStudents(Color c, byte number) {
        return howManyStudents() + number <= maxStudents;
    }

    // returns the number of students with color c
    public byte howManyStudents(Color c) {
        return students[c.ordinal()];
    }

    // returns the global number of students
    public byte howManyStudents() {
        byte sum = 0;
        for (Color c : Color.values()) {
            sum += students[c.ordinal()];
        }
        return sum;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameComponent that = (GameComponent) o;
        return maxStudents == that.maxStudents && Arrays.equals(students, that.students);
    }

    public byte getId() {
        return idGameComponent;
    }
}
