package it.polimi.ingsw.network;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;

public class Adapter {
    public static GameComponentClient transform (GameComponent gameComponent){
        byte[] students= new byte[5];
        for (Color c: Color.values()) {
            students[c.ordinal()]= gameComponent.howManyStudents(c);
        }
        return new GameComponentClient(students);
    }
}
