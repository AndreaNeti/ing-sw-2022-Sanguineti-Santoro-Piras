package it.polimi.ingsw.utils;

import java.io.Serializable;

/**
 * MatchConstants record is used to initialize a game with a specific set of immutable attributes. <br>
 *
 * @param numOfCards           of type {@code int} - number of assistant cards per player.
 * @param minIslands           of type {@code int} - minimum amount of islands left necessary to end the game.
 * @param numOfCharacterCards  of type {@code int} - number of character cards available in the game.
 * @param totalCoins           of type {@code int} - total amount of coins available in the game.
 * @param initialPlayerCoins   of type {@code int} - initial amount of coins given to each player.
 * @param entranceHallStudents of type {@code int} - maximum number of students placeable in the entrance hall.
 * @param maxLunchHallStudents of type {@code int} - maximum number of students placeable in the lunch hall.
 * @param towersForTeam        of type {@code int} - initial (and maximum) amount of towers given to each team.
 * @param studentsToMove       of type {@code int} - number of students required to be moved by each player during the respective phase.
 */
public record MatchConstants(int numOfCards, int minIslands, int numOfCharacterCards, int totalCoins,
                             int initialPlayerCoins, int entranceHallStudents, int maxLunchHallStudents,
                             int towersForTeam, int studentsToMove, int extraStep) implements Serializable {
}
