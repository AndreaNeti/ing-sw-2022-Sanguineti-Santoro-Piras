package it.polimi.ingsw.model;

import java.util.*;

public class Game {
    private Player[] professors;
    private ArrayList<Player> players;
    private ArrayList<Island> islands;
    private byte motherNaturePosition;
    private Player currentPlayer;
    private boolean currentPhase;
    private byte currentActionPhase;
    private byte roundIndex;


    public Game(byte numberOfPlayers, List<Wizard> wizards, List<String> nicknames){
        Random rand = new Random(System.currentTimeMillis());
        this.professors = new Player[5];
        this.players = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(wizards.get(i), (byte) (numberOfPlayers == 3 ? 6 : 8), nicknames.get(i)));
        }
        this.islands = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }
        this.motherNaturePosition = (byte) rand.nextInt(12);
        this.currentPlayer = players.get(rand.nextInt(numberOfPlayers));
        this.currentPhase = false;
        this.currentActionPhase = 0;
        this.roundIndex = 0;
    }

    private void checkMerge(Island island){}


    public void drawStudents(GameComponent gameComponent, byte students){}
    public void moveMotherNature(int moves){
        motherNaturePosition += moves;
        motherNaturePosition %= islands.size();
    }
    public void playCard(Player player,Card card){
        //function to play card
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
