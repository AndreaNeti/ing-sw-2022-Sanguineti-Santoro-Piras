package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.Util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

/**
 * Char2 class represents the <b>"Herald"</b> character card. <br>
 * <b>Effect</b>: Choose an Island and resolve the Island as if Mother Nature had ended her movement there.
 * Mother Nature will still move and the Island where she ends her movement will also be resolved.<br>
 * <b>Inputs required</b>: Island ID.
 */
public class Char2 implements CharacterCardLogicInterface {

    /**
     * Method play recalculates the influence on the selected island.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card modifies with its effect.
     * @throws GameException if the island ID is not valid.
     * @throws EndGameException if after calculating the influence on the island there are less than 3 islands left
     * or a team has no towers left.
     */
    @Override
    public void play(GameInterfaceForCharacter game) throws GameException, EndGameException {
        int idIsland = game.getCharacterInputs().get(0);
        if (idIsland > 2 * MatchType.MAX_PLAYERS + 12 || idIsland < 2 *MatchType.MAX_PLAYERS)
            throw new NotAllowedException("Set wrong inputs for idIsland");
        game.calculateInfluence((Island) game.getComponentById(idIsland));
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 1;
    }

}