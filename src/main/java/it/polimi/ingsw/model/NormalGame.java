package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameDelta;
import it.polimi.ingsw.controller.GameListener;
import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.NotExpertGameException;

import java.util.ArrayList;

public class NormalGame implements Game {
    private final GameDelta gameDelta;
    private final ArrayList<Island> islands;
    private final ArrayList<Cloud> clouds;
    //professors are handled as a 5 player array: professors[i]=j means that professor i (it follows the ordinal of enum) is
    //controlled by the player j
    private final Wizard[] professors;
    private final ArrayList<Team> teams;

    //a reference to all the pieces contained in the game
    private transient Bag bag;
    private byte motherNaturePosition;
    private byte currentPlayer;


    public NormalGame(ArrayList<Team> teamList, GameListener listener) {
        gameDelta = getNewGameDelta(listener);
        this.teams = new ArrayList<>(teamList);
        byte numberOfPlayers = this.getPlayerSize();

        this.islands = new ArrayList<>(12);
        for (byte i = 2; i < 14; i++) {
            islands.add(new Island(i));
        }

        this.clouds = new ArrayList<>(numberOfPlayers);
        for (byte i = -1; i >= -numberOfPlayers; i--) {
            clouds.add(new Cloud(numberOfPlayers, i));
        }

        this.professors = new Wizard[Color.values().length];

        this.bag = new Bag((byte) 2, (byte) 69);
        initializeMotherNature();

        try {
            refillClouds();
            for (Team t : teams)
                for (Player p : t.getPlayers())
                    drawStudents(p.getEntranceHall(), (byte) p.getEntranceHall().getMaxStudents());
        } catch (EndGameException e) {
            e.printStackTrace();
        }
    }

    protected GameDelta getNewGameDelta(GameListener listener) {
        return new GameDelta(listener);
    }
    // checks if the islands before and after the selected island have the same team and in case merges them

    private void initializeMotherNature() {
        this.motherNaturePosition = 0;
        for (int i = 0; i < islands.size(); i++) {
            if (!(i == 0 || i == (6) % 12)) {
                try {
                    drawStudents(islands.get(i), (byte) 1);
                } catch (EndGameException e) {
                    System.out.println("Initialized islands");
                }
            }
        }
        this.bag = new Bag((byte) 24, (byte) 69);
    }

    protected void checkMerge(Island island) throws EndGameException {
        int islandBeforeIndex = Math.floorMod(islands.indexOf(island) - 1, islands.size());
        int islandAfterIndex = (islands.indexOf(island) + 1) % islands.size();

        Island islandBefore = islands.get(islandBeforeIndex);
        Island islandAfter = islands.get(islandAfterIndex);

        if (islandBefore.getTeamColor() != null && islandBefore.getTeamColor().equals(island.getTeamColor())) {

            island.merge(islandBefore);
            islands.remove(islandBeforeIndex);

            if (islandBeforeIndex < motherNaturePosition) motherNaturePosition--;

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            gameDelta.addDeletedIslands(islandBefore);

            islandAfterIndex--;
        }
        if (islandAfter.getTeamColor() != null && islandAfter.getTeamColor().equals(island.getTeamColor())) {
            island.merge(islandAfter);
            islands.remove(islandAfterIndex);

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            gameDelta.addDeletedIslands(islandAfter);

            if (islandAfterIndex <= motherNaturePosition) motherNaturePosition--;
        }
        if (motherNaturePosition < 0) motherNaturePosition = 0;

        if (islands.size() <= 3) throw new EndGameException(true);

    }

    protected void drawStudents(GameComponent gameComponent, byte students) throws EndGameException {
        try {
            bag.drawStudent(gameComponent, students);

            // add to game delta
            gameDelta.addUpdatedGC(gameComponent);
        } catch (GameException ignored) {
        }
    }

    @Override
    public void refillClouds() throws EndGameException {
        for (GameComponent cloud : this.clouds) {
            // draw 3 if 2 teams, draws 4 if 3 teams
            drawStudents(cloud, (byte) (teams.size() % 2 + 3));
        }
    }

    @Override
    public void playCard(byte card) throws GameException, EndGameException {
        getCurrentPlayer().useCard(card);

        // add to game delta
        gameDelta.setPlayedCard(card);
    }

