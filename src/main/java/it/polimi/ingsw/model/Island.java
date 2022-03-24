package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;

public class Island extends GameComponent{
    private Player player;
    private boolean prohibition;
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

    //merge serve per mandare tutti i propri studenti su un'altra isola: il try catch è dovuto al fatto che
    //l'isola sa precisamente quanti studenti ha e quindi non potrà mai lanciarla come eccezion
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
