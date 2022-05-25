package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.ArrayList;

public interface CharacterCardGame {
    ArrayList<Integer> getCharacterInputs();

    void setEqualProfessorCalculation();

    void setExtraSteps();

    void removeTowerInfluence();

    void setIgnoredColorInfluence(Color c);

    GameDelta getGameDelta();

    void setExtraInfluence();

    void setProhibition(Island i) throws NotAllowedException;

    GameComponent getComponentById(int id) throws GameException;

    void drawStudents(GameComponent gc, byte n) throws EndGameException;

    void calculateInfluence(Island i) throws EndGameException;

    byte getPlayerSize();

    Player getCurrentPlayer();
}
