package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NormalGame implements Game {
    private final ArrayList<Island> islands;
    private final ArrayList<Cloud> clouds;
    //professors are handled as a 5 player array: professors[i]=j means that professor i (it follows the ordinal of enum) is
    //controlled by the player j
    private final Player[] professors;
    private final ArrayList<Team> teams;
    //all players
    private final ArrayList<Player> players;
    //a reference to all the pieces contained in the game
    private Bag bag;
    private byte motherNaturePosition;
    private Player currentPlayer;


    public NormalGame(byte numberOfPlayers, ArrayList<Team> teamList, ArrayList<Player> playerList) {
        Random rand = new Random(System.currentTimeMillis());
        this.islands = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            islands.add(new Island());
        }
        this.clouds = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            clouds.add(new Cloud(numberOfPlayers));
        }
        this.professors = new Player[Color.values().length];
        this.teams = new ArrayList<>(numberOfPlayers == 3 ? 3 : 2);
        this.players = new ArrayList<>(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(playerList.get(i));
        }
        for (int i = 0; i < (numberOfPlayers == 3 ? 3 : 2); i++) {
            teams.add(teamList.get(i));
        }
        this.bag = new Bag((byte) 2);
        initializeMotherNature((byte) rand.nextInt(islands.size()));

        try {
            refillClouds();
            for (Player p : playerList)
                drawStudents(p.getEntranceHall(), (byte) p.getEntranceHall().getMaxStudents());
        } catch (EndGameException e) {
            e.printStackTrace();
        }
    }

    // checks if the islands before and after the selected island have the same team and in case merges them

    private void initializeMotherNature(byte index) {
        this.motherNaturePosition = index;
        for (int i = 0; i < islands.size(); i++) {
            if (!(i == index || i == (index + 6) % 12)) {
                try {
                    drawStudents(islands.get(i), (byte) 1);
                } catch (EndGameException e) {
                    System.out.println("Initialized islands");
                }
            }
        }
        this.bag = new Bag((byte) 24);
    }

    protected void checkMerge(Island island) throws EndGameException {
        int islandBeforeIndex = (islands.indexOf(island) - 1) % islands.size();
        int islandAfterIndex = (islands.indexOf(island) + 1) % islands.size();

        Island islandBefore = islands.get(islandBeforeIndex);
        Island islandAfter = islands.get(islandAfterIndex);

        if (islandBefore.getTeam().equals(island.getTeam())) {
            island.merge(islandBefore);
            islands.remove(islandBefore);
            if (islandBeforeIndex < motherNaturePosition)
                motherNaturePosition--;
        }
        if (islandAfter.getTeam().equals(island.getTeam())) {
            island.merge(islandAfter);
            islands.remove(islandAfter);
            if (islandAfterIndex <= motherNaturePosition)
                motherNaturePosition--;
        }
        if (motherNaturePosition < 0) motherNaturePosition = 0;
        if (islands.size() <= 3)
            throw new EndGameException(true);

    }

    private GameComponent getComponentById(int idGameComponent) throws NotAllowedException {
        //0 is for the entranceHall, 1 is for the LunchHall, negative numbers are for clouds, and from 2 is islands.
        if (idGameComponent < (-clouds.size()) || idGameComponent >= (islands.size() + 2)) {
            throw new NotAllowedException("idGameComponent not valid");
        }
        if (idGameComponent < 0)
            return clouds.get(-idGameComponent - 1);
        if (idGameComponent == 0)
            return currentPlayer.getEntranceHall();
        if (idGameComponent == 1)
            return currentPlayer.getLunchHall();
        return islands.get(idGameComponent - 2);
    }

    @Override
    public void move(Color color, int gameComponentSource, int gameComponentDestination) throws GameException {
        getComponentById(gameComponentSource).moveStudents(color, (byte) 1, getComponentById(gameComponentDestination));
        if (gameComponentDestination == 1)
            calculateProfessor();
    }

    protected void drawStudents(GameComponent gameComponent, byte students) throws EndGameException {
        try {
            bag.drawStudent(gameComponent, students);
        } catch (UnexpectedValueException e) {
            System.err.println(e.getErrorMessage());
        }
    }

    @Override
    public void playCard(byte card) throws GameException, EndGameException {
        currentPlayer.useCard(card);
    }

    @Override
    public void setCurrentPlayer(Player p) {
        this.currentPlayer = p;
    }

    @Override
    public void moveMotherNature(int moves) throws NotAllowedException, EndGameException {
        if (moves > currentPlayer.getPlayedCardMoves())
            throw new NotAllowedException("Moves can't be higher than the value of the card");
        else {
            motherNaturePosition += moves;
            motherNaturePosition %= islands.size();
            calculateInfluence(islands.get(motherNaturePosition));
        }
    }

    private void calculateInfluence(Island island) throws EndGameException {
        Team oldController = island.getTeam();
        int maxInfluence = 0;
        Team winner = null;
        for (Team t : teams) {
            int influence = 0;
            for (Color c : Color.values()) {
                for (Player p : t.getPlayers()) {
                    if (p.equals(professors[c.ordinal()])) influence += island.howManyStudents(c);
                }
            }
            if (island.getTeam() != null && t.equals(island.getTeam())) influence += island.getNumber();

            if (influence > maxInfluence) {
                winner = t;
                maxInfluence = influence;
            } else if (influence == maxInfluence) {
                winner = oldController;
            }

        }

        Team oldTeam = island.getTeam();
        if (oldTeam != null && winner != null && !oldTeam.equals(winner)) {
            island.setTeam(winner);
            try {
                oldTeam.addTowers(island.getNumber());
            } catch (NotAllowedException ex) {
                System.err.println(ex.getErrorMessage());
            }
            winner.removeTowers(island.getNumber());
            checkMerge(island);
        }
    }


    // compares for each color the lunchHall of each player and then puts the player with the most students
    // in the professor array slot of the current color
    protected void calculateProfessor() {
        byte max;
        Player currentOwner;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            // player actually controlling that professor
            currentOwner = professors[c.ordinal()];

            if (currentOwner != null)
                max = currentOwner.getLunchHall().howManyStudents(c);
            else max = 0;
            newOwner = currentOwner;

            for (Player p : players) {
                if (!p.equals(currentOwner) && p.getLunchHall().howManyStudents(c) > max) {
                    max = p.getLunchHall().howManyStudents(c);
                    newOwner = p;
                }
            }
            professors[c.ordinal()] = newOwner;
        }
    }

    // checks the team with fewer towers left and calculates its number of controlled professors.
    // if two teams have the same number of towers left the team with more professors controlled becomes the winner
    @Override
    public ArrayList<Team> calculateWinner() {
        ArrayList<Team> winners = new ArrayList<>();
        byte minTowers = 8;
        byte maxProfessors = 0;
        for (Team t : teams) {
            if (t.getTowersLeft() < minTowers) {
                winners.clear();
                winners.add(t);
                byte numberOfProfessors = 0;
                minTowers = t.getTowersLeft();
                for (Color c : Color.values()) {
                    for (Player p : t.getPlayers()) {
                        if (p.equals(professors[c.ordinal()])) numberOfProfessors++;
                    }
                }
                maxProfessors = numberOfProfessors;
            } else if (t.getTowersLeft() == minTowers) {
                byte numberOfProfessors = 0;
                for (Color c : Color.values()) {
                    for (Player p : t.getPlayers()) {
                        if (p.equals(professors[c.ordinal()])) numberOfProfessors++;
                    }
                }
                if (numberOfProfessors > maxProfessors) {
                    winners.clear();
                    winners.add(t);
                    maxProfessors = numberOfProfessors;
                } else if (numberOfProfessors == maxProfessors) {
                    winners.add(t);
                }
            }
        }
        return winners;
    }

    @Override
    public void refillClouds() throws EndGameException {
        for (GameComponent cloud : this.clouds) {
            // draw 3 if 2 teams, draws 4 if 3 teams
            drawStudents(cloud, (byte) (teams.size() % 2 + 3));
        }
    }

    public void setCharacterInput(int input) throws GameException {
        throw new NotExpertGameException();
    }

    public void chooseCharacter(int index) throws GameException {
        throw new NotExpertGameException();
    }

    public void playCharacter() throws GameException, EndGameException {
        throw new NotExpertGameException();
    }

    @Override
    public void moveFromCloud(int cloudId) throws NotAllowedException {
        if (cloudId >= 0 || getComponentById(cloudId).howManyStudents() == 0)
            throw new NotAllowedException("Can't move from the selected cloud");
        GameComponent cloudSource = getComponentById(cloudId);
        cloudSource.moveAll(currentPlayer.getEntranceHall());
    }

    // TODO: pass by copy player, team etc..

    protected Player getCurrentPlayer() { return this.currentPlayer; }

    protected ArrayList<Player> getPlayers() {
        return players;
    }

    protected ArrayList<Team> getTeams() { return teams; }

    protected ArrayList<Island> getIslands() {
        return islands;
    }

    protected GameComponent getBag() {
        return this.bag;
    }

    protected Player[] getProfessor() {
        return professors;
    }

}
