package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.GameClientListened;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.*;

import java.util.ArrayList;
import java.util.Arrays;

public class GameClient extends GameClientListened implements GameClientView {
    public ArrayList<IslandClient> islands;
    public ArrayList<GameComponentClient> clouds;
    private final Wizard[] professors;
    private byte motherNaturePosition;
    private Byte currentPlayer;
    private final Wizard myWizard;
    private final MatchConstants matchConstants;
    //players are in the same order of wizard.ordinal
    private final ArrayList<PlayerClient> players;

    public GameClient(ArrayList<PlayerClient> players, Wizard myWizard, MatchConstants matchConstants) {
        System.out.println(players.toString());
        this.myWizard = myWizard;
        this.professors = new Wizard[Color.values().length];
        Arrays.fill(professors, null);
        this.matchConstants = matchConstants;
        islands = new ArrayList<>(12);
        clouds = new ArrayList<>(matchConstants.studentsToMove());
        for (int i = 0; i < matchConstants.studentsToMove(); i++) {
            clouds.add(new GameComponentClient(-(i + 1)));
        }
        for (int i = 0; i < 12; i++) {
            islands.add(new IslandClient(2 * players.size() + i));
        }
        this.players = players;

    }

    public PlayerClient getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public void setCurrentPlayer(Byte currentPlayer) {
        this.currentPlayer = currentPlayer;
        boolean isMyTurn = currentPlayer == myWizard.ordinal();
        notify(players.get(currentPlayer).toString(), isMyTurn);
    }

    public Wizard[] getProfessors() {
        return Arrays.copyOf(professors, professors.length);
    }

    public void setProfessors(Color color, Wizard owner) {
        this.professors[color.ordinal()] = owner;
        notify(professors);
    }

    //I am trusting server pls
    public void setGameComponent(Byte idGameComponent, GameComponent gameComponent) {

        /*here the id is static
        from 0 to 2*numberOfPlayer-1 is entranceHall,LunchHall
        from 2*numberOfPlayer to 2*numberOfPlayer+12 are the island
        from -1 to -4 are clouds;
        from -10 to -12 are the characters, here I ignore this
        */
        if (idGameComponent >= 0 && idGameComponent < 2 * players.size()) {
            int playerIndex = idGameComponent / 2;
            if (idGameComponent % 2 == 0) {
                GameComponentClient entranceHall = players.get(idGameComponent / 2).getEntranceHall();
                entranceHall.modifyGameComponent(gameComponent);
                notify(entranceHall);

            } else {
                GameComponentClient lunchHall = players.get(idGameComponent / 2).getLunchHall();
                lunchHall.modifyGameComponent(gameComponent);
                notify(lunchHall);
            }

        } else if (idGameComponent >= 2 * players.size()) {
            IslandClient islandToReturn = getIslandById(idGameComponent);
            if (islandToReturn == null) {
                throw new RuntimeException("error in passing parameters probably");
            }
            islandToReturn.modifyGameComponent(gameComponent);

            notify(islandToReturn);
        } else {
            GameComponentClient cloud = clouds.get(-(idGameComponent + 1));
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
        for (PlayerClient p : players) {
            if (p.getHouseColor() == houseColor) {
                p.setTowersLeft(towerLeft);
                notify(houseColor, towerLeft);
            }
        }
    }

    @Override
    public ArrayList<GameComponentClient> getClouds() {
        // not editable out of model package
        return clouds;
    }

    @Override
    public ArrayList<IslandClient> getIslands() {
        return islands;
    }
}
