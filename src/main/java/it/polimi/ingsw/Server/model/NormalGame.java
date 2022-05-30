package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.GameComponents.Bag;
import it.polimi.ingsw.Server.model.GameComponents.Cloud;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotExpertGameException;

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
    private Bag bag;
    private byte motherNaturePosition;
    private byte currentPlayer;
    private final MatchConstants matchConstants;

    public NormalGame(ArrayList<Team> teamList, MatchConstants matchConstants) {
        if (teamList == null || matchConstants == null) throw new IllegalArgumentException("Passing null parameter");
        if (teamList.size() < 2) throw new IllegalArgumentException("Cannot initialize a game with less than 2 teams");
        this.matchConstants = matchConstants;
        gameDelta = getNewGameDelta();
        this.teams = new ArrayList<>(teamList);
        byte numberOfPlayers = this.getPlayerSize();

        this.islands = new ArrayList<>(12);
        for (byte i = (byte) (2 * numberOfPlayers); i < (byte) (2 * numberOfPlayers + 12); i++) {
            islands.add(new Island(i));
        }

        this.clouds = new ArrayList<>(numberOfPlayers);
        for (byte i = -1; i >= -numberOfPlayers; i--) {
            clouds.add(new Cloud(matchConstants.studentsToMove(), i));
        }

        this.professors = new Wizard[Color.values().length];

        this.bag = new Bag((byte) 2);
        initializeMotherNature();

        try {
            refillClouds();
            for (Team t : teams)
                for (Player p : t.getPlayers())
                    drawStudents(p.getEntranceHall(), (byte) p.getEntranceHall().getMaxStudents());
        } catch (EndGameException | GameException e) {
            e.printStackTrace();
        }
    }

    protected GameDelta getNewGameDelta() {
        return new GameDelta();
    }


    private void initializeMotherNature() {
        this.motherNaturePosition = 0;
        for (int i = 0; i < islands.size(); i++) {
            if (!(i == 0 || i == (6) % 12)) {
                try {
                    drawStudents(islands.get(i), (byte) 1);
                } catch (GameException | EndGameException ignored) {
                }
            }
        }
        this.bag = new Bag((byte) 24);
    }

    // checks if the islands before and after the selected island have the same team and in case merges them
    protected void checkMerge(Island island) throws EndGameException {
        if (island == null) throw new IllegalArgumentException("Passing null island");
        int islandBeforeIndex = Math.floorMod(islands.indexOf(island) - 1, islands.size());
        int islandAfterIndex = (islands.indexOf(island) + 1) % islands.size();

        Island islandBefore = islands.get(islandBeforeIndex);
        Island islandAfter = islands.get(islandAfterIndex);

        if (islandBefore.getTeamColor() != null && islandBefore.getTeamColor().equals(island.getTeamColor())) {

            island.merge(islandBefore);
            islands.remove(islandBefore);

            if (islandBeforeIndex < motherNaturePosition) {
                motherNaturePosition--;
                islandAfterIndex--;
            }

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            gameDelta.addDeletedIslands(islandBefore);
        }
        if (islandAfter.getTeamColor() != null && islandAfter.getTeamColor().equals(island.getTeamColor())) {
            island.merge(islandAfter);
            islands.remove(islandAfter);

            if (islandAfterIndex < motherNaturePosition) motherNaturePosition--;

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            gameDelta.addDeletedIslands(islandAfter);

        }
        if (islands.size() <= matchConstants.minIslands()) {
            gameDelta.send();
            throw new EndGameException(true);
        }
    }

    protected void drawStudents(GameComponent gameComponent, byte students) throws EndGameException, GameException {
        if (gameComponent == null) throw new IllegalArgumentException("Drawing students to null gameComponent");
        try {
            bag.drawStudent(gameComponent, students);
        } finally {
            // add to game delta
            gameDelta.addUpdatedGC(gameComponent);
        }
    }

    @Override
    public void refillClouds() throws EndGameException {
        try {
            for (GameComponent cloud : this.clouds) {
                // draw 3 if 2 teams, draws 4 if 3 teams
                try {
                    drawStudents(cloud, (byte) (teams.size() % 2 + 3));
                } catch (GameException e) {
                    System.err.println("Refilling cloud over its limit?");
                }
            }
        } finally {
            getGameDelta().send();
        }
    }

    @Override
    public void playCard(AssistantCard card) throws GameException, EndGameException {
        if (card.value() < 1 || card.value() > matchConstants.numOfCards())
            throw new IllegalArgumentException("Not a valid card to play");
        try {
            getCurrentPlayer().useCard(card);
        } finally {
            // set in delta also if it's the last one played
            gameDelta.setPlayedCard(card);
            getGameDelta().send();
        }
    }

    protected GameComponent getComponentById(int idGameComponent) throws GameException {
        /*here the id is static
        from 0 to 2*numberOfPlayer-1 is entranceHall,LunchHall
        from 2*numberOfPlayer to 2*numberOfPlayer+12 are the island
        from -1 to -4 are clouds;
        from -10 to -12 are the characters, here I ignore this because you can never move to this component
        */
        if (idGameComponent == 69)
            return bag;

        if (idGameComponent < (-clouds.size()) || idGameComponent >= (2 * getPlayerSize() + 12)) {
            throw new NotAllowedException(idGameComponent + " is not a valid gameComponent id");
        }
        if (idGameComponent < 0) {
            return clouds.get(-idGameComponent - 1);
        }
        if (idGameComponent >= 2 * getPlayerSize()) {
            Island islandToReturn = null;
            for (Island island : islands) {
                if (island.getId() == idGameComponent) {
                    islandToReturn = island;
                    break;
                }
            }
            if (islandToReturn == null)
                throw new NotAllowedException("gameComponent id not valid, island non exists");
            else
                return islandToReturn;
        }
        if (idGameComponent < 2 * getPlayerSize()) {
            Player p = getPlayer((byte) (idGameComponent / 2));
            if (idGameComponent % 2 == 0)
                return p.getEntranceHall();
            else return p.getLunchHall();
        }
        throw new NotAllowedException("Not a valid gameComponent id");
    }


    @Override
    public void move(Color color, int idGameComponentSource, int idGameComponentDestination) throws GameException {
        if (color == null) throw new IllegalArgumentException("Null color");
        try {
            GameComponent source = getComponentById(idGameComponentSource), destination = getComponentById(idGameComponentDestination);
            source.moveStudents(color, (byte) 1, destination);

            // add to game delta
            gameDelta.addUpdatedGC(source);
            gameDelta.addUpdatedGC(destination);

            if (idGameComponentDestination < 2 * getPlayerSize() && idGameComponentDestination % 2 == 1)
                calculateProfessor();
        } finally {
            getGameDelta().send();
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
        return moves <= getCurrentPlayer().getPlayedCard().moves();
    }

    @Override
    public void moveMotherNature(int moves) throws NotAllowedException, EndGameException {
        if (moves < 0) throw new IllegalArgumentException("Cannot move backwards");
        if (!checkMoveMotherNature(moves))
            throw new NotAllowedException("Moves can't be higher than the value of the card");
        try {
            motherNaturePosition += moves;
            motherNaturePosition %= islands.size();

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);

            calculateInfluence(islands.get(motherNaturePosition));
        } finally {
            gameDelta.send();
        }
    }

    protected void calculateInfluence(Island island) throws EndGameException {
        if (island == null) throw new IllegalArgumentException("Calculating influence on null island");
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
            if (oldController != null && t.getHouseColor() == oldController)
                influence += island.getNumber();

            if (influence > maxInfluence) {
                winnerColor = t.getHouseColor();
                maxInfluence = influence;
            } else if (influence == maxInfluence) {
                winnerColor = oldController;
            }

        }
        setIslandController(island, winnerColor, oldController);
    }

    protected void setIslandController(Island island, HouseColor newController, HouseColor oldController) throws EndGameException {
        if (newController != null && !newController.equals(oldController)) {
            island.setTeamColor(newController);

            // add to game delta
            gameDelta.addUpdatedGC(island);

            if (oldController != null) {
                Team oldTeam = getTeams().get(oldController.ordinal());
                oldTeam.addTowers(island.getNumber());

                // add to game delta
                gameDelta.updateTeamTowersLeft(oldController, oldTeam.getTowersLeft());
            }
            Team winnerTeam = getTeams().get(newController.ordinal());
            try {
                winnerTeam.removeTowers(island.getNumber());

                // add to game delta
                gameDelta.updateTeamTowersLeft(newController, winnerTeam.getTowersLeft());
            } finally {
                // even if last tower removed and endGameException, check merge so the update can be sent
                checkMerge(island);
            }
        }
    }

    // checks the team with fewer towers left and calculates its number of controlled professors.
    // if two teams have the same number of towers left the team with more professors controlled becomes the winner

    @Override
    public ArrayList<HouseColor> calculateWinner() {
        ArrayList<HouseColor> winners = new ArrayList<>();
        byte minTowers = 8;
        byte maxProfessors = 0;
        for (Team t : teams) {
            HouseColor hc = t.getHouseColor();
            if (t.getTowersLeft() < minTowers) {
                winners.clear();
                winners.add(hc);
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
                    winners.add(hc);
                    maxProfessors = numberOfProfessors;
                } else if (numberOfProfessors == maxProfessors) {
                    winners.add(hc);
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
    public void moveFromCloud(int cloudId) throws GameException {

        GameComponent cloudSource = getComponentById(cloudId);

        if (cloudSource.howManyStudents() == 0)
            throw new NotAllowedException("Can't move from the selected cloud");
        try {
            cloudSource.moveAll(getCurrentPlayer().getEntranceHall());

            // add to game delta
            gameDelta.addUpdatedGC(cloudSource);
            gameDelta.addUpdatedGC(getCurrentPlayer().getEntranceHall());
        } finally {
            gameDelta.send();
        }
    }

    protected ArrayList<Cloud> getClouds() {
        return clouds;
    }

    protected Player getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    protected byte getCurrentPlayerIndex() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player p) {
        if (p == null) throw new IllegalArgumentException("Cannot set null current player");
        byte newCurrentPlayer = (byte) p.getWizard().ordinal();
        if (newCurrentPlayer != this.currentPlayer) {
            this.currentPlayer = newCurrentPlayer;
        }
    }

    @Override
    public void setCurrentPlayer(byte currentPlayer) {
        if (currentPlayer < 0 || currentPlayer >= getPlayerSize())
            throw new IllegalArgumentException("Not a valid current player index");
        this.currentPlayer = currentPlayer;
    }

    public GameDelta transformAllGameInDelta() {
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                gameDelta.addUpdatedGC(p.getEntranceHall());
                gameDelta.addUpdatedGC(p.getLunchHall());
            }
        }
        for (Cloud c : clouds) {
            gameDelta.addUpdatedGC(c);
        }
        for (Island i : islands) {
            gameDelta.addUpdatedGC(i);
        }
        gameDelta.setNewMotherNaturePosition(motherNaturePosition);

        return gameDelta;
    }

    protected Player getPlayer(byte b) {
        if (b < 0 || b >= getPlayerSize())
            throw new IllegalArgumentException("Not a valid player index");
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

    public GameDelta getGameDelta() {
        return this.gameDelta;
    }

    protected byte getPlayerSize() {
        return (byte) (teams.size() * teams.get(0).getPlayers().size());
    }

    protected ArrayList<Team> getTeams() {
        return teams;
    }

    // used only in tests
    protected ArrayList<Island> getIslands() {
        return islands;
    }

    protected Bag getBag() {
        return this.bag;
    }

    protected Wizard[] getProfessor() {
        return professors;
    }

    // used only in tests
    protected byte getMotherNaturePosition() {
        return motherNaturePosition;
    }
}
