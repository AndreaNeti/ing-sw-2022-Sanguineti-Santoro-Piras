package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.GameComponent;

import java.util.Arrays;


public class GameComponentClient {
    private final byte[] students;
    private int id;


    public GameComponentClient(int id) {
        students = new byte[5];
        this.id = id;
    }

    public byte[] getStudents() {
        return Arrays.copyOf(students, students.length);
    }

    @Override
    public String toString() {
        return getNameOfComponent() + " | Students: " + studentsToString();
    }

    private String studentsToString() {
        StringBuilder ret = new StringBuilder();
        for (Color c : Color.values())
            ret.append(c).append("=").append(students[c.ordinal()]).append(" ");
        return ret.toString();
    }

    public int getId() {
        return id;
    }

    // returns the global number of students
    public byte howManyStudents() {
        byte sum = 0;
        for (Color c : Color.values()) {
            sum += students[c.ordinal()];
        }
        return sum;
    }

    public byte howManyStudents(Color c) {
        return students[c.ordinal()];
    }

    protected void modifyGameComponent(GameComponent gameComponent) {
        for (Color c : Color.values()) {
            this.students[c.ordinal()] = gameComponent.howManyStudents(c);
        }
        this.id = gameComponent.getId();
    }

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
}
