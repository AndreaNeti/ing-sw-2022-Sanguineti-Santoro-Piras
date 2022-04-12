package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

import java.util.Arrays;

public abstract class GameComponent {
    private final byte[] students;
    private final int maxStudents;

    private GameComponent(int maxStudents, byte studentsPerColor) {
        this.students = new byte[Color.values().length];
        for (byte i = 0; i < students.length; i++) {
            students[i] = studentsPerColor;
        }
        this.maxStudents = maxStudents;
    }

    public GameComponent(int maxStudents) {
        this(maxStudents, (byte) 0);
    }

    public GameComponent(byte studentsPerColor) {
        this(Color.values().length * studentsPerColor, studentsPerColor);
    }

    public GameComponent() {
        this(Integer.MAX_VALUE, (byte) 0);
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
    //the exception is for the subclasses like bag,entranceHall and lunchHall that can't use this function
    public void moveAll(GameComponent destination) throws NotAllowedException {
        if (howManyStudents() != 0) {
            for (Color c : Color.values()) {
                try {
                    moveStudents(c, howManyStudents(c), destination);
                } catch (GameException ignored) {
                    //TODO technically we could for example move all students from
                    //a cloud to an island which is an error
                    //TODO FIX THIS BUG PLS
                }
            }
        } else {
            System.out.println("Bravo, ti sei impegnato per fare questa cacata. Trovati qualcos'altro da fare");
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
}
