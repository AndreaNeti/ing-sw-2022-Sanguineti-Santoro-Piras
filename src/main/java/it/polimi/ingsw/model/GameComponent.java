package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public abstract class GameComponent {
    private final byte[] students;


    public GameComponent() {
        this.students = new byte[Color.values().length];
        for (byte s : students) {
            s = 0;
        }
    }

    public void addStudents(Color color, byte number) {
        students[color.ordinal()] += number;
    }

    private void removeStudents(Color color, byte number) throws NotEnoughStudentsException {
        if (students[color.ordinal()] >= number)
            students[color.ordinal()] -= number;
        else
            throw new NotEnoughStudentsException();
    }

    public void moveStudents(Color color, byte number, GameComponent destination) throws NotEnoughStudentsException {
        this.removeStudents(color, number);
        destination.addStudents(color, number);
    }

    public void moveAll(GameComponent destination) {
        for (Color c : Color.values()) {
            try {
                moveStudents(c, howManyStudents(c), destination);
            } catch (NotEnoughStudentsException e) {
                // should not call this
                e.printStackTrace();
            }
        }
    }

    public byte howManyStudents(Color c) {
        return students[c.ordinal()];
    }

    public int howManyStudents() {
        int sum = 0;
        for (Color c : Color.values()) {
            sum += students[c.ordinal()];
        }
        return sum;
    }
}