    private GameComponent getComponentByIndex(int gameComponentIndex) throws NotAllowedException {
        //0 is for the entranceHall, 1 is for the LunchHall, negative numbers are for clouds, and from 2 is islands.
        //for island 2->0; 3->1;4->2 .....
        if (gameComponentIndex < (-clouds.size()) || gameComponentIndex >= (islands.size() + 2)) {
            throw new NotAllowedException("gameComponentIndex not valid");
        }
        if (gameComponentIndex < 0) return clouds.get(-gameComponentIndex - 1);
        if (gameComponentIndex == 0) return getCurrentPlayer().getEntranceHall();
        if (gameComponentIndex == 1) return getCurrentPlayer().getLunchHall();
        return islands.get(gameComponentIndex - 2);
    }


    @Override
    public void move(Color color, int gameComponentSource, int gameComponentDestination) throws GameException {
        GameComponent source = getComponentByIndex(gameComponentSource), destination = getComponentByIndex(gameComponentDestination);
        source.moveStudents(color, (byte) 1, destination);

        // add to game delta
        gameDelta.addUpdatedGC(source);
        gameDelta.addUpdatedGC(destination);

        if (gameComponentDestination == 1) calculateProfessor();
    }

    // compares for each color the lunchHall of each player and then puts the player with the most students
    // in the professor array slot of the current color
    protected void calculateProfessor() {
        byte max;
        Player currentOwner;
        // player with the maximum number of students for the current color
        Player newOwner;
        for (Color c : Color.values()) {
            currentOwner = null;
            // player actually controlling that professor
            if (getProfessor()[c.ordinal()] != null)
                currentOwner = getPlayer((byte) getProfessor()[c.ordinal()].ordinal());

            if (currentOwner != null) max = currentOwner.getLunchHall().howManyStudents(c);
            else max = 0;
            newOwner = currentOwner;

            for (Player p : getPlayers()) {
                if (!p.equals(currentOwner) && p.getLunchHall().howManyStudents(c) > max) {
                    max = p.getLunchHall().howManyStudents(c);
                    newOwner = p;
                }
            }
            if (newOwner != null && !newOwner.equals(currentOwner)) {
                professors[c.ordinal()] = newOwner.getWizard();

                // add to game delta
                gameDelta.addUpdatedProfessors(c, newOwner.getWizard());
            }
        }
    }

    protected boolean checkMoveMotherNature(int moves) {
        return moves <= getCurrentPlayer().getPlayedCardMoves();
    }

    @Override
    public void moveMotherNature(int moves) throws NotAllowedException, EndGameException {
        if (!checkMoveMotherNature(moves))
            throw new NotAllowedException("Moves can't be higher than the value of the card");
        motherNaturePosition += moves;
        motherNaturePosition %= islands.size();

        // add to game delta
        gameDelta.setNewMotherNaturePosition(motherNaturePosition);

        calculateInfluence(islands.get(motherNaturePosition));
    }

