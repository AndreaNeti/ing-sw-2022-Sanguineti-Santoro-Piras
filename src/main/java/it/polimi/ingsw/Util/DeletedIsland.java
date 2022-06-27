package it.polimi.ingsw.Util;

import java.io.Serializable;
import java.util.Set;

/**
 * AssistantCard record represents the assistant cards in "Eriantys", used by players during the planification phase.
 *
 * @param idWinnerIsland of type {@code Byte} - value of idIsland that "wins" after the merge -> the one that lasted
 * @param deletedIsland  of type {@code Set<Byte>} - all the id of the islands that are now merged in the winner
 */
public record DeletedIsland(byte idWinnerIsland, Set<Byte> deletedIsland) implements Serializable {
}
