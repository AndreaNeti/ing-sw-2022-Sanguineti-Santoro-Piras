package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.util.Set;

/**
 * DeletedIsland record is used to inform the client about the islands that are merged into the group of islands.
 *
 * @param idWinnerIsland of type {@code Byte} - value of idIsland that "wins" after the merge -> the one that lasted
 * @param deletedIsland  of type {@code Set<Byte>} - all the id of the islands that are now merged in the winner
 */
public record DeletedIsland(byte idWinnerIsland, Set<Byte> deletedIsland) implements Serializable {
}
