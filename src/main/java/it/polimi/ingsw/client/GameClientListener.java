package it.polimi.ingsw.client;

import it.polimi.ingsw.client.model.GameComponentClient;
import it.polimi.ingsw.client.model.IslandClient;
import it.polimi.ingsw.client.model.PlayerClient;
import it.polimi.ingsw.client.model.TeamClient;
import it.polimi.ingsw.utils.*;

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
     * @param color  of type {@link Color} - color of the updated professor.
     * @param wizard of type {@link Wizard} - wizard associated with the new controller.
     */
    void updateProfessor(Color color, Wizard wizard);

    /**
     * Method updateMembers updates new list of member in the game.
     *
     * @param membersLeftToStart of type {@code int} - updated amount of players left before the game starts.
     * @param playerJoined       of type {@code PlayerClient} - the player that just joined the game.
     */
    void updateMembers(int membersLeftToStart, PlayerClient playerJoined);

    /**
     * Method updateMatchInfo sets the Match info when you join a match
     *
     * @param matchType of type {@code MatchType} - a record telling you in which type of match you joined.
     * @param constants of type {@code MatchConstants} - a record of constants used during the match.
     * @param teams     of type {@code List}<{@link TeamClient}> - list of teams already in lobby.
     */
    void updateMatchInfo(MatchType matchType, MatchConstants constants, List<TeamClient> teams);

    /**
     * Method updateCardPlayed updates the assistant card played.
     *
     * @param playedCard of type {@link AssistantCard} - instance of the updated assistant card played.
     * @param wizard     of type {@link Wizard } - wizard that played the card
     */
    void updateCardPlayed(AssistantCard playedCard, Wizard wizard);

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
     * @param charId of type {@code byte} - ID of the updated character card.
     */
    void updateCharacter(byte charId);

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
     * @param coins  of type {@code Integer} - updated amount of coins owned by the player.
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

    /**
     * Method updateError notifies a new error.
     *
     * @param error of type {@code String} - the error message.
     */
    void updateError(String error);

    /**
     * Method updateCurrentPlayer updates the new current player.
     *
     * @param newCurrentPlayer of type {@code byte} - the new current player id.
     */
    void updateCurrentPlayer(byte newCurrentPlayer);
}