    private void calculateInfluence(Island island) throws EndGameException {
        HouseColor oldController = island.getTeamColor();
        int maxInfluence = 0;
        HouseColor winnerColor = null;
        for (Team t : teams) {
            int influence = 0;
            for (Color c : Color.values()) {
                for (Player p : t.getPlayers()) {
                    if (p.getWizard().equals(professors[c.ordinal()])) influence += island.howManyStudents(c);
                }
            }
            if (island.getTeamColor() != null && t.getHouseColor() == island.getTeamColor())
                influence += island.getNumber();

            if (influence > maxInfluence) {
                winnerColor = t.getHouseColor();
                maxInfluence = influence;
            } else if (influence == maxInfluence) {
                winnerColor = oldController;
            }

        }

        HouseColor oldTeamColor = island.getTeamColor();
        if (winnerColor != null && !winnerColor.equals(oldTeamColor)) {
            island.setTeamColor(winnerColor);

            // add to game delta
            gameDelta.addUpdatedGC(island);

            if (oldTeamColor != null) {
                try {
                    Team oldTeam = getTeams().get(oldTeamColor.ordinal());
                    oldTeam.addTowers(island.getNumber());

                    // add to game delta
                    gameDelta.updateTeamTowersLeft(oldTeamColor, oldTeam.getTowersLeft());
                } catch (NotAllowedException ignored) {
                }
            }
            Team winnerTeam = getTeams().get(winnerColor.ordinal());
            winnerTeam.removeTowers(island.getNumber());

            // add to game delta
            gameDelta.updateTeamTowersLeft(winnerColor, winnerTeam.getTowersLeft());

            checkMerge(island);
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
                        if (p.getWizard() == professors[c.ordinal()]) numberOfProfessors++;
                    }
                }
                maxProfessors = numberOfProfessors;
            } else if (t.getTowersLeft() == minTowers) {
                byte numberOfProfessors = 0;
                for (Color c : Color.values()) {
                    for (Player p : t.getPlayers()) {
                        if (p.getWizard() == professors[c.ordinal()]) numberOfProfessors++;
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
    public void setCharacterInput(int input) throws GameException {
        throw new NotExpertGameException();
    }

    @Override
    public void chooseCharacter(byte index) throws GameException {
        throw new NotExpertGameException();
    }

    @Override
    public void playCharacter() throws GameException, EndGameException {
        throw new NotExpertGameException();
    }

    @Override
    public void moveFromCloud(int cloudId) throws NotAllowedException {
        if (cloudId >= 0 || cloudId < -getClouds().size() || getComponentByIndex(cloudId).howManyStudents() == 0)
            throw new NotAllowedException("Can't move from the selected cloud");
        GameComponent cloudSource = getComponentByIndex(cloudId);
        cloudSource.moveAll(getCurrentPlayer().getEntranceHall());

        // add to game delta
        gameDelta.addUpdatedGC(cloudSource);
        gameDelta.addUpdatedGC(getCurrentPlayer().getEntranceHall());
    }

    public ArrayList<Cloud> getClouds() {
        //TODO do the clone
        return clouds;
    }

    protected Player getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    protected byte getCurrentPlayerIndex() {
        return currentPlayer;
    }

    // TODO: pass by copy player, team etc..

    @Override
    public void setCurrentPlayer(Player p) {
        byte newCurrentPlayer = (byte) p.getWizard().ordinal();
        if (newCurrentPlayer != this.currentPlayer) {
            this.currentPlayer = newCurrentPlayer;

            // add to game delta
            gameDelta.setNewCurrentPlayer(currentPlayer);
        }
    }

    @Override
    public void setCurrentPlayer(byte currentPlayer) {
        if (currentPlayer != this.currentPlayer) {
            this.currentPlayer = currentPlayer;

            // add to game delta
            gameDelta.setNewCurrentPlayer(currentPlayer);
        }
    }

    protected Player getPlayer(byte b) {
        // teams index = b % team size (in 4 players game, players are inserted in team 0, indexOf1, 0, 1)
        // player index (in team members) = b / team size (2 or 3 players -> = 0 (team contains only 1 member), 4 players -> first 2 = 0, last 2 = 1 (3rd player is in team 0 and is member[1]))
        return teams.get(b % teams.size()).getPlayers().get(b / teams.size());
    }

    protected ArrayList<Player> getPlayers() {
        ArrayList<Player> ret = new ArrayList<>(getPlayerSize());
        for (byte i = 0; i < getPlayerSize() / teams.size(); i++)
            for (Team t : teams)
                ret.add(t.getPlayers().get(i));
        return ret;
    }

    @Override
    public GameDelta getAndClearGameDelta() {
        GameDelta copy = getGameDelta();
        gameDelta.clear();
        return copy;
    }

    protected GameDelta getGameDelta() {
        return this.gameDelta;
    }

    protected byte getPlayerSize() {
        return (byte) (teams.size() * teams.get(0).getPlayers().size());
    }

    protected ArrayList<Team> getTeams() {
        return teams;
    }

    protected ArrayList<Island> getIslands() {
        return islands;
    }

    protected Bag getBag() {
        return this.bag;
    }

    protected Wizard[] getProfessor() {
        return professors;
    }

    public byte getMotherNaturePosition() {
        return motherNaturePosition;
    }
}
