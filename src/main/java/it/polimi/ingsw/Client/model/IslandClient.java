package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.Util.HouseColor;

/**
 * IslandClient class represents the island on the client side and corresponds to the server class {@link Island}.
 */
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

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    /**
     * Method modifyGameComponent updates the students, the ID, the owner team, the number of towers and the prohibitions
     * of the island to match the ones of the game component provided.
     *
     * @param gameComponent of type {@link GameComponent} - instance of the game component.
     */
    @Override
    protected void modifyGameComponent(GameComponent gameComponent) {
        super.modifyGameComponent(gameComponent);
        Island island = (Island) gameComponent;
        team = island.getTeamColor();
        number = island.getNumber();
        prohibition = island.getProhibitions();
    }

    /**
     * Method toString returns the name of the island, its students, its team, its number of towers and its prohibitions.
     *
     * @return {@code String} - "Island | Students: (RED = X, BLUE = Y, ...) | Owned by (team) with Z towers | Number of prohibitions: W".
     */
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

    /**
     * Method getNameOfComponent returns the name of the component.
     *
     * @return {@code String} - "Island".
     */
    @Override
    public String getNameOfComponent() {
        return "Island";
    }
}
