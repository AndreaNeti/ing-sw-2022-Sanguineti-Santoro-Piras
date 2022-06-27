package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.Wizard;

import java.util.ArrayList;
import java.util.List;

/**
 * GameClientView interface represents the game which the client's view (CLI or GUI) can interact with and get info about the game. <br>
 * It only contains the getter methods required to get info and the components of the game.
 */
public interface GameClientView {

    /**
     * Method getCurrentPlayer returns the player currently playing its turn.
     *
     * @return {@link PlayerClient} - instance of the player currently playing.
     */
    PlayerClient getCurrentPlayer();

    /**
     * Method getCharacters returns the character cards available in the game.
     *
     * @return {@code List}<{@link CharacterCardClient}> - list of instances of the character cards.
     */
    List<CharacterCardClient> getCharacters();

    /**
     * Method getClouds returns the cloud components of the game.
     *
     * @return {@code ArrayList}<{@link GameComponentClient}> - list of instances of the clouds.
     */
    ArrayList<GameComponentClient> getClouds();

    /**
     * Method getIslands returns the island components of the game.
     *
     * @return {@code ArrayList}<{@link IslandClient}> - list of instances of the islands.
     */
    ArrayList<IslandClient> getIslands();

    /**
     * Method getTeams returns the teams playing in the game.
     *
     * @return {@code List}<{@link TeamClient}> - list of instances of the teams.
     */
    List<TeamClient> getTeams();

    /**
     * Method getCurrentCharacterCard returns the character card currently being played.
     *
     * @return {@link CharacterCardClient} - instance of the character card currently being played.
     */
    CharacterCardClient getCurrentCharacterCard();

    /**
     * Method getMotherNaturePosition returns the position of mother nature.
     *
     * @return {@code byte} - position of mother nature, corresponding to the index of the island on which she currently is.
     */
    byte getMotherNaturePosition();

    /**
     * Method isExperts checks if the game is in expert mode.
     *
     * @return {@code boolean} - true if it's an expert game, false else.
     */
    boolean isExpert();

    /**
     * Method getProfessors returns the wizards of the players controlling each professor.
     *
     * @return {@link Wizard}{@code []} - array of wizards controlling each professor (size = number of colors). <br>
     * Each position in the array corresponds to the respective professor's color in the {@link Color} enum.
     */
    Wizard[] getProfessors();

    /**
     * Method getNewProhibitionsLeft returns the amount of prohibitions remaining in the game.
     *
     * @return {@code Byte} - amounts of prohibitions left in the game.
     */
    Byte getNewProhibitionsLeft();

    /**
     * Method getMatchConstants returns the match constants of the game.
     *
     * @return {@link MatchConstants} - match constants of the game.
     */
    MatchConstants getMatchConstants();

    /**
     * Method getMyWizard returns the wizard of the player associated with the client calling this method.
     *
     * @return {@link Wizard} - wizard of the player of the client.
     */
    Wizard getMyWizard();

    /**
     * Method getCoinsPlayer returns the amount of coins of the player with the player index provided.
     *
     * @param wizardIndex of type {@code byte} - index of the player of which we want to get the coins.
     * @return {@code int} - amount of coins owned by the selected player.
     */
    int getCoinsPlayer(byte wizardIndex);

    /**
     * Method getMatchType returns the match type of the game.
     *
     * @return {@link MatchType} - match type of the game.
     */
    MatchType getMatchType();

    /**
     * Method getPlayer returns the player in the game with the provided index.
     *
     * @param index of type {@code int} - index of the player.
     * @return {@link PlayerClient} - instance of the selected player.
     */
    PlayerClient getPlayer(int index);

    /**
     * Method getPlayer returns the players in the game.
     *
     * @return {@code List }<{@link PlayerClient}> - list of instances of the players.
     */
    List<PlayerClient> getPlayers();

    /**
     * Method getIgnoredColorInfluence returns the student color currently being ignored in the influence calculation.
     *
     * @return {@link Color} - color ignored for influence calculation.
     */
    Color getIgnoredColorInfluence();

    /**
     * Method isExtraSteps checks if mother nature can be moved 2 steps more than allowed by the assistant card played.
     *
     * @return {@code boolean} - true if there are 2 extra steps allowed, false else.
     */
    boolean isExtraSteps();

    /**
     * Method getNewCoinsLeft returns the amount of coins available in the game.
     *
     * @return {@code Integer} - amount of coins available in the game.
     */
    Integer getNewCoinsLeft();

    /**
     * Method addListener adds to the game a new client listener, to be notified when changes occur in the game.
     *
     * @param listener of type {@link GameClientListener} - instance of the client listener to add.
     */
    void addListener(GameClientListener listener);
}
