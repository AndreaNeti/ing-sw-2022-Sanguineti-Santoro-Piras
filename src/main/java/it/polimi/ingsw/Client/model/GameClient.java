package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Client.LimitedChat;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.controller.Server;
import it.polimi.ingsw.Server.model.*;

import java.util.*;
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
    private final List<CharacterCardClient> characters;
    private final List<CharacterCardClientWithStudents> charactersWithStudents;
    private CharacterCardClient currentCharacterCard;
    // player id, new playerCoinsLeft
    private Map<Byte, Byte> updatedCoinPlayer;
    private Byte newCoinsLeft, newProhibitionsLeft;
    private Color ignoredColorInfluence;

    private final LimitedChat<String> chat;

    public GameClient(ArrayList<TeamClient> teamsClient, Wizard myWizard, MatchType matchType) {
        System.out.println(teamsClient.toString());
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
        this.chat = new LimitedChat<>(9);
    }

    public void addMessage(String message) {
        chat.add(message);
    }

    public LimitedChat<String> getChat() {
        return chat;
    }

    public MatchConstants getMatchConstants() {
        return matchConstants;
    }

    public boolean isExpert() {
        return matchType.isExpert();
    }

    public PlayerClient getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    public void setCurrentPlayer(Byte currentPlayer) {
        this.currentPlayer = currentPlayer;
        boolean isMyTurn = currentPlayer == myWizard.ordinal();
        notify(getPlayer(currentPlayer).toString(), isMyTurn);
    }

    public Wizard[] getProfessors() {
        return Arrays.copyOf(professors, professors.length);
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
                GameComponentClient entranceHall = getPlayer(idGameComponent / 2).getEntranceHall();
                entranceHall.modifyGameComponent(gameComponent);
                notify(entranceHall);

            } else {
                GameComponentClient lunchHall = getPlayer(idGameComponent / 2).getLunchHall();
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

    public byte getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public void playCard(byte value) {
        getCurrentPlayer().playCard(value);
        notifyCardPlayed(value);
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

    public Wizard getMyWizard() {
        return myWizard;
    }

    @Override
    public ArrayList<IslandClient> getIslands() {
        return islands;
    }

    @Override
    public List<TeamClient> getTeams() {
        return teams;
    }

    public Byte getNewCoinsLeft() {
        return newCoinsLeft;
    }

    public void setNewCoinsLeft(Byte newCoinsLeft) {
        this.newCoinsLeft = newCoinsLeft;
        //TODO add update
    }

    public Byte getNewProhibitionsLeft() {
        return newProhibitionsLeft;
    }

    public void setNewProhibitionsLeft(Byte newProhibitionsLeft) {
        this.newProhibitionsLeft = newProhibitionsLeft;
        //TODO add update
    }

    public Color getIgnoredColorInfluence() {
        return ignoredColorInfluence;
    }

    public void setIgnoredColorInfluence(Color ignoredColorInfluence) {
        this.ignoredColorInfluence = ignoredColorInfluence;
        notifyIgnoredColor(ignoredColorInfluence);
    }

    public void setUpdatedCoinPlayer(Map<Byte, Byte> updatedCoinPlayer) {
        this.updatedCoinPlayer = updatedCoinPlayer;
    }

    public byte getCoinsPlayer(byte id) {
        return updatedCoinPlayer.get(id);
    }

    @Override
    public List<CharacterCardClient> getCharacters() {
        return Stream.concat(characters.stream(), charactersWithStudents.stream()).collect(Collectors.toList());
    }

    public void setCharacters(List<Byte> charactersReceived) {

        for (Byte character : charactersReceived) {
            if (character == 0 || character == 6 || character == 10) {
                charactersWithStudents.add((CharacterCardClientWithStudents) factoryCharacter(character));
            } else {
                this.characters.add(factoryCharacter(character));
            }
        }
        notifyCharacter(getCharacters());
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
}
