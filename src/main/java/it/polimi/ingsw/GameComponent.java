package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public abstract class GameComponent {
    private final byte[] students;
    private final int idGameComponent;

    public GameComponent(int id) {
        this.students = new byte[]{0, 0, 0, 0, 0};
        this.idGameComponent = id;
    }


    private void addStudent(byte color, byte number) {
        students[color] += number;
    }

    private void removeStudent(byte color, byte number) throws NotEnoughStudentsException {
        if(students[color] > number)
            students[color] -= number;
        else
            throw new NotEnoughStudentsException();
    }

    public void moveStudent(Color color, byte number, GameComponent component) {
        byte index = (byte) color.ordinal();
        try {
            this.removeStudent(index, number);
            component.addStudent(index, number);
        } catch (NotEnoughStudentsException ex) {
            System.out.println("Not enough students of selected color in component");
        }
    }

    public int getIdGameComponent() {
        return idGameComponent;
    }

    public byte[] getStudents() {
        return students.clone();
    }

}
