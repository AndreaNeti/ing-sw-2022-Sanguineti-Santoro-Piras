package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.network.toClientMessage.DeltaUpdate;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;

import java.io.Serializable;
import java.util.*;

/**
 * GameDelta class is used to send all the server's game changes to the clients, instead of sending
 * the whole game each time an update happens. <br>
 * The GameDelta will send info about updated game components, removed islands, new professors' controllers,
 * new towers left in the teams, new position of mother nature and the played assistant card.
 */
public class GameDelta implements Serializable {
    private final transient ArrayList<GameListener> listeners;
    // GC index, students array
    private Map<Byte, GameComponent> updatedGC;
    // deleted islands ids
    private Set<Byte> deletedIslands;

    // professor color, new wizard controlling
    private Map<Color, Wizard> updatedProfessors;
    // towers left
    private Map<HouseColor, Byte> newTeamTowersLeft;
    private Byte newMotherNaturePosition;
    private AssistantCard playedCard;

    /**
     * Constructor GameDelta creates a new instance of GameDelta.
     */
    public GameDelta() {
        listeners = new ArrayList<>();
        clear();
    }

    /**
     * Method clear sets the GameDelta attributes to their default values.
     */
    public void clear() {
        updatedGC = null;
        deletedIslands = null;
        newTeamTowersLeft = null;
        updatedProfessors = null;
        newMotherNaturePosition = null;
        playedCard = null;
    }

    /**
     * Method addUpdatedGC adds a game component that has been modified to the game delta.
     *
     * @param gcUpdated of type {@link GameComponent} - instance of the updated game component.
     */
    public void addUpdatedGC(GameComponent gcUpdated) {
        if (updatedGC == null)
            updatedGC = new HashMap<>();
        updatedGC.put(gcUpdated.getId(), gcUpdated);
    }

    /**
     * Method addDeletedIslands adds an island that has been merged and therefore deleted to the game delta.
     *
     * @param deletedIsland of type {@link Island} - instance of the deleted island.
     */
    public void addDeletedIslands(Island deletedIsland) {
        if (deletedIslands == null)
            deletedIslands = new HashSet<>();
        deletedIslands.add(deletedIsland.getId());
    }

    /**
     * Method updateTeamTowersLeft adds the team with its new number of towers left to the game delta.
     *
     * @param teamColor of type {@link HouseColor} - house color of the team.
     * @param newTowersLeft of type {@code byte} - new amount of towers left in the team.
     */
    public void updateTeamTowersLeft(HouseColor teamColor, byte newTowersLeft) {
        if (newTeamTowersLeft == null)
            newTeamTowersLeft = new HashMap<>();
        newTeamTowersLeft.put(teamColor, newTowersLeft);
    }

    /**
     * Method addUpdatedProfessors adds the color of the professor and its new controller to the game delta.
     *
     * @param professorColor of type {@link Color} - color of the professor.
     * @param newController of type {@link Wizard} - wizard of the new professor's controller.
     */
    public void addUpdatedProfessors(Color professorColor, Wizard newController) {
        if (updatedProfessors == null)
            updatedProfessors = new HashMap<>();
        updatedProfessors.put(professorColor, newController);
    }

    /**
     * Method setNewMotherNaturePosition adds the new position of mother nature to the game delta.
     *
     * @param newMotherNaturePosition of type {@code byte} - new position of mother nature.
     */
    public void setNewMotherNaturePosition(byte newMotherNaturePosition) {
        this.newMotherNaturePosition = newMotherNaturePosition;
    }

    /**
     * Method setPlayedCard adds the played assistant card to the game delta.
     *
     * @param playedCard of type {@link AssistantCard} - instance of the played assistant card.
     */
    public void setPlayedCard(AssistantCard playedCard) {
        this.playedCard = playedCard;
    }

    /**
     * Method addListener adds a new game listener that will be updated by the game delta.
     *
     * @param gameListener of type {@link GameListener} - instance of the new game listener.
     */
    public void addListener(GameListener gameListener) {
        listeners.add(gameListener);
    }


    /**
     * Method send is used to update all listeners added to the game delta via a DeltaUpdate message. <br>
     * After sending the delta update the game delta is reset.
     */
    public void send() {
        ToClientMessage m = new DeltaUpdate(this);
        for (GameListener listener : listeners)
            listener.update(m);
        clear();
    }

    /**
     * Method setUpdatedCoinPlayer not used for GameDelta.
     *
     * @param playerId of type {@code byte} - ID of the player.
     * @param newCoinsPlayer of type {@code byte} - new amount of coins for the player.
     */
    public void setUpdatedCoinPlayer(byte playerId, byte newCoinsPlayer) {
    }

    /**
     * Method setNewCoinsLeft not used for GameDelta.
     *
     * @param newCoinsLeft of type {@code byte} - new amount of coins left in the game.
     */
    public void setNewCoinsLeft(byte newCoinsLeft) {
    }

    /**
     * Method setNewProhibitionsLeft not used for GameDelta.
     *
     * @param newProhibitionsLeft of type {@code byte} - new amount of prohibitions left in the game.
     */
    public void setNewProhibitionsLeft(byte newProhibitionsLeft) {
    }

    /**
     * Method setExtraSteps not used for GameDelta.
     *
     * @param extraSteps of type {@code Boolean} - new value of the extraSteps boolean in the game.
     */
    public void setExtraSteps(Boolean extraSteps) {
    }

