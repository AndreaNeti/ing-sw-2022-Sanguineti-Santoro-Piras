package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.model.Island;

public class IslandClient extends GameComponentClient {
    private HouseColor team;
    private byte prohibition;
    private byte number;

    public IslandClient(int i){
        super(i);
    }
    public IslandClient(IslandClient islandClient){
        super(islandClient.getId());
        this.team=islandClient.team;
        this.prohibition=islandClient.prohibition;
        this.number=islandClient.number;
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
    public void modifyGameComponent(GameComponent gameComponent) {
        super.modifyGameComponent(gameComponent);
        Island island = (Island) gameComponent;
        team = island.getTeamColor();
        number = island.getNumber();
        prohibition = island.getProhibitions();
    }

    @Override
    public String toString() {
        if (prohibition != 0)
            return super.toString() + "; owned by " + team + "; number of towers " + number + "; numberOf prohibition= " + prohibition;
        else
            return super.toString() + "; owned by " + team + "; number of towers " + number;
    }

}
