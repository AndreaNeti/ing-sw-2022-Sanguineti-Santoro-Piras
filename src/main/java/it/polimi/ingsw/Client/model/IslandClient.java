package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.Util.HouseColor;

/**
 * IslandClient class represents the island on the client side and corresponds to the server class {@link Island}.
 */
public class IslandClient extends GameComponentClient {
    private HouseColor team;
    private byte prohibitions;
    private byte archipelagoSize;

    /**
     * Constructor IslandClient creates a new instance of IslandClient.
     *
     * @param id of type {@code int} - unique ID to assign to the game component.
     */
    public IslandClient(int id) {
        super(id);
    }

    /**
     * Method getTeam returns the team controlling the island.
     *
     * @return {@link HouseColor} - house color of the team controlling the island.
     */
    public HouseColor getTeam() {
        return team;
    }

    /**
     * Method setTeam updates the team controlling the island.
     *
     * @param team of type {@link HouseColor} - color of the new team controlling the island.
     */
    public void setTeam(HouseColor team) {
        this.team = team;
    }

    /**
     * Method getProhibitions returns the number of prohibitions placed on the island.
     *
     * @return {@code byte} - number of prohibitions.
     */
    public byte getProhibitions() {
        return prohibitions;
    }

    /**
     * Method getArchipelagoSize returns the size of the archipelago of the island.
     *
     * @return {@code byte} - total number of islands in the archipelago.
     */
    public byte getArchipelagoSize() {
        return archipelagoSize;
    }

    //TODO: not used?
    /**
     * Method setNumber updates size of the archipelago of the island.
     *
     * @param archipelagoSize of type {@code byte} - new size of the archipelago.
     */
    public void setArchipelagoSize(byte archipelagoSize) {
        this.archipelagoSize = archipelagoSize;
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
        archipelagoSize = island.getArchipelagoSize();
        prohibitions = island.getProhibitions();
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
            s += "| Owned by " + team + " with " + archipelagoSize + " towers ";
        }
        if (prohibitions != 0)
            s += "| Number of prohibitions: " + prohibitions;
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
