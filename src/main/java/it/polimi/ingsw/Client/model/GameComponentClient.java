package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.GameComponent;

import java.util.Arrays;


public class GameComponentClient {
    private final byte[]  students;
    private int id;


    public GameComponentClient(int id){
        students=new byte[5];
        this.id=id;
    }
    public byte[] getStudents() {
        byte[] clonedStudents=new byte[5];
        System.arraycopy(students, 0, clonedStudents, 0, 5);
        return clonedStudents;
    }

    @Override
    public String toString() {
        return "Component:"+ id+ "; students= " + Arrays.toString(students);
    }

    public int getId() {
        return id;
    }
    public void modifyGameComponent(GameComponent gameComponent){
        for (Color c: Color.values()) {
            this.students[c.ordinal()]= gameComponent.howManyStudents(c);
        }
        this.id= gameComponent.getId();
    }
}
