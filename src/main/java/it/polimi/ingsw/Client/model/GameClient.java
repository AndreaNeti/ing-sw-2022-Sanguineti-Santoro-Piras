package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Client.GameClientListener;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Server.model.Color;
import it.polimi.ingsw.Server.model.GameComponent;
import it.polimi.ingsw.Server.model.Island;
import it.polimi.ingsw.Server.model.Wizard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameClient {
    public ArrayList<IslandClient> islands;
    public ArrayList<GameComponentClient> clouds;
    private Wizard[] professors;
    public ArrayList<GameClientListener> listeners;
    private byte motherNaturePosition;
    private byte currentPlayer;
    private final MatchConstants matchConstants;
    //players are in the same order of wizard.ordinal
    private final ArrayList<PlayerClient> players;

    public GameClient(ArrayList<PlayerClient> players, MatchConstants matchConstants) {
        this.professors = new Wizard[Color.values().length];
        Arrays.fill(professors, null);
        this.matchConstants = matchConstants;
        islands = new ArrayList<>(12);
        clouds = new ArrayList<>(matchConstants.studentsToMove());
        this.players = players;

    }

    public byte getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(byte currentPlayer) {
        this.currentPlayer = currentPlayer;
        for (GameClientListener listener : listeners) {
            listener.update(professors);
        }
    }

    public Wizard[] getProfessors() {
        return Arrays.copyOf(professors, professors.length);
    }

    public void setProfessors(Wizard[] professors) {
        this.professors = professors;
        for (GameClientListener listener : listeners) {
            listener.update(professors);
        }
    }

    public void addListener(GameClientListener listener) {
        listeners.add(listener);
    }

    //I am trusting server pls
    public void setGameComponent(byte idGameComponent, GameComponent gameComponent) {
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
                for (GameClientListener listener : listeners) {
                    listener.update(entranceHall);
                }
            } else {
                GameComponentClient lunchHall = players.get(idGameComponent / 2).getEntranceHall();
                lunchHall.modifyGameComponent(gameComponent);
                for (GameClientListener listener : listeners) {
                    listener.update(lunchHall);
                }
            }

        } else if (idGameComponent > 2 * players.size()) {
            IslandClient islandToReturn=null;
            for (IslandClient island:islands) {
                if(island.getId()==idGameComponent){
                    islandToReturn=island;
                }
            }
            if (islandToReturn == null) {
                throw new RuntimeException("error in passing parameters probably");
            }
            islandToReturn.modifyGameComponent(gameComponent);
            for (GameClientListener listener : listeners) {
                listener.update(islandToReturn);
            }
        } else {
            GameComponentClient cloud = clouds.get(-(idGameComponent + 1));
            cloud.modifyGameComponent(gameComponent);
            for (GameClientListener listener : listeners) {
                listener.update(cloud);
            }
        }
    }

    public void removeIsland(byte indexIsland){
        islands.remove(indexIsland);
    }
    public void setMotherNaturePosition(byte motherNaturePosition){
        this.motherNaturePosition=motherNaturePosition;
    }
}
