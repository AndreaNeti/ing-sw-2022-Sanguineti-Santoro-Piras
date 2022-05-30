package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.util.ArrayList;

public interface Game {
    void move(Color color, int idGameComponentSource, int idGameComponentDestination) throws GameException;

    void playCard(AssistantCard card) throws GameException, EndGameException;

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
