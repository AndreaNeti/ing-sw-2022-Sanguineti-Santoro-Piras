package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.model.AssistantCard;
import it.polimi.ingsw.Server.model.Player;

import java.util.List;

public class PlayerClient {
    private final String nickName;
    private final Wizard wizard;
    private final List<AssistantCard> assistantCards;
    private final GameComponentClient entranceHall;
    private final GameComponentClient lunchHall;
    private AssistantCard playedCard;

    public PlayerClient(Player p) {
        this.nickName = p.toString();
        this.wizard = p.getWizard();
        this.assistantCards = p.getAssistantCards();
        this.playedCard = null;

        this.entranceHall = new GameComponentClient(2 * wizard.ordinal());
        this.lunchHall = new GameComponentClient(2 * wizard.ordinal() + 1);
    }

    public GameComponentClient getEntranceHall() {
        return entranceHall;
    }

    public GameComponentClient getLunchHall() {
        return lunchHall;
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }

    public void playCard(AssistantCard playedCard) {
        if (assistantCards.remove(playedCard))
            this.playedCard = playedCard;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String toString() {
        return nickName;
    }
}
