package it.polimi.ingsw.model;

public interface Game {
    void move (Color color, int idGameComponent);
    void drawStudents(GameComponent bag, byte number);
    boolean playCard(byte card);
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
    void setCharacterInput(int input);
    void chooseCharacter(int indexCharacter);
    void playCharacter();
}
