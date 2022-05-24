package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;
import java.util.List;

public abstract class GameClientListened {
    private List<GameClientListener> listeners;

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

    public void notify(ArrayList<IslandClient> islands) {
        for (GameClientListener listener : listeners) {
            listener.update(islands);
        }
    }

    public void notify(HouseColor houseColor, Byte towerLefts) {
        for (GameClientListener listener : listeners) {
            listener.update(houseColor, towerLefts);
        }
    }

    public void notify(Color color, Wizard wizard) {
        for (GameClientListener listener : listeners) {
            listener.update(color, wizard);
        }
    }

    public void notify(String currentPlayer, boolean isMyTurn) {
        for (GameClientListener listener : listeners) {
            listener.update(currentPlayer, isMyTurn);
        }
    }

    public void notifyCardPlayed(Byte playedCard) {
        for (GameClientListener listener : listeners) {
            listener.updateCardPlayed(playedCard);
        }
    }

//    public void notifyOk() {
//        for (GameClientListener listener : listeners) {
//            listener.ok();
//        }
//    }
    public void notifyIgnoredColor(Color color){
        for (GameClientListener listener : listeners) {
            listener.updateIgnoredColor(color);
        }
    }
    public void notifyCharacter(List<CharacterCardClient> characters){
        for (GameClientListener listener : listeners) {
            listener.updateCharacter(characters);
        }
    }
    public void notifyMembers(int membersLeftToStart, String nickPlayerJoined) {
        for (GameClientListener listener : listeners) {
            listener.updateMembers(membersLeftToStart, nickPlayerJoined);
        }
    }

    public void notifyWinners(List<HouseColor> winners){
        for (GameClientListener listener : listeners) {
            listener.setWinners(winners);
        }
    }

    public void addListener(GameClientListener listener) {
        if (listeners == null) listeners = new ArrayList<>();
        listeners.add(listener);
    }

    public void addListener(GameClientListened gameClientListened) {
        if (listeners == null) listeners = gameClientListened.listeners;
    }

//    public void notifyError(String e) {
//        for (GameClientListener listener : listeners) {
//            listener.error(e);
//        }
//    }

    public void notifyView() {
        for (GameClientListener listener : listeners) {
            listener.update();
        }
    }

}
