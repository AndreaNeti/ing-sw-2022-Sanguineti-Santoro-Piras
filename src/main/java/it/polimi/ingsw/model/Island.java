package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public class Island extends GameComponent{
    private Player player;
    //prohibition is the representation of the NO Entry Tiles which  avoids the calcutaion of the influence on a island
    private boolean prohibition;

    //it's the number of the island merged in this island
    private byte number;

    public Island() {
        super();
        player=null;
        prohibition=false;
        number=1;


    }


    public void setProhibition(boolean prohibition) {
        this.prohibition = prohibition;
    }

    public Player getPlayer() {
        return player;
    }

    //merge function is used to send all its students on another island: the try catch block is due to the fact that an island knows
    //exactly how many students are present and therefore it will never throw the exception
    public void merge(Island island){
        byte[] students= getStudents();

        for (Color c: Color.values()) {
            try{
                moveStudents(c, students[c.ordinal()], island);
            }catch (NotEnoughStudentsException ex){
                System.out.println("qualcosa");
            }

        }


    }


    public byte getNumber() {
        return number;
    }

    public boolean isProhibition() {
        return prohibition;
    }
}
