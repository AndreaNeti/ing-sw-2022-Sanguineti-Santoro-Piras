package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.List;

public class Char0 extends GameComponent implements CharacterCard {

    public Char0(byte idGameComponent) {
        super(4, idGameComponent);
    }

    @Override
    public void play(CharacterCardGame game) throws GameException, EndGameException {
        List<Integer> inputs = game.getCharacterInputs();
        // input 0 is the color chosen, input 1 is the island id chosen
        int color = inputs.get(0), idIsland = inputs.get(1);
        //TODO Check this idIsland
        if (color < 0 || color >= Color.values().length)
            throw new NotAllowedException("Set wrong inputs for color");
        if (idIsland > 2 * game.getPlayerSize() + 12 || idIsland < 2 * game.getPlayerSize())
            throw new NotAllowedException("Set wrong input for id Island");
        GameComponent islandDestination = game.getComponentById(idIsland);
        this.moveStudents(Color.values()[color], (byte) 1, islandDestination);
        game.drawStudents(this, (byte) 1);
        game.getGameDelta().addUpdatedGC(this);
        game.getGameDelta().addUpdatedGC(islandDestination);
    }

    @Override
    public byte getCost() {
        return 1;
    }

    @Override
    public byte getCharId() {
        return 0;
    }

    @Override
    public boolean canPlay(int nInput) {
        return nInput == 2;
    }

}
