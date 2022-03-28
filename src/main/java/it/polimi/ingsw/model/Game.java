package it.polimi.ingsw.model;

import java.util.*;

public class Game {
    //professors are handled as a 5 player array: professors[i]=j means that professor i (it follows the ordinal of enum) is
    //controlled by the player j
    private Player[] professors;
    //all players
    private ArrayList<Player> players;
    // This array is also used to represent the order of round
    private ArrayList<Byte> playerOrder;


    private Player currentPlayer;
    //current phase is true in the planification phase, false during the action phase
    private boolean currentPhase;
    /*it's a number that goes from 1 to 3 and it represent the sub-section of the actione phase
    1-move 3 students; 2-move mother nature(calculate influence and merge); 3-drawstudent from cloud*/
    private byte currentActionPhase;
    //it's the index of playerOrder: it goes from 0 to players.size() and when it's 3 it changes phase
    private byte roundIndex;
    private byte motherNaturePosition;

    //a reference to all the pieces contained in the game
    private ArrayList<GameComponent> islands;
    private GameComponent bag;
    private ArrayList<GameComponent> clouds;

    public Game(byte numberOfPlayers, List<Wizard> wizards, List<String> nicknames) {
        Random rand = new Random(System.currentTimeMillis());
        this.professors = new Player[5];

        this.playerOrder = new ArrayList<>(numberOfPlayers);
        this.players = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(wizards.get(i), (byte) (numberOfPlayers == 3 ? 6 : 8), nicknames.get(i)));
            playerOrder.add((byte) i);
        }

        this.islands = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }

        this.bag = new Bag();

        this.clouds = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++)
            clouds.add(new Cloud(numberOfPlayers));

        this.motherNaturePosition = (byte) rand.nextInt(12);
        this.currentPlayer = players.get(rand.nextInt(numberOfPlayers));
        this.currentPhase = false;
        this.currentActionPhase = 0;

    }

    private void checkMerge(Island island) {
    }


    public void drawStudents(GameComponent gameComponent, byte students) {
    }

    public void moveMotherNature(int moves) {
        motherNaturePosition += moves;
        motherNaturePosition %= islands.size();
    }

    public void playCard(Player player, Card card) {
        //function to play card
    }

    public void nextPlayer() {
        if (currentPhase) {

            int index = (currentPlayer.getWizard().ordinal() + 1) % players.size();
            for (Wizard w : Wizard.values()) {
                if (w.ordinal() == index) {
                    for (Player p : players) {
                        if (p.getWizard().equals(w))
                            currentPlayer = p;
                    }
                }

            }
        } else {
            currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());

        }
    }

    public void nextActionPhase() {
        this.currentActionPhase = (byte) ((this.currentActionPhase + 1) % 3 + 1);

    }

    //sort the array if the nextPhase is the action phase, anyway set the current player to the first of the array
    public void nextPhase() {
        this.currentPhase = !currentPhase;
        if (!currentPhase) {
            playerOrder.sort(Comparator.comparingInt((Byte b) -> players.get(b).getPlayedCard().getValue()));
        }
        currentPlayer = players.get(playerOrder.get(0));
    }

    public Player getCurrentPlayer() {

        return currentPlayer;
    }

    public boolean getPhase() {


        return currentPhase;
    }


    public Player calculateInfluence(Island island) {
        return null;
    }

    public Player calculateInfluence() {
        return calculateInfluence((Island) islands.get(motherNaturePosition));
    }

    public void calculateProfessor(boolean characterEffect) {
    }

    public void endGame() {
    }

    public void endGame(Player player) {
    }


}
