package it.polimi.ingsw;

import java.util.ArrayList;

public class Game {
    static Game game;
    private Player[] professors;
    private ArrayList<Island> islands;
    private byte motherNaturePosition;
    private Player currentPlayer;
    private byte numberOfPlayers;
    private boolean currentPhase;
    private byte currentActionPhase;
    private byte roundIndex;

    private void checkMerge(Island island){}


    public void drawStudents(GameComponent gameComponent, byte students){}
    public void playCard(Player player,Card card){}
    public void moveMotherNature(int moves){
        motherNaturePosition += moves;
        motherNaturePosition %= islands.size();
    }
    public void nextPlayer() {}
    public void nextActionPhase(){}
    public void nextPhase(){}
    public Player calculateInfluence(Island island){ return null;}
    public Player calculateInfluence (){return null;}
    public void calculateProfessor(boolean characterEffect){}
    public void endGame(){}
    public void endGame(Player player){}



    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean getPhase(){


        return currentPhase;
    }


}