    /**
     * Method setIgnoredColorInfluence not used for GameDelta.
     *
     * @param ignoredColorInfluence of type {@link Color} - color that will be ignored.
     */
    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
    }

    /**
     * Method setUsedCharacter not used for GameDelta.
     *
     * @param charId of type {@code byte} - ID of the used character card.
     * @param used of type {@code boolean} - boolean used to know if the character card has been already used.
     */
    public void setUsedCharacter(byte charId, boolean used) {
    }

    /**
     * Method getCharacters not used for GameDelta.
     *
     * @return {@code List}<{@code Byte}> - Empty ArrayList.
     */
    public List<Byte> getCharacters() {
        return new ArrayList<>();
    }

    /**
     * Method getUpdatedCoinPlayer not used for GameDelta.
     *
     * @return {@code Map}<{@code Byte, Byte}> - Empty Map.
     */
    public Map<Byte, Byte> getUpdatedCoinPlayer() {
        return Collections.emptyMap();
    }

    /**
     * Method getNewCoinsLeft not used for GameDelta.
     *
     * @return {@code Optional}<{@code Byte}> - Empty Optional.
     */
    public Optional<Byte> getNewCoinsLeft() {
        return Optional.empty();
    }

    /**
     * Method getNewProhibitionsLeft not used for GameDelta.
     *
     * @return {@code Optional}<{@code Byte}> - Empty Optional.
     */
    public Optional<Byte> getNewProhibitionsLeft() {
        return Optional.empty();
    }

    /**
     * Method isExtraSteps not used for GameDelta.
     *
     * @return {@code Optional}<{@code Boolean}> - Empty Optional.
     */
    public Optional<Boolean> isExtraSteps() {
        return Optional.empty();
    }

    /**
     * Method getIgnoredColorInfluence not used for GameDelta.
     *
     * @return {@code Optional}<{@code Color}> - Empty Optional.
     */
    public Optional<Color> getIgnoredColorInfluence() {
        return Optional.empty();
    }

    /**
     * Method getUsedCharacter not used for GameDelta.
     *
     * @return {@code Map}<{@code Byte, Boolean}> - Empty Map.
     */
    public Map<Byte, Boolean> getUsedCharacter() {
        return Collections.emptyMap();
    }


    /**
     * Method getUpdatedGC returns all the updated game components. <br>
     * <b>Map entry</b>: (game component's ID - GameComponent instance).
     *
     * @return {@code Map}<{@code Byte}, {@link GameComponent}> - map of all the updated game components.
     */
    public Map<Byte, GameComponent> getUpdatedGC() {
        if (updatedGC == null) return Collections.emptyMap();
        return updatedGC;
    }

    /**
     * Method getNewMotherNaturePosition returns the new position of mother nature.
     *
     * @return {@code Optional}<{@code Byte}> - if present the new mother nature's position, Empty Optional else.
     */
    public Optional<Byte> getNewMotherNaturePosition() {
        return Optional.ofNullable(newMotherNaturePosition);
    }

    /**
     * Method getUpdatedProfessors returns all the updated professors' controllers. <br>
     * <b>Map entry</b>: (Professor color - New controller).
     *
     * @return {@code Map}<{@link Color}, {@link Wizard}> - map of all the updated professors' controllers.
     */
    public Map<Color, Wizard> getUpdatedProfessors() {
        if (updatedProfessors == null) return Collections.emptyMap();
        return updatedProfessors;
    }

    /**
     * Method getPlayedCard returns the played assistant card.
     *
     * @return {@code Optional}<{@link AssistantCard}> - if present the played assistant card, Empty Optional else.
     */
    public Optional<AssistantCard> getPlayedCard() {
        return Optional.ofNullable(playedCard);
    }

    /**
     * Method getDeletedIslands returns all the deleted islands. <br>
     * <b>Set entry</b>: (Island's ID).
     *
     * @return {@code Set}<{@code Byte}> - map of all the deleted islands.
     */
    public Set<Byte> getDeletedIslands() {
        if (deletedIslands == null) return Collections.emptySet();
        return deletedIslands;
    }

    /**
     * Method getNewTeamTowersLeft returns all the teams with their updated number of towers left. <br>
     * <b>Map entry</b>: (Team's house color - new amount of towers left).
     *
     * @return {@code Map}<{@link HouseColor}, {@code Byte}> - map of all the teams with their updated number of towers left.
     */
    public Map<HouseColor, Byte> getNewTeamTowersLeft() {
        if (newTeamTowersLeft == null) return Collections.emptyMap();
        return newTeamTowersLeft;
    }

    /**
     * Method toString returns all the GameDelta attributes.
     *
     * @return {@code String} - "GameDelta{ listeners = X, updatedGC = Y, deletedIslands = Z, updatedProfessors = W, newTeamTowersLeft = XX,
     *          newMotherNaturePosition = YY, playedCard = ZZ }"
     */
    @Override
    public String toString() {
        return "GameDelta{" +
                " listeners = " + listeners +
                ", updatedGC = " + updatedGC +
                ", deletedIslands = " + deletedIslands +
                ", updatedProfessors = " + updatedProfessors +
                ", newTeamTowersLeft = " + newTeamTowersLeft +
                ", newMotherNaturePosition = " + newMotherNaturePosition +
                ", playedCard = " + playedCard +
                " }";
    }
}
