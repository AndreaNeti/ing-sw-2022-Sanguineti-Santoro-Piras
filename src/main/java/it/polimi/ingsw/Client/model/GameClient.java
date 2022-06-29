package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Server.model.ExpertGame;
import it.polimi.ingsw.Server.model.GameComponents.GameComponent;
import it.polimi.ingsw.Server.model.NormalGame;
import it.polimi.ingsw.Util.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * GameClient class represents the main logic of the game, client side, and corresponds to
 * the server classes {@link NormalGame} and {@link ExpertGame}. <br>
 * It contains all the methods for the client to get info about the game and for the server to modify it through the
 * client's controller.
 */
public class GameClient extends GameClientListened implements GameClientView {
    public final ArrayList<IslandClient> islands;
    public final ArrayList<GameComponentClient> clouds;
    private final Wizard[] professors;
    private byte motherNaturePosition;
    private Byte currentPlayer;
    private final Wizard myWizard;
    private final MatchConstants matchConstants;

    private final MatchType matchType;
    //players are in the same order of wizard.ordinal
    private final List<TeamClient> teams;
    private Lock lockForCharacter;
    private final List<CharacterCardClient> characters;
    private final List<CharacterCardClientWithStudents> charactersWithStudents;
    private CharacterCardClient currentCharacterCard;
    //TODO: maybe change these names
    // player id, new playerCoinsLeft
    private final Map<Byte, Integer> updatedCoinPlayer;
    private byte newProhibitionsLeft;
    private int newCoinsLeft;
    private boolean extraSteps;
    private Color ignoredColorInfluence;


    /**
     * Constructor GameClient creates a new instance of GameClient.
     *
     * @param teamsClient of type {@code ArrayList}<{@link TeamClient}> - list of the instances of the game's teams.
     * @param myWizard    of type {@link Wizard} - wizard associated with the client's player.
     * @param matchType   of type {@link MatchType} - type of the game that the client decided to join.
     */
    public GameClient(ArrayList<TeamClient> teamsClient, Wizard myWizard, MatchType matchType, MatchConstants constants) {
        this.myWizard = myWizard;
        this.professors = new Wizard[Color.values().length];
        Arrays.fill(professors, null);
        this.matchType = matchType;
        this.matchConstants = constants;
        islands = new ArrayList<>(12);
        clouds = new ArrayList<>(matchType.nPlayers());
        for (int i = 1; i <= matchType.nPlayers(); i++) {
            clouds.add(new GameComponentClient(-i));
        }
        for (int i = 0; i < 12; i++) {
            islands.add(new IslandClient(2 * MatchType.MAX_PLAYERS + i));
        }
        this.teams = teamsClient;
        this.characters = new ArrayList<>();
        this.charactersWithStudents = new ArrayList<>();
        this.updatedCoinPlayer = new HashMap<>();
        for (byte i = 0; i < matchType.nPlayers(); i++) {
            updatedCoinPlayer.put(i, matchConstants.initialPlayerCoins());
        }
    }


    @Override
    public MatchConstants getMatchConstants() {
        return matchConstants;
    }

    @Override
    public boolean isExpert() {
        return matchType.isExpert();
    }

    @Override
    public PlayerClient getCurrentPlayer() {
        if (currentPlayer != null) return getPlayer(currentPlayer);
        return null;
    }

    /**
     * Method setCurrentPlayer updates the current player based on the index provided.
     *
     * @param currentPlayer of type {@code Byte} - index of the new current player.
     */
    public void setCurrentPlayer(Byte currentPlayer) {
        this.currentPlayer = currentPlayer;
        boolean isMyTurn = currentPlayer == myWizard.ordinal();
    }

    /**
     * Method getProfessors returns the wizards of the players controlling each professor.
     *
     * @return {@link Wizard}{@code []} - copy of the array of wizards controlling each professor (size = number of colors). <br>
     * Each position in the array corresponds to the respective professor's color in the {@link Color} enum.
     */
    @Override
    public Wizard[] getProfessors() {
        synchronized (professors) {
            return Arrays.copyOf(professors, professors.length);
        }
    }

