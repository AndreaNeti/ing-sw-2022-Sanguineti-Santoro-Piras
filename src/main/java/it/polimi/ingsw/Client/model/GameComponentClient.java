package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;


public class GameComponentClient {
    private final byte[] students;

    public GameComponentClient(GameComponent gameComponent){
        byte[] students= new byte[5];
        for (Color c: Color.values()) {
            students[c.ordinal()]= gameComponent.howManyStudents(c);
        }
        this.students=students;
    }

    public byte[] getStudents() {
        byte[] clonedStudents=new byte[5];
        System.arraycopy(students, 0, clonedStudents, 0, 5);
        return clonedStudents;
    }
}
