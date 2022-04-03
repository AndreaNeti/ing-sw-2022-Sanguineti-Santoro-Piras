package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;

public interface Game {
    void move (Color color, int idGameComponent, byte actionPhase) throws UnexpectedValueException, NotEnoughStudentsException;
    void drawStudents(GameComponent gameComponent, byte number) throws EndGameException;
    void playCard(byte card) throws GameException, EndGameException;
    void setCurrentPlayer(Player p);
    Player getCurrentPlayer();
    void moveMotherNature(int moves) throws NotAllowedException, EndGameException;
    void calculateInfluence(Island island) throws EndGameException;
    void calculateProfessor();
    Team calculateWinner();
    void refillClouds() throws EndGameException;
    void setCharacterInput(int input) throws GameException;
    void chooseCharacter(int indexCharacter) throws NotExpertGameException, UnexpectedValueException, NotAllowedException;
    void playCharacter() throws GameException, EndGameException;
    void checkMerge(Island island) throws EndGameException;
    ArrayList<Player> getPlayers();
    ArrayList<Team> getTeams();
    ArrayList<Island> getIslands();
    void initializeMotherNature(byte index);
    GameComponent getBag();
    Player [] getProfessor();
}