    /**
     * Method setProfessors updates the controller of the professor of a specific color.
     *
     * @param color of type {@link Color} - color of the professor with the new controller.
     * @param owner of type {@link Wizard} - wizard of the new controller.
     */
    public void setProfessors(Color color, Wizard owner) {
        this.professors[color.ordinal()] = owner;
        notifyProfessor(color, owner);
    }

    /**
     * Method setGameComponent updates a game component, identified by its ID, to make it
     * equal to the game component provided.
     *
     * @param idGameComponent of type {@code Byte} - unique ID of the game component.
     * @param gameComponent   of type {@link GameComponent} - instance of the game component to copy.
     */
    // I am trusting server pls
    public void setGameComponent(Byte idGameComponent, GameComponent gameComponent) {

        /*here the id is static
        from 0 to 2 * numberOfPlayer - 1 is entranceHall, LunchHall
        from 2 * maxPlayers to 2 * maxPlayers + 12 are the island
        from -1 to -4 are clouds;
        */
        if (idGameComponent >= 0 && idGameComponent < 2 * matchType.nPlayers()) {
            int playerIndex = idGameComponent / 2;
            if (idGameComponent % 2 == 0) {
                GameComponentClient entranceHall = getPlayer(playerIndex).getEntranceHall();
                entranceHall.modifyGameComponent(gameComponent);
                notifyGameComponent(entranceHall);

            } else {
                GameComponentClient lunchHall = getPlayer(playerIndex).getLunchHall();
                lunchHall.modifyGameComponent(gameComponent);
                notifyGameComponent(lunchHall);
            }

        } else if (idGameComponent >= 2 * MatchType.MAX_PLAYERS) {
            IslandClient islandToReturn = getIslandById(idGameComponent);
            if (islandToReturn == null) {
                System.err.println("Error in passing parameters");
            } else {
                islandToReturn.modifyGameComponent(gameComponent);
                notifyGameComponent(islandToReturn);
            }
        } else if (idGameComponent <= -10) {
            System.out.println(idGameComponent);
            for (CharacterCardClientWithStudents ch : charactersWithStudents) {
                if (ch.getId() == idGameComponent) {
                    ch.modifyGameComponent(gameComponent);
                    notifyGameComponent(ch);
                }
            }
        } else {
            GameComponentClient cloud = clouds.get(-idGameComponent - 1);
            cloud.modifyGameComponent(gameComponent);
            notifyGameComponent(cloud);

        }
    }

    /**
     * Method getIslandById returns the island with the specific ID provided.
     *
     * @param idIsland of type {@code byte} - unique ID of the island.
     * @return {@link IslandClient} - instance of the client's island with the selected ID.
     */
    private IslandClient getIslandById(byte idIsland) {
        IslandClient islandToReturn = null;
        for (IslandClient island : islands) {
            if (island.getId() == idIsland) {
                islandToReturn = island;
                break;
            }
        }
        return islandToReturn;
    }

    /**
     * Method removeIsland removes an island (after merging it) from the game.
     *
     * @param deletedIsland of {@code DeletedIsland} is the record received from the game delta
     */
    public void removeIslands(DeletedIsland deletedIsland) {
        for (Byte idIsland : deletedIsland.deletedIsland()
        ) {
            IslandClient islandToRemove = getIslandById(idIsland);
            if (islandToRemove == null) {
                System.err.println("Error in passing parameter, island not found");
            }
            islands.remove(islandToRemove);
            notifyDeletedIsland(islandToRemove, getIslandById(deletedIsland.idWinnerIsland()));
        }

    }

    /**
     * Method setMotherNaturePosition updates the position of mother nature.
     *
     * @param motherNaturePosition of type {@code byte} - new island index position of mother nature.
     */
    public void setMotherNaturePosition(byte motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
        notifyMotherNature(motherNaturePosition);
    }

