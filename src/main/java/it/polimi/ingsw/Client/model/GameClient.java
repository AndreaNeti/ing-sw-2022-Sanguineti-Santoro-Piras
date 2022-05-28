package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.AssistantCard;
import it.polimi.ingsw.Server.model.GameComponent;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameClient extends GameClientListened implements GameClientView {
    public ArrayList<IslandClient> islands;
    public ArrayList<GameComponentClient> clouds;
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
    // player id, new playerCoinsLeft
    private final Map<Byte, Byte> updatedCoinPlayer;
    private byte newCoinsLeft, newProhibitionsLeft;
    private boolean extraSteps;


    public GameClient(ArrayList<TeamClient> teamsClient, Wizard myWizard, MatchType matchType) {
        this.myWizard = myWizard;
        this.professors = new Wizard[Color.values().length];
        Arrays.fill(professors, null);
        this.matchType = matchType;
        this.matchConstants = Server.getMatchConstants(matchType);
        islands = new ArrayList<>(12);
        clouds = new ArrayList<>(matchType.nPlayers());
        for (int i = 1; i <= matchType.nPlayers(); i++) {
            clouds.add(new GameComponentClient(-i));
        }
        for (int i = 0; i < 12; i++) {
            islands.add(new IslandClient(2 * matchType.nPlayers() + i));
        }
        this.teams = teamsClient;
        this.characters = new ArrayList<>();
        this.charactersWithStudents = new ArrayList<>();
        this.updatedCoinPlayer = new HashMap<>();
        for (byte i = 0; i < matchType.nPlayers(); i++) {
            updatedCoinPlayer.put(i, (byte) matchConstants.initialPlayerCoins());
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
        if (currentPlayer != null)
            return getPlayer(currentPlayer);
        return null;
    }

    public void setCurrentPlayer(Byte currentPlayer) {
        this.currentPlayer = currentPlayer;
        boolean isMyTurn = currentPlayer == myWizard.ordinal();
        notify(getPlayer(currentPlayer).toString(), isMyTurn);
    }

    @Override
    public Wizard[] getProfessors() {
        synchronized (professors) {
            return Arrays.copyOf(professors, professors.length);
        }
    }

    public void setProfessors(Color color, Wizard owner) {
        this.professors[color.ordinal()] = owner;
        notify(color, owner);
    }

    //I am trusting server pls
    public void setGameComponent(Byte idGameComponent, GameComponent gameComponent) {

        /*here the id is static
        from 0 to 2*numberOfPlayer-1 is entranceHall,LunchHall
        from 2*numberOfPlayer to 2*numberOfPlayer+12 are the island
        from -1 to -4 are clouds;
        from -10 to -12 are the characters, here I ignore this
        */
        if (idGameComponent >= 0 && idGameComponent < 2 * matchType.nPlayers()) {
            int playerIndex = idGameComponent / 2;
            if (idGameComponent % 2 == 0) {
                GameComponentClient entranceHall = getPlayer(playerIndex).getEntranceHall();
                entranceHall.modifyGameComponent(gameComponent);
                notify(entranceHall);

            } else {
                GameComponentClient lunchHall = getPlayer(playerIndex).getLunchHall();
                lunchHall.modifyGameComponent(gameComponent);
                notify(lunchHall);
            }

        } else if (idGameComponent >= 2 * matchType.nPlayers()) {
            IslandClient islandToReturn = getIslandById(idGameComponent);
            if (islandToReturn == null) {
                throw new RuntimeException("error in passing parameters probably");
            }
            islandToReturn.modifyGameComponent(gameComponent);

            notify(islandToReturn);
        } else if (idGameComponent <= -10) {
            for (CharacterCardClientWithStudents ch : charactersWithStudents) {
                if (ch.getId() == idGameComponent) {
                    ch.modifyGameComponent(gameComponent);
                }
            }
        } else {
            GameComponentClient cloud = clouds.get(-idGameComponent - 1);
            cloud.modifyGameComponent(gameComponent);
            notify(cloud);
        }
    }

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

    public void removeIsland(byte idIsland) {
        IslandClient islandToRemove = getIslandById(idIsland);
        if (islandToRemove == null) {
            throw new RuntimeException("error in passing parameters probably");
        }
        islands.remove(islandToRemove);
        notify(islands);
    }

    public void setMotherNaturePosition(byte motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
        notifyMotherNature(motherNaturePosition);
    }

    @Override
    public byte getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public void playCard(AssistantCard playedCard) {
        getCurrentPlayer().playCard(playedCard);
        notifyCardPlayed(playedCard);
    }

    public void setTowerLeft(HouseColor houseColor, Byte towerLeft) {
        teams.get(houseColor.ordinal()).setTowersLeft(towerLeft);
        notify(houseColor, towerLeft);
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
    public Byte getNewCoinsLeft() {
        return newCoinsLeft;
    }

    public void setNewCoinsLeft(Byte newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
        //TODO add update
    }

    @Override
    public Byte getNewProhibitionsLeft() {
        return newProhibitionsLeft;
    }

    public void setNewProhibitionsLeft(Byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
        //TODO add update
    }

    @Override
    public boolean isExtraSteps() {
        return extraSteps;
    }

    public void setExtraSteps(boolean extraSteps) {
        this.extraSteps = extraSteps;
        notifyExtaSteps(extraSteps);
    }

    public void setUpdatedCoinPlayer(byte playerIndex, byte newCoins) {
        this.updatedCoinPlayer.put(playerIndex, newCoins);
    }

    @Override
    public byte getCoinsPlayer(byte wizardIndex) {
        return updatedCoinPlayer.get(wizardIndex);
    }

    @Override
    public List<CharacterCardClient> getCharacters() {
        lockForCharacter.lock();
        List<CharacterCardClient> characterCardClients = Stream.concat(characters.stream(), charactersWithStudents.stream()).collect(Collectors.toList());
        lockForCharacter.unlock();
        return characterCardClients;
    }

    public void setCharacters(List<Byte> charactersReceived) {
        if (lockForCharacter == null)
            lockForCharacter = new ReentrantLock();
        if (lockForCharacter.tryLock())
            try {
                for (Byte character : charactersReceived) {
                    if (character == 0 || character == 6 || character == 10) {
                        charactersWithStudents.add((CharacterCardClientWithStudents) factoryCharacter(character));
                    } else {
                        this.characters.add(factoryCharacter(character));
                    }
                }
                notifyCharacter(getCharacters());
            } finally {
                lockForCharacter.unlock();
            }
    }

    private CharacterCardClient factoryCharacter(byte i) {
        switch (i) {
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
        throw new IllegalArgumentException("Character card " + i + " doesn't exists");
    }

    @Override
    public CharacterCardClient getCurrentCharacterCard() {
        return currentCharacterCard;
    }

    @Override
    public MatchType getMatchType() {
        return matchType;
    }

    public void setCurrentCharacterCard(int indexCharacter) {
        this.currentCharacterCard = getCharacters().get(indexCharacter);
    }

    @Override
    public PlayerClient getPlayer(int n) {
        if (n < 0 || n >= matchType.nPlayers())
            throw new IllegalArgumentException("Not a valid player index");
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

    public void unsetCurrentCharacterCard() {
        this.currentCharacterCard = null;
    }

    public void setUpdatedCharacter(Byte charId, Boolean value) {
        for (CharacterCardClient c : characters) {
            if (c.getCharId() == charId)
                c.setUsed();
        }
        for (CharacterCardClientWithStudents c : charactersWithStudents)
            if (c.getCharId() == charId)
                c.setUsed();
    }
}
