package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.NotEnoughStudentsException;
import it.polimi.ingsw.exceptions.NotExpertGameException;

import java.util.*;

public class NormalGame implements Game{
    //a reference to all the pieces contained in the game
    private final Bag bag;
    private final ArrayList<GameComponent> islands;
    private final ArrayList<GameComponent> clouds;
    //professors are handled as a 5 player array: professors[i]=j means that professor i (it follows the ordinal of enum) is
    //controlled by the player j
    private final Player[] professors;
    private final ArrayList<Team> teams;
    //all players
    private final ArrayList<Player> players;
    // This array is also used to represent the order of round
    private final ArrayList<Byte> playerOrder;
    private byte motherNaturePosition;
    private Player currentPlayer;
    //current phase is true in the planification phase, false during the action phase
    private boolean currentPhase;
    private byte numberOfPlayers;
    /*it's a number that goes from 1 to 3 and it represent the sub-section of the actione phase
    1-move 3 students; 2-move mother nature(calculate influence and merge); 3-drawstudent from cloud*/
    private byte currentActionPhase;
    //it's the index of playerOrder: it goes from 0 to players.size() and when it's 3 it changes phase
    private byte roundIndex;
    private boolean lastRound;

    public NormalGame(byte numberOfPlayers, List<Wizard> wizards, List<String> nicknames) {
        Random rand = new Random(System.currentTimeMillis());
        this.bag = new Bag();
        this.islands = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }

        this.clouds = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            clouds.add(new Cloud(numberOfPlayers));
        }

        this.professors = new Player[5];
        this.teams = new ArrayList<Team>(numberOfPlayers == 3 ? 3 : 2);

        this.players = new ArrayList<>(numberOfPlayers);
        this.playerOrder = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(wizards.get(i), (byte) (numberOfPlayers == 3 ? 6 : 8), nicknames.get(i)));
            playerOrder.add((byte) i);
        }

        this.motherNaturePosition = (byte) rand.nextInt(12);
        this.currentPlayer = players.get(rand.nextInt(numberOfPlayers));
        this.currentPhase = false;
        this.currentActionPhase = 0;
        this.roundIndex = 0;
        this.lastRound = false;
    }

    private void checkMerge(Island island) {

    }

    public void move(Color color, int idGameComponent) {

    }

    public void drawStudents(GameComponent gameComponent, byte students) {
    }



    public void playCard(byte card) {
        //TODO function to play card

    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void moveMotherNature(int moves) {
        motherNaturePosition += moves;
        motherNaturePosition %= islands.size();
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

    public boolean getPhase() {
        return currentPhase;
    }

    //sort the array if the nextPhase is the action phase, anyway set the current player to the first of the array
    public void nextPhase() {
        this.currentPhase = !currentPhase;
        if (!currentPhase) {
            playerOrder.sort(Comparator.comparingInt((Byte b) -> players.get(b).getPlayedCard().getValue()));
        }
        currentPlayer = players.get(playerOrder.get(0));
    }

    public void calculateInfluence(Island island) {
        if(island.getProhibition()) {
            island.setProhibition(false);
        } else {

            for (Color c: Color.values()) {
                island.getStudentSize();
            }
        }
    }

    public void calculateInfluence() {
        calculateInfluence((Island) islands.get(motherNaturePosition));
    }

    // compares for each color the lunchhall of each player and then puts the player with the most students
    // in the professor array slot of the current color
    public void calculateProfessor() {
        byte max;
        // player with the maximum number of students for the current color
        Player maxP;
        for (Color c: Color.values()) {
            max = 0;
            maxP = null;
            for (Player p: players) {
                if(p.getLunchHall().getStudentSize(c) > max) {
                    max = p.getLunchHall().getStudentSize(c);
                    maxP = p;
                }
                if(maxP != null) {
                    professors[c.ordinal()] = maxP;
                }
            }
        }
    }

    public void endGame() {
    }

    public void endGame(Team team) {
    }

    public void nextActionPhase() {
        this.currentActionPhase = (byte) ((this.currentActionPhase + 1) % 3 + 1);
    }

    public void refillClouds() {

    }

    public void setLastRound() {
        this.lastRound = true;
    }

    public void setCharacterInput(int input) throws NotExpertGameException {
        throw new NotExpertGameException();
    }

    public void chooseCharacter(int index) throws NotExpertGameException {
        throw new NotExpertGameException();
    }

    public void playCharacter() throws NotExpertGameException{
        throw new NotExpertGameException();
    }

}
