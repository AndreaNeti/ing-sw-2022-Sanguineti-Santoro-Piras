package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;

public class GameClientListened {
    private ArrayList<GameClientListener> listeners;

    public void notifyMotherNature(Byte motherNaturePosition) {
        for (GameClientListener listener : listeners) {
            listener.updateMotherNature(motherNaturePosition);
        }
    }

    public void notify(GameComponentClient gameComponent) {
        for (GameClientListener listener : listeners) {
            listener.update(gameComponent);
        }
    }

    public void notify(IslandClient island) {
        for (GameClientListener listener : listeners) {
            listener.update(island);
        }
    }
    public void notify(GamePhase phase){
        for (GameClientListener listener : listeners) {
            listener.update(phase);
        }
    }
    public void notify(ArrayList<IslandClient> islands){
        for (GameClientListener listener : listeners) {
            listener.update(islands);
        }
    }

    public void notify(HouseColor houseColor,Byte towerLefts) {
        for (GameClientListener listener : listeners) {
            listener.update(houseColor,towerLefts);
        }
    }

    public void notify(Wizard[] professors) {
        for (GameClientListener listener : listeners) {
            listener.update(professors);
        }
    }
    public void notify(PlayerClient currentPlayer) {
        for (GameClientListener listener : listeners) {
            listener.update(currentPlayer);
        }
    }

    public void notifyCardPlayed(Byte playedCard){
        for (GameClientListener listener : listeners) {
            listener.updateCardPlayed(playedCard);
        }
    }
    public void notify(String e){
        for (GameClientListener listener : listeners) {
            listener.error(e);
        }
    }
    public void notifyOk(){
        for (GameClientListener listener : listeners) {
            listener.ok();
        }
    }

    public void notifyMembers(int membersLeftToStart){
        for (GameClientListener listener : listeners) {
            listener.updateMembers(membersLeftToStart);
        }
    }
    public void addListener(GameClientListener listener) {
        if(listeners==null)
            listeners=new ArrayList<>();
        listeners.add(listener);
    }
    public void addListener(GameClientListened gameClientListened){
        if(listeners==null)
            listeners=gameClientListened.listeners;
        
    }

}