    @Override
    public byte getMotherNaturePosition() {
        return motherNaturePosition;
    }

    /**
     * Method playCard is used by the current player to play a selected assistant card.
     *
     * @param playedCard of type {@link AssistantCard} - instance of the assistant card to play.
     */
    public void playCard(AssistantCard playedCard) {
        getCurrentPlayer().playCard(playedCard);
        notifyCardPlayed(playedCard);
    }

    /**
     * Method setTowerLeft updates the amount of towers left for a specific team.
     *
     * @param houseColor of type {@link HouseColor} - color of the team to update.
     * @param towerLeft  of type {@code Byte} - new amount of towers left.
     */
    public void setTowerLeft(HouseColor houseColor, Byte towerLeft) {
        teams.get(houseColor.ordinal()).setTowersLeft(towerLeft);
        notifyTowerLeft(houseColor, towerLeft);

    }

    @Override
    public ArrayList<GameComponentClient> getClouds() {
        // not editable out of model package
        return clouds;
    }

    @Override
    public Wizard getMyWizard() {
        return myWizard;
    }

    @Override
    public ArrayList<IslandClient> getIslands() {
        return islands;
    }

    @Override
    public List<TeamClient> getTeams() {
        synchronized (teams) {
            return new ArrayList<>(teams);
        }
    }

    @Override
    public Integer getNewCoinsLeft() {
        return newCoinsLeft;
    }

    /**
     * Method setNewCoinsLeft updates the amount of coins available in the game.
     *
     * @param newCoinsLeft of type {@code Integer} - new amount of coins available.
     */
    public void setNewCoinsLeft(Integer newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
        notifyCoins(newCoinsLeft);
    }

    @Override
    public Byte getNewProhibitionsLeft() {
        return newProhibitionsLeft;
    }

    /**
     * Method setNewProhibitionsLeft updates the amount of prohibitions remaining in the game.
     *
     * @param newProhibitionsLeft of type {@code byte} - new amount of prohibitions remaining.
     */
    public void setNewProhibitionsLeft(Byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
        notifyProhibitions(newProhibitionsLeft);
    }

    @Override
    public boolean isExtraSteps() {
        return extraSteps;
    }

    /**
     * Method setExtraSteps updates the {@code extraSteps} boolean with a new provided value.
     *
     * @param extraSteps of type {@code boolean} - new value of {@code extraSteps}.
     */
    public void setExtraSteps(boolean extraSteps) {
        //    notifyExtraSteps(extraSteps);
        this.extraSteps = extraSteps;

    }

    @Override
    public Color getIgnoredColorInfluence() {
        return ignoredColorInfluence;
    }

