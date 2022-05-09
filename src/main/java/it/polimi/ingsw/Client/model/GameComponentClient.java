package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.Color;
import it.polimi.ingsw.Server.model.GameComponent;

import java.util.Arrays;


public class GameComponentClient {
    private byte[] students;
    private int id;



    public byte[] getStudents() {
        byte[] clonedStudents=new byte[5];
        System.arraycopy(students, 0, clonedStudents, 0, 5);
        return clonedStudents;
    }

    @Override
    public String toString() {
        return "students= " + Arrays.toString(students);
    }

    public int getId() {
        return id;
    }
    public void modifyGameComponent(GameComponent gameComponent){
        byte[] students= new byte[5];
        for (Color c: Color.values()) {
            students[c.ordinal()]= gameComponent.howManyStudents(c);
        }
        this.id= gameComponent.getId();
        this.students=students;
    }
}
