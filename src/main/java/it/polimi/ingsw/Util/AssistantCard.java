package it.polimi.ingsw.Util;

import java.io.Serializable;

/**
 * AssistantCard record represents the assistant cards in "Eriantys", used by players during the planification phase.
 *
 * @param value of type {@code byte} - value of the card, from 1 to 10.
 * @param moves of type {@code byte} - number of moves of mother nature that the card allows ( (value+1) / 2).
 */
public record AssistantCard(byte value, byte moves) implements Serializable {
}
