package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotExpertGameException;

public interface Game {
    void move (Color color, int idGameComponent);
    void drawStudents(GameComponent bag, byte number);
    void playCard(byte card);
    Player getCurrentPlayer();
    void moveMotherNature(int moves);
    void nextPlayer();
    boolean getPhase();
    void nextPhase();
    void calculateInfluence(Island island);
    void calculateInfluence();
    void calculateProfessor();
    void endGame();
    void endGame(Team team);
    void nextActionPhase();
    void refillClouds();
    void setLastRound();
    void setCharacterInput(int input) throws NotExpertGameException;
    void chooseCharacter(int indexCharacter) throws NotExpertGameException;
    void playCharacter() throws NotExpertGameException;
}
