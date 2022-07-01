package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.AssistantCard;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.HouseColor;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.network.GameDelta;

import java.util.ArrayList;
import java.util.List;

/**
 * Game interface represents the game logic in "Eriantys". <br>
 * It is used to implement the decorator pattern for the normal and expert game, that
 * implement this interface. <br>
 */
public interface Game {

    /**
     * Method move is used to move a student from a game component to another, using their unique ID.
     *
     * @param color of type {@link Color} - the color of the student to move.
     * @param idGameComponentSource of type {@code int} - the ID of the source component.
     * @param idGameComponentDestination of type {@code int} - the ID of the target component.
     * @throws GameException if the color is null or if at least on ID is not valid or if it's not possible to move the student.
     */
    void move(Color color, int idGameComponentSource, int idGameComponentDestination) throws GameException;

    /**
     * Method playCard is used by each player to play an assistant card during the planification phase.
     *
     * @param card of type {@link AssistantCard} - the card that the player wants to play.
     * @throws GameException if the card value is not in the permitted range of values.
     * @throws EndGameException if after playing the selected card there are no cards available left.
     */
    void playCard(AssistantCard card) throws GameException, EndGameException;

    /**
     * Method setCurrentPlayer updates the current player based on his index.
     *
     * @param currentPlayerIndex of type {@code byte} - the index of the new current player.
     */
    void setCurrentPlayer(byte currentPlayerIndex);

    /**
     * Method moveMotherNature moves mother nature by a number of steps selected by the player and then
     * recalculates the influence on the new island position of mother nature.
     *
     * @param moves of type {@code int} - number of steps the player want to move mother nature.
     * @throws NotAllowedException if the number of moves is bigger than the value allowed.
     * @throws EndGameException if the number of islands left is <= 3.
     */
    void moveMotherNature(int moves) throws NotAllowedException, EndGameException;

    /**
     * Method calculateWinner returns the team with fewer towers left. In case of a tie, the winner is the team with
     * more professors controlled. In case of another tie, two or more teams are considered winners.
     *
     * @return {@code ArrayList}<{@link HouseColor}> - the list of winning teams.
     */
    ArrayList<HouseColor> calculateWinner();

    /**
     * Method refillClouds draws 3 or 4 students based on the number of players to each cloud.
     *
     * @throws EndGameException if there are no more students available on the bag.
     */
    void refillClouds() throws EndGameException;

    /**
     * Method setCharacterInputs is used by the player to add inputs to the character card.
     *
     * @param inputs of type {@code List}<{@code Integer}> - list of inputs for character chard.
     * @throws GameException if there is no character card selected.
     */
    void setCharacterInputs(List<Integer> inputs) throws GameException;

    /**
     * Method chooseCharacter is used to select one of the 3 available character cards based on their unique ID.
     *
     * @param charId of type {@code Byte} - index of the character card chosen. It can be null so it deselects the current character
     * @throws GameException if the selected card is not available in the current game or the player doesn't have enough coins.
     */
    void chooseCharacter(Byte charId) throws GameException;

    /**
     * Method playCharacter is used to play the selected character card, applying its effects, removing coins
     * from the player and increasing its cost by 1 if it has never been played before.
     *
     * @throws GameException if the inputs set by the player are invalid (wrong inputs or more/fewer than requested).
     * @throws EndGameException if the character card's effect trigger an endgame event (no more students in the bag,
     * no more towers in a team's board or less than 3 islands left)
     */
    void playCharacter() throws GameException, EndGameException;

    /**
     * Method moveFromCloud moves students from the selected cloud to the player's entrance hall.
     *
     * @param cloudId of type {@code int} - the unique ID of the selected cloud.
     * @throws GameException if the selected cloud has no students.
     */
    void moveFromCloud(int cloudId) throws GameException;

    /**
     * Method getGameDelta is used to obtain the GameDelta of the game.
     *
     * @return {@link GameDelta} - instance of the game's GameDelta.
     */
    GameDelta getGameDelta();

    /**
     * Method transformAllGameInDelta saves all the game info inside the GameDelta that is sent to the client.
     *
     * @return {@link GameDelta} - GameDelta with all the info of the game.
     */
    GameDelta transformAllGameInDelta();
}
