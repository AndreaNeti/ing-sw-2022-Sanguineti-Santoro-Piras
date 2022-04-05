package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

public interface Game {
    void move(Color color, int idGameComponent, byte actionPhase) throws GameException;

    void drawStudents(GameComponent gameComponent, byte number) throws EndGameException;

    void playCard(byte card) throws GameException, EndGameException;

    Player getCurrentPlayer();

    void setCurrentPlayer(Player p);

    void moveMotherNature(int moves) throws NotAllowedException, EndGameException;

    Team calculateWinner();

    void refillClouds() throws EndGameException;

    void setCharacterInput(int input) throws GameException;

    void chooseCharacter(int indexCharacter) throws NotExpertGameException, UnexpectedValueException, NotAllowedException;

    void playCharacter() throws GameException, EndGameException;
}
