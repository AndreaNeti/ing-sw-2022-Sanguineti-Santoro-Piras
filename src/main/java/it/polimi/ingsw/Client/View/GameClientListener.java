package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;

import java.util.List;

/**
 * GameClientListener interface represents the client's views (CLI or GUI) that listen for updates of the game in order
 * to update themselves. <br>
 * Each method of this interface will update a specific part of the game.
 */
public interface GameClientListener {

    /**
     * Method updateMotherNature updates the position of mother nature.
     *
     * @param motherNaturePosition of type {@code Byte} - updated island index position of mother nature.
     */
    void updateMotherNature(Byte motherNaturePosition);

    /**
     * Method updateGameComponent updates a game component.
     *
     * @param gameComponent of type {@link GameComponentClient} - instance of the updated game component.
     */
    void updateGameComponent(GameComponentClient gameComponent);

    /**
     * Method updateGameComponent updates an island component.
     *
     * @param island of type {@link IslandClient} - instance of the updated island.
     */
    void updateGameComponent(IslandClient island);

    /**
     * Method updateDeletedIsland removes an island that was merged in the game.
     *
     * @param island         of type {@link IslandClient} - instance of the removed island.
     * @param idIslandWInner is the island that last after the merge
     */
    void updateDeletedIsland(IslandClient island, IslandClient idIslandWInner);

    /**
     * Method updateTowerLeft updates the tower left of a team.
     *
     * @param houseColor of type {@link HouseColor} - house color of the updated team.
     * @param towerLefts of type {@code Byte} - updated amount of towers left.
     */
    void updateTowerLeft(HouseColor houseColor, Byte towerLefts);

    /**
     * Method updateProfessor updates the controller of a professor.
     *
     * @param color of type {@link Color} - color of the updated professor.
     * @param wizard of type {@link Wizard} - wizard associated with the new controller.
     */
    void updateProfessor(Color color, Wizard wizard);

    /**
     * Method updateMembers updates new list of member in the game.
     *
     * @param membersLeftToStart of type {@code int} - updated amount of players left before the game starts.
     * @param nickPlayerJoined of type {@code String} - nickname of the player that just joined the game.
     */
    void updateMembers(int membersLeftToStart, String nickPlayerJoined);

    /**
     * Method updateCardPlayed updates the assistant card played.
     *
     * @param playedCard of type {@link AssistantCard} - instance of the updated assistant card played.
     */
    void updateCardPlayed(AssistantCard playedCard);

    /**
     * Method updateIgnoredColor updates the color ignored for influence calculation.
     *
     * @param color of type {@link Color} - instance of the updated ignored color.
     */
    void updateIgnoredColor(Color color);

    /**
     * Method updateExtraSteps updates the extra steps available for mother nature movement.
     *
     * @param extraSteps of type {@code boolean} - boolean to check if mother nature can move two extra steps.
     */
    void updateExtraSteps(boolean extraSteps);

    /**
     * Method updateCharacter updates a character card.
     *
     * @param charId of type {@code int} - ID of the updated character card.
     */
    void updateCharacter(int charId);

    /**
     * Method updateCoins updates the amount of coins left in the game.
     *
     * @param coins of type {@code Integer} - updated amount of coins left in the game.
     */
    void updateCoins(Integer coins);

    /**
     * Method updateCoins updates the amount of coins owned by a player.
     *
     * @param wizard of type {@link Wizard} - wizard associated with the updated player.
     * @param coins of type {@code Integer} - updated amount of coins owned by the player.
     */
    void updateCoins(Wizard wizard, Integer coins);

    /**
     * Method setWinners updates the winners of the game.
     *
     * @param winners of type {@code List}<{@link HouseColor}> - list of the house colors of the winner teams.
     */
    void setWinners(List<HouseColor> winners);

    /**
     * Method updateMessage updates the client's chat with a new message.
     *
     * @param message of type {@code String} - new text message.
     */
    void updateMessage(String message);

    /**
     * Method updateProhibitions updates the amount of prohibitions left in the game.
     *
     * @param newProhibitions of type {@code Byte} - updated amount of prohibitions left in the game.
     */
    void updateProhibitions(Byte newProhibitions);

    void updateError(String error);

    void updateCurrentPlayer(byte newCurrentPlayer);
}