    /**
     * Method setIgnoredColorInfluence updates the student color to ignore during the influence calculation.
     *
     * @param ignoredColorInfluence of type {@link Color} - new student color to ignore.
     */
    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
        notifyIgnoredColor(ignoredColorInfluence);
    }

    /**
     * Method setUpdatedCoinPlayer updates the amount of coins of the player with the player index provided.
     *
     * @param playerIndex of type {@code byte} - index of the player of which we want to update the coins.
     * @param newCoins    of type {@code int} - new amount of coins.
     */
    public void setUpdatedCoinPlayer(byte playerIndex, int newCoins) {
        this.updatedCoinPlayer.put(playerIndex, newCoins);
        notifyCoins(getPlayers().get(playerIndex).getWizard(), newCoins);
    }

    @Override
    public int getCoinsPlayer(byte wizardIndex) {
        return updatedCoinPlayer.get(wizardIndex);
    }

    @Override
    public List<CharacterCardClient> getCharacters() {
        if (characters == null || lockForCharacter == null) return null;
        lockForCharacter.lock();
        List<CharacterCardClient> characterCardClients = Stream.concat(characters.stream(), charactersWithStudents.stream()).collect(Collectors.toList());
        lockForCharacter.unlock();
        return characterCardClients;
    }

    /**
     * Method setCharacters adds the list of characters received by the server to the client's game, creating a new instance
     * for each of them.
     *
     * @param charactersReceived of type {@code List}<{@code Byte}> - list of the unique IDs of the character cards to add.
     */
    public void setCharacters(List<Byte> charactersReceived) {
        if (lockForCharacter == null) lockForCharacter = new ReentrantLock();
        if (lockForCharacter.tryLock()) try {
            for (Byte character : charactersReceived) {
                if (character == 0 || character == 6 || character == 10) {
                    charactersWithStudents.add((CharacterCardClientWithStudents) factoryCharacter(character));
                } else {
                    this.characters.add(factoryCharacter(character));
                }
            }
        } finally {
            lockForCharacter.unlock();
        }
    }

    /**
     * Method factoryCharacter creates an instance of one of the 12 possible character cards, based on the index provided.
     *
     * @param index of type {@code byte} - index of the CharacterCard to instantiate.
     * @return {@link CharacterCardClient} - instance of the CharacterCard requested.
     */
    private CharacterCardClient factoryCharacter(byte index) {
        switch (index) {
            case 0:
                return new Char0Client();
            case 1:
                return new Char1Client();
            case 2:
                return new Char2Client();
            case 3:
                return new Char3Client();
            case 4:
                return new Char4Client();
            case 5:
                return new Char5Client();
            case 6:
                return new Char6Client();
            case 7:
                return new Char7Client();
            case 8:
                return new Char8Client();
            case 9:
                return new Char9Client();
            case 10:
                return new Char10Client();
            case 11:
                return new Char11Client();
        }
        throw new IllegalArgumentException("Character card " + index + " doesn't exists");
    }

    @Override
    public CharacterCardClient getCurrentCharacterCard() {
        return currentCharacterCard;
    }

    @Override
    public MatchType getMatchType() {
        return matchType;
    }

    /**
     * Method setCurrentCharacterCard updates the character card currently being played, based on the index provided.
     *
     * @param indexCharacter of type {@code int} - index of the new character card being played.
     */
    public void setCurrentCharacterCard(int indexCharacter) {
        this.currentCharacterCard = getCharacters().get(indexCharacter);
    }

    @Override
    public PlayerClient getPlayer(int n) {
        if (n < 0 || n >= matchType.nPlayers()) throw new IllegalArgumentException("Not a valid player index");
        // teams index = b % team size (in 4 players game, players are inserted in team 0, indexOf1, 0, 1)
        // player index (in team members) = b / team size (2 or 3 players -> = 0 (team contains only 1 member), 4 players -> first 2 = 0, last 2 = 1 (3rd player is in team 0 and is member[1]))
        return teams.get(n % teams.size()).getPlayers().get(n / teams.size());
    }

    @Override
    public List<PlayerClient> getPlayers() {
        ArrayList<PlayerClient> ret = new ArrayList<>(matchType.nPlayers());
        for (byte i = 0; i < matchType.nPlayers() / teams.size(); i++)
            for (TeamClient t : teams)
                ret.add(t.getPlayers().get(i));
        return ret;
    }

    /**
     * Method unsetCurrentCharacterCard sets to null the character card currently being played.
     */
    public void unsetCurrentCharacterCard() {
        this.currentCharacterCard = null;
    }

    /**
     * Method setUpdatedCharacter updates the character chard with the provided ID to used, increasing therefore its cost by 1.
     *
     * @param charId of type {@code Byte} - ID of the character card to update.
     */
    public void setUpdatedCharacter(Byte charId) {
        for (CharacterCardClient c : characters) {
            if (c.getCharId() == charId) c.setUsed();
        }
        for (CharacterCardClientWithStudents c : charactersWithStudents)
            if (c.getCharId() == charId) c.setUsed();
        notifyCharacter(charId);
    }

    /**
     * Method deleteModel removes the current game from all the clients that are playing (and listening) it.
     */
    public void deleteModel() {
        this.removeListeners();
    }
}
