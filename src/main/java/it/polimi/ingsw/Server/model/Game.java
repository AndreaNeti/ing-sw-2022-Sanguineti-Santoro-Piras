package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;

import java.io.Serializable;
import java.util.ArrayList;

public interface Game extends Serializable {
    void move(Color color, int idGameComponentSource, int idGameComponentDestination) throws GameException;

    void playCard(byte card) throws GameException, EndGameException;

    void setCurrentPlayer(Player p);
    void setCurrentPlayer(byte currentPlayerIndex);

    void moveMotherNature(int moves) throws NotAllowedException, EndGameException;

    ArrayList<HouseColor> calculateWinner();

    void refillClouds() throws EndGameException;

    void setCharacterInput(int input) throws GameException;

    void chooseCharacter(byte indexCharacter) throws GameException;

    void playCharacter() throws GameException, EndGameException;

    void moveFromCloud(int cloudId) throws GameException;
    GameDelta getGameDelta();
    GameDelta transformAllGameInDelta();
}
