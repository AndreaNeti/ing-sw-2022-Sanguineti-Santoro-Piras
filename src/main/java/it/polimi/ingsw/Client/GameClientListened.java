package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;

import java.util.ArrayList;
import java.util.List;

public abstract class GameClientListened {
    private List<GameClientListener> listeners;

    public void notifyMotherNature(Byte motherNaturePosition) {
        for (GameClientListener listener : listeners) {
            listener.updateMotherNature(motherNaturePosition);
        }
    }

    public void notifyGameComponent(GameComponentClient gameComponent) {
        for (GameClientListener listener : listeners) {
            listener.updateGameComponent(gameComponent);
        }
    }

    public void notifyGameComponent(IslandClient island) {
        for (GameClientListener listener : listeners) {
            listener.updateGameComponent(island);
        }
    }

    public void notifyDeletedIsland(IslandClient islands) {
        for (GameClientListener listener : listeners) {
            listener.updateDeletedIsland(islands);
        }
    }

    public void notifyTowerLeft(HouseColor houseColor, Byte towerLefts) {
        for (GameClientListener listener : listeners) {
            listener.updateTowerLeft(houseColor, towerLefts);
        }
    }

    public void notifyProfessor(Color color, Wizard wizard) {
        for (GameClientListener listener : listeners) {
            listener.updateProfessor(color, wizard);
        }
    }
    public void notifyCardPlayed(AssistantCard playedCard) {
        for (GameClientListener listener : listeners) {
            listener.updateCardPlayed(playedCard);
        }
    }

    public void notifyIgnoredColor(Color color) {
        for (GameClientListener listener : listeners) {
            listener.updateIgnoredColor(color);
        }
    }

    public void notifyExtraSteps(boolean extraSteps) {
        for (GameClientListener listener : listeners) {
            listener.updateExtraSteps(extraSteps);
        }
    }

    public void notifyCharacter(List<CharacterCardClient> characters) {
        for (GameClientListener listener : listeners) {
            listener.updateCharacter(characters);
        }
    }

    public void notifyMembers(int membersLeftToStart, String nickPlayerJoined) {
        for (GameClientListener listener : listeners) {
            listener.updateMembers(membersLeftToStart, nickPlayerJoined);
        }
    }

    public void notifyWinners(List<HouseColor> winners) {
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
    public void notifyMessage(String message) {
        for (GameClientListener listener : listeners) {
            listener.updateMessage(message);
        }
    }

}
