package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public abstract class GameComponent {
    private final byte[] students;


    public GameComponent() {
        this.students = new byte[]{0, 0, 0, 0, 0};
    }

    public void addStudents(byte color, byte number) {
        students[color] += number;
    }

    private void removeStudents(byte color, byte number) throws NotEnoughStudentsException {
        if(students[color] > number)
            students[color] -= number;
        else
            throw new NotEnoughStudentsException();
    }

    public void moveStudents(Color color, byte number, GameComponent component) throws NotEnoughStudentsException{
        byte index = (byte) color.ordinal();
        this.removeStudents(index, number);
        component.addStudents(index, number);
    }


    public byte[] getStudents() {
        return students.clone();
    }

}
