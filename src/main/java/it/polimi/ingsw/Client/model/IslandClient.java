package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;

public class IslandClient extends GameComponentClient {
    private HouseColor team;
    private byte prohibition;
    private byte number;

    public IslandClient(int i) {
        super(i);
    }

    public HouseColor getTeam() {
        return team;
    }

    public void setTeam(HouseColor team) {
        this.team = team;
    }

    public byte getProhibition() {
        return prohibition;
    }

    public void setProhibition(byte prohibition) {
        this.prohibition = prohibition;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    @Override
    protected void modifyGameComponent(GameComponent gameComponent) {
        super.modifyGameComponent(gameComponent);
        Island island = (Island) gameComponent;
        team = island.getTeamColor();
        number = island.getNumber();
        prohibition = island.getProhibitions();
    }

    @Override
    public String toString() {
        String s = super.toString();
        if (team != null) {
            s += "| Owned by " + team + " with " + number + " towers ";
        }
        if (prohibition != 0)
            s += "| Number of prohibitions: " + prohibition;
        return s;
    }

    @Override
    public String getNameOfComponent() {
        return "Island";
    }
}
