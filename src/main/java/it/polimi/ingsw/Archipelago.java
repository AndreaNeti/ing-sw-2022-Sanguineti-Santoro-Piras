package it.polimi.ingsw;

import java.util.List;

public class Archipelago {
    private final int idArchipelago;
    private List<Island> islands;

    public Archipelago(int id) {
        this.idArchipelago = id;
    }

    public int getIdArchipelago() {
        return idArchipelago;
    }

    public void calculateInfluence() {
    }

}
