package it.polimi.ingsw.Server.model;

import it.polimi.ingsw.Server.controller.GameDelta;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.model.GameComponents.Bag;
import it.polimi.ingsw.Server.model.GameComponents.Cloud;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.GameComponents.Island;
import it.polimi.ingsw.Util.*;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.NotExpertGameException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * NormalGame class contains the main logic of "Eriantys" and correspond to the model in the MVC pattern. <br>
 * The game is divided into various game components: the islands, the clouds, the bag and
 * the players' lunch halls and entrance halls. The game also contains the playing teams and their
 * respective players. <br>
 * Most functions add the updated info of the game to the Game Delta, which is then sent to the
 * clients to inform them about the changes happening in the game.
 */
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

    /**
     * Constructor NormalGame creates a new NormalGame instance.
     *
     * @param teamList       of type {@code ArrayList}<{@link Team}> - list of the instances of team that are playing in the game.
     * @param matchConstants of type {@link MatchConstants} - match constant of the game, based on its type.
     */
    public NormalGame(ArrayList<Team> teamList, MatchConstants matchConstants) {
        if (teamList == null || matchConstants == null) throw new IllegalArgumentException("Passing null parameter");
        if (teamList.size() < 2) throw new IllegalArgumentException("Cannot initialize a game with less than 2 teams");
        this.matchConstants = matchConstants;
        gameDelta = getNewGameDelta();
        this.teams = new ArrayList<>(teamList);
        byte numberOfPlayers = this.getPlayerSize();

        this.islands = new ArrayList<>(12);
        for (byte i = (byte) (2 * MatchType.MAX_PLAYERS); i < (byte) (2 * MatchType.MAX_PLAYERS + 12); i++) {
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


    /**
     * Method getNewGameDelta returns a new GameDelta for the game.
     *
     * @return {@link GameDelta} - new instance of the game's GameDelta.
     */
    protected GameDelta getNewGameDelta() {
        return new GameDelta();
    }


    /**
     * Method initializeMotherNature sets the position of mother nature to the first island of the list (position 0)
     * and draws a student on each island the one with mother nature on it and the one opposite to it.
     */
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


    /**
     * Method checkMerge checks if the islands before and/or after the selected island are controlled
     * by the same team and in case merges them together.
     *
     * @param island of type {@link Island} - the island of which we want to check the neighbouring islands.
     * @throws EndGameException if the number of islands left after merging is <= 3.
     */
    protected void checkMerge(Island island) throws EndGameException {
        if (island == null) throw new IllegalArgumentException("Passing null island");
        int islandBeforeIndex = Math.floorMod(islands.indexOf(island) - 1, islands.size());
        int islandAfterIndex = (islands.indexOf(island) + 1) % islands.size();
        Set<Byte> deletedIsland = new HashSet<>();
        Island islandBefore = islands.get(islandBeforeIndex);
        Island islandAfter = islands.get(islandAfterIndex);

        if (islandBefore.getTeamColor() != null && islandBefore.getTeamColor().equals(island.getTeamColor())) {

            island.merge(islandBefore);
            System.out.println("Removed island with id " + islandBefore.getId());
            islands.remove(islandBefore);
            //TODO bug with herald
            System.out.println("1index before: " + islandBeforeIndex + "index after:" + islandAfterIndex);
            if (islandBeforeIndex < islandAfterIndex)
                islandAfterIndex--;
            System.out.println("1position of mt" + motherNaturePosition);
            if (islandBeforeIndex < motherNaturePosition)
                motherNaturePosition--;
            System.out.println("1position of mt after" + motherNaturePosition);

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            //TODO
            deletedIsland.add(islandBefore.getId());
        }
        if (islandAfter.getTeamColor() != null && islandAfter.getTeamColor().equals(island.getTeamColor())) {
            island.merge(islandAfter);
            System.out.println("Removed island with id " + islandAfter.getId());
            islands.remove(islandAfter);
            System.out.println("2position of mt" + motherNaturePosition);
            if (islandAfterIndex <= motherNaturePosition)
                motherNaturePosition = (byte) Math.floorMod(motherNaturePosition - 1, islands.size());

            System.out.println("1position of mt after" + motherNaturePosition);

            // add to game delta
            gameDelta.setNewMotherNaturePosition(motherNaturePosition);
            gameDelta.addUpdatedGC(island);
            //TODO
            deletedIsland.add(islandAfter.getId());

        }
        gameDelta.setDeletedIslands(new DeletedIsland(island.getId(), deletedIsland));
        if (islands.size() <= matchConstants.minIslands())
            throw new EndGameException(true);
    }

    /**
     * Method drawStudents draws students from the bag to the selected game component.
     *
     * @param gameComponent of type {@link GameComponent} - the game component on which we want to put the students.
     * @param students      of type {@code byte} - the number of students to draw.
     * @throws EndGameException if there are no more students available on the bag.
     * @throws GameException    if the game component selected is null.
     */
    protected void drawStudents(GameComponent gameComponent, byte students) throws EndGameException, GameException {
        if (gameComponent == null) throw new IllegalArgumentException("Drawing students to null gameComponent");
        try {
            bag.drawStudent(gameComponent, students);
        } finally {
            // add to game delta
            gameDelta.addUpdatedGC(gameComponent);
        }
    }

    /**
     * Method refillClouds draws 3 or 4 students based on the number of players to each cloud.
     *
     * @throws EndGameException if there are no more students available on the bag.
     */
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

    /**
     * Method playCard is used by each player to play an assistant card during the planification phase.
     *
     * @param card of type {@link AssistantCard} - the card that the player wants to play.
     * @throws GameException    if the card value is not in the permitted range of values.
     * @throws EndGameException if after playing the selected card there are no cards available left.
     */
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

    /**
     * Method getComponentById gets a game component instance based on his unique ID.
     *
     * @param idGameComponent of type {@code int} - the id of the game component
     * @return {@link GameComponent} - the instance of the game component
     * @throws GameException if the id is not a valid one or corresponds to a merged island
     */
    protected GameComponent getComponentById(int idGameComponent) throws GameException {
        /*here the id is static
        from 0 to 2*numberOfPlayer-1 is entranceHall,LunchHall
        from 2*maxNumberOfPlayer to 2*maxNumberOfPlayer+12 are the island
        from -1 to -4 are clouds;
        from -10 to -12 are the characters, here I ignore this because you can never move to this component
        */
        if (idGameComponent == 69)
            return bag;

        if (idGameComponent < (-clouds.size()) || idGameComponent >= (2 * MatchType.MAX_PLAYERS + 12) || (idGameComponent >= 2 * getPlayerSize() && idGameComponent < 2 * MatchType.MAX_PLAYERS)) {
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


    /**
     * Method move is used to move a student from a game component to another, using their unique ID.
     *
     * @param color                      of type {@link Color} - the color of the student to move.
     * @param idGameComponentSource      of type {@code int} - the ID of the source component.
     * @param idGameComponentDestination of type {@code int} - the ID of the target component.
     * @throws GameException if the color is null or if at least one ID is not valid or if it's not possible to move the student.
     */
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

    /**
     * Method calculateProfessor compares for each color the number of students in the lunch hall of each player
     * and then puts the wizard of the player with the most students in the professors array, in the slot of the color compared.
     * In case of a tie, no wizard will be put in the array.
     */
    protected void calculateProfessor() {
        //TODO: make this function similar to notify coins
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

    /**
     * Method checkMoveMotherNature checks if mother nature can move the requested number of steps, based
     * on the current player's played card.
     *
     * @param moves of type {@code int} - number of steps the player want to move mother nature.
     * @return {@code boolean} - true if moves <= moves allowed by played card, boolean false else.
     */
    protected boolean checkMoveMotherNature(int moves) {
        return moves <= getCurrentPlayer().getPlayedCard().moves();
    }

    /**
     * Method moveMotherNature moves mother nature by a number of steps selected by the player and then
     * recalculates the influence on the new island position of mother nature.
     *
     * @param moves of type {@code int} - number of steps the player want to move mother nature.
     * @throws NotAllowedException if the number of moves is bigger than the value allowed.
     * @throws EndGameException    if the number of islands left is <= 3.
     */
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
            // TODO move all send calls in a single place
            gameDelta.send();
        }
    }

    /**
     * Method calculateInfluence sets the team with the highest influence as the controller of the selected island,
     * based on the number of students present for each controlled color and on the number of towers on the island.
     *
     * @param island of type {@link Island} - the island of which we want to calculate the new controller.
     * @throws EndGameException if a team has no towers left in its board.
     */
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
                influence += island.getArchipelagoSize();

            if (influence > maxInfluence) {
                winnerColor = t.getHouseColor();
                maxInfluence = influence;
            } else if (influence == maxInfluence) {
                winnerColor = oldController;
            }

        }
        setIslandController(island, winnerColor, oldController);
    }

    /**
     * Method setIslandController updates the controller of the island, adding towers to the old controller and removing
     * them from the new one.
     *
     * @param island        of type {@link Island} - island of which we want to update the controller.
     * @param newController of type {@link HouseColor} - the new controller of the island.
     * @param oldController of type {@link HouseColor} - the old controller of the island.
     * @throws EndGameException if after removing towers from the new controller he has no towers left in his board.
     */
    protected void setIslandController(Island island, HouseColor newController, HouseColor oldController) throws EndGameException {
        if (newController != null && !newController.equals(oldController)) {
            island.setTeamColor(newController);

            // add to game delta
            gameDelta.addUpdatedGC(island);

            if (oldController != null) {
                Team oldTeam = getTeams().get(oldController.ordinal());
                oldTeam.addTowers(island.getArchipelagoSize());

                // add to game delta
                gameDelta.updateTeamTowersLeft(oldController, oldTeam.getTowersLeft());
            }
            Team winnerTeam = getTeams().get(newController.ordinal());
            try {
                winnerTeam.removeTowers(island.getArchipelagoSize());

                // add to game delta
                gameDelta.updateTeamTowersLeft(newController, winnerTeam.getTowersLeft());
            } finally {
                // even if last tower removed and endGameException, check merge so the update can be sent
                checkMerge(island);
            }
        }
    }

    /**
     * Method calculateWinner returns the team with fewer towers left. In case of a tie, the winner is the team with
     * more professors controlled. In case of another tie, two or more teams are considered winners.
     *
     * @return {@code ArrayList}<{@link HouseColor}> - the list of winning teams.
     */
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


    /**
     * Method setCharacterInputs not available for normal games.
     *
     * @param inputs of type {@code List}<{@code Integer}> - list of inputs for character chard.
     * @throws GameException when this method is played during a normal game.
     */
    @Override
    public void setCharacterInputs(List<Integer> inputs) throws GameException {
        throw new NotExpertGameException();
    }

    /**
     * Method chooseCharacter not available for normal games.
     *
     * @param charId of type {@code byte} - index of the character card chosen.
     * @throws GameException when this method is played during a normal game.
     */
    @Override
    public void chooseCharacter(byte charId) throws GameException {
        throw new NotExpertGameException();
    }

    /**
     * Method playCharacter not available for normal games.
     *
     * @throws GameException when this method is played during a normal game.
     */
    @Override
    public void playCharacter() throws GameException, EndGameException {
        throw new NotExpertGameException();
    }

    /**
     * Method moveFromCloud moves students from the selected cloud to the player's entrance hall.
     *
     * @param cloudId of type {@code int} - the unique ID of the selected cloud.
     * @throws GameException if the selected cloud has no students.
     */
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

    /**
     * Method getClouds returns the list of clouds.
     *
     * @return {@code ArrayList}<{@link Cloud}> - clouds in the game.
     */
    protected ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Method getCurrentPlayer returns the player currently playing.
     *
     * @return {@link Player} - instance of the current player.
     */
    protected Player getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    /**
     * Method getCurrentPlayerIndex returns the index of the player currently playing.
     *
     * @return {@code byte} - index of the current player.
     */
    protected byte getCurrentPlayerIndex() {
        return currentPlayer;
    }

    /**
     * Method setCurrentPlayer updates the current player.
     *
     * @param player of type {@link Player} - the instance of the new current player.
     */
    public void setCurrentPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Cannot set null current player");
        byte newCurrentPlayer = (byte) player.getWizard().ordinal();
        if (newCurrentPlayer != this.currentPlayer) {
            this.currentPlayer = newCurrentPlayer;
        }
    }

    /**
     * Method setCurrentPlayer updates the current player based on his index.
     *
     * @param currentPlayerIndex of type {@code byte} - the index of the new current player.
     */
    @Override
    public void setCurrentPlayer(byte currentPlayerIndex) {
        if (currentPlayerIndex < 0 || currentPlayerIndex >= getPlayerSize())
            throw new IllegalArgumentException("Not a valid current player index");
        this.currentPlayer = currentPlayerIndex;
    }


    /**
     * Method transformAllGameInDelta saves all the game info inside the GameDelta that is sent to the client.
     *
     * @return {@link GameDelta} - GameDelta with all the info of the game.
     */
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


    /**
     * Method getPlayer returns the player based on the index provided.
     *
     * @param index of type {@code byte} - the index of the player.
     * @return {@link Player} - instance of the player with the requested index.
     */
    protected Player getPlayer(byte index) {
        if (index < 0 || index >= getPlayerSize())
            throw new IllegalArgumentException("Not a valid player index");
        // teams index = b % team size (in 4 players game, players are inserted in team 0, indexOf1, 0, 1)
        // player index (in team members) = b / team size (2 or 3 players -> = 0 (team contains only 1 member), 4 players -> first 2 = 0, last 2 = 1 (3rd player is in team 0 and is member[1]))
        return teams.get(index % teams.size()).getPlayers().get(index / teams.size());
    }


    /**
     * Method getPlayers returns all the players in the game.
     *
     * @return {@code ArrayList}<{@link Player}> - list of the instances of all players in game.
     */
    protected ArrayList<Player> getPlayers() {
        ArrayList<Player> ret = new ArrayList<>(getPlayerSize());
        for (byte i = 0; i < getPlayerSize() / teams.size(); i++)
            for (Team t : teams)
                ret.add(t.getPlayers().get(i));
        return ret;
    }


    /**
     * Method getGameDelta is used to obtain the GameDelta of the game.
     *
     * @return {@link GameDelta} - instance of the game's GameDelta.
     */
    public GameDelta getGameDelta() {
        return this.gameDelta;
    }

    /**
     * Method getPlayerSize returns the number of players in the game.
     *
     * @return {@code byte} - number of players in the game.
     */
    protected byte getPlayerSize() {
        return (byte) (teams.size() * teams.get(0).getPlayers().size());
    }

    /**
     * Method getTeams returns the teams playing in the game.
     *
     * @return {@code ArrayList}<{@link Team}> - list of the instances of all teams in game.
     */
    protected ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * Method getIslands returns the islands of the game.
     *
     * @return {@code ArrayList}<{@link Island}> - list of the instances of the game's islands.
     */
    // used only in tests
    protected ArrayList<Island> getIslands() {
        return islands;
    }

    /**
     * Method getBag returns the game's bag.
     *
     * @return {@link Bag} - instance of the game's bag.
     */
    protected Bag getBag() {
        return this.bag;
    }

    /**
     * Method getProfessors returns the wizards controlling each professor.
     *
     * @return {@link Wizard}{@code []} - array of wizards controlling each professor (size = number of colors). <br>
     * Each position in the array corresponds to the respective professor's color in the {@link Color} enum.
     */

    protected Wizard[] getProfessor() {
        return professors;
    }


    /**
     * Method getMotherNaturePosition returns the current position of mother nature.
     *
     * @return {@code byte} - mother nature position, corresponding to the index of the island on which she currently is.
     */
    // used only in tests
    protected byte getMotherNaturePosition() {
        return motherNaturePosition;
    }
}
