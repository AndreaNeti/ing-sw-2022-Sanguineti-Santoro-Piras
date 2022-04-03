package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public abstract class GameComponent {
    private final byte[] students;


    public GameComponent() {
        this.students = new byte[]{0, 0, 0, 0, 0};
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


    public byte[] getStudents() {
        return students.clone();
    }

}
