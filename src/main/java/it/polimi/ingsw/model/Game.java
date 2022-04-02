package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotExpertGameException;

import java.util.ArrayList;

public interface Game {
    void move (Color color, int idGameComponent);
    void drawStudents(GameComponent gameComponent, byte number);
    void playCard(byte card) throws GameException;
    Player getCurrentPlayer();
    void moveMotherNature(int moves) throws NotAllowedException;
    void nextPlayer();
    boolean getPhase();
    void nextPhase();
    void calculateInfluence(Island island);
    void calculateProfessor();
    void endGame();
    void endGame(Team team);
    void nextActionPhase();
    void refillClouds();
    void setLastRound();
    void setCharacterInput(int input) throws NotExpertGameException;
    void chooseCharacter(int indexCharacter) throws NotExpertGameException;
    void playCharacter() throws NotExpertGameException;
    void checkMerge(Island island);
    ArrayList<Player> getPlayers();
    ArrayList<Team> getTeams();
    GameComponent getIsland(int index);
    
     Player [] getProfessor();
}
