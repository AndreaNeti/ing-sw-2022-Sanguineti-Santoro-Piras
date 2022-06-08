package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.ArrayList;

/**
 * CharacterCardGame interface is a game with only functions required by character cards to apply their effects.
 */
public interface CharacterCardGame {
    /**
     * Method getCharacterInputs returns the inputs added by the player for the chosen character card.
     *
     * @return ArrayList<Integer> - list of the inputs added by the player.
     */
    ArrayList<Integer> getCharacterInputs();
    /**
     * Method setEqualProfessorCalculation is called by the character card to set to true the respective boolean.
     */
    void setEqualProfessorCalculation();
    /**
     * Method setExtraSteps is called by the character card to set to true the respective boolean.
     */
    void setExtraSteps();
    /**
     * Method removeTowerInfluence is called by the character card to set to false the respective boolean.
     */
    void removeTowerInfluence();
    /**
     * Method setIgnoredColorInfluence is called by the character card to set the color to ignore when calculating influence.
     *
     * @param ignoredColorInfluence of type Color - color of the students to ignore during the influence calculation.
     */
    void setIgnoredColorInfluence(Color ignoredColorInfluence);
    /**
     * Method getGameDelta is used to obtain the GameDelta of the game.
     *
     * @return GameDelta - instance of the game's GameDelta.
     */
    GameDelta getGameDelta();
    /**
     * Method setExtraInfluence is called by the character card to set to true the respective boolean.
     */
    void setExtraInfluence();
    /**
     * Method setProhibition is called by the character card to add a prohibition to a selected island.
     * @param island of type Island - instance of the island to which the prohibition is added.
     * @throws NotAllowedException if there are no more prohibitions left in the game (maximum 4).
     */
    void setProhibition(Island island) throws NotAllowedException;
    /**
     * Method getComponentById gets a game component instance based on his unique ID.
     *
     * @param idGameComponent of type int - the id of the game component
     * @return GameComponent - the instance of the game component
     * @throws GameException if the id is not a valid one or corresponds to a merged island
     */
    GameComponent getComponentById(int idGameComponent) throws GameException;
    /**
     * Method drawStudents draws students from the bag to the selected game component.
     *
     * @param gameComponent of type GameComponent - the game component on which we want to put the students.
     * @param students of type byte - the number of students to draw.
     * @throws EndGameException if there are no more students available on the bag.
     * @throws GameException if the game component selected is null.
     */
    void drawStudents(GameComponent gameComponent, byte students) throws EndGameException, GameException;
    /**
     * Method calculateInfluence sets the team with the highest influence as the controller of the selected island,
     * based on the number of students present for each controlled color and on the number of towers on the island.
     *
     * @param island of type Island - the island of which we want to calculate the new controller.
     * @throws EndGameException if a team has no towers left in its board.
     */
    void calculateInfluence(Island island) throws EndGameException;
    /**
     * Method getPlayerSize returns the number of players in the current game.
     *
     * @return byte - number of players.
     */
    byte getPlayerSize();
    /**
     * Method getPlayerSize returns the current player.
     *
     * @return Player - instance of the current player.
     */
    Player getCurrentPlayer();
    /**
     * Method calculateProfessor compares for each color the number of students in the lunch hall of each player
     * and then puts the wizard of the player with the most students in the professors array, in the slot of the color compared.
     */
    void calculateProfessor();
}
