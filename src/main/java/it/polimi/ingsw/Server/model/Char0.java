package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

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
        if(idIsland > 2 * game.getPlayerSize() + 12 || idIsland < 2 * game.getPlayerSize())
            throw new NotAllowedException("Set wrong input for id Island");
        this.moveStudents(Color.values()[color], (byte) 1, game.getComponentById(idIsland));
        game.drawStudents(this, (byte) 1);
        game.getGameDelta().addUpdatedGC(this);
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Char0Client)) return false;
//        CharacterCard c = (CharacterCard) o;
//        return getCharId() == c.getCharId();
//    }
}
