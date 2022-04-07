package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

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
            if (canAddStudents()) this.removeStudents(color, number);
            else throw new NotAllowedException("Can't add more students to this component");
            destination.addStudents(color, number);
        }
    }

    public void moveAll(GameComponent destination) {
        if(destination.howManyStudents() != 0 ) {
            for (Color c : Color.values()) {
                try {
                    moveStudents(c, howManyStudents(c), destination);
                } catch (GameException e) {
                    // should not call this
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Bravo, ti sei impegnato per fare questa cacatoa. Trovati qualcos'altro da fare");
        }
    }

    private boolean canAddStudents() {
        return howManyStudents() < maxStudents;
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

    public int getMaxStudents() {
        return maxStudents;
    }
}
