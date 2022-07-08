package it.polimi.ingsw.server.model.characterServerLogic;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.model.GameInterfaceForCharacter;

/**
 * Char11 class represents the <b>"Paolino"</b> character card. <br>
 * <b>Effect</b>: Instantly win the game, all other players have to keep living their lives knowing
 * they got destroyed by Paolino's mighty will. <br>
 * <b>Inputs required</b>: None.
 */
public class CharP implements CharacterServerLogicInterface {

    /**
     * Method play throws an EndGameException, effectively ending the game.
     *
     * @param game of type {@link GameInterfaceForCharacter} - the game instance that the card ends with its effect.
     * @throws EndGameException when this card is played: the game must end under the incredible influence of this card.
     */
    @Override
    public void play(GameInterfaceForCharacter game) throws EndGameException, GameException {
        if(game.getCurrentPlayer().toString().equals("Paolino"))
            throw new EndGameException(true, true);
        else
            throw new NotAllowedException("You are not Paolino >:(");
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 0;
    }
}
