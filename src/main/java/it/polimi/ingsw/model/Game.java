package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;

public interface Game {
    void move(Color color, int gameComponentSource, int gameComponentDestination) throws GameException;

    void playCard(byte card) throws GameException, EndGameException;

    void setCurrentPlayer(Player p);

    void moveMotherNature(int moves) throws NotAllowedException, EndGameException;

    ArrayList<Team> calculateWinner();

    void refillClouds() throws EndGameException;

    void setCharacterInput(int input) throws GameException;

    void chooseCharacter(int indexCharacter) throws GameException;

    void playCharacter() throws GameException, EndGameException;

    void moveFromCloud(int cloudId) throws NotAllowedException;
}
