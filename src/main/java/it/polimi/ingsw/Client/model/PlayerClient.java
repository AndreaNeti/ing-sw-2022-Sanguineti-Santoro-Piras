package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.Player;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Wizard;

import java.util.List;

/**
 * PlayerClient class represents the player on the client side and corresponds to the server class {@link Player}.
 */
public class PlayerClient {
    private final String nickName;
    private final Wizard wizard;
    private final List<AssistantCard> assistantCards;
    private final GameComponentClient entranceHall;
    private final GameComponentClient lunchHall;
    private AssistantCard playedCard;

    /**
     * Constructor PlayerClient creates a new instance of PlayerClient.
     *
     * @param p of type {@link Player} - instance of the server's player.
     */
    public PlayerClient(Player p) {
        this.nickName = p.toString();
        this.wizard = p.getWizard();
        this.assistantCards = p.getAssistantCards();
        this.playedCard = null;

        this.entranceHall = new GameComponentClient(2 * wizard.ordinal());
        this.lunchHall = new GameComponentClient(2 * wizard.ordinal() + 1);
    }

    /**
     * Method getEntranceHall returns the entrance hall of the player.
     *
     * @return {@link GameComponentClient} - instance of the player's entrance hall.
     */
    public GameComponentClient getEntranceHall() {
        return entranceHall;
    }

    /**
     * Method getLunchHall returns the lunch hall of the player.
     *
     * @return {@link GameComponentClient} - instance of the player's lunch hall.
     */
    public GameComponentClient getLunchHall() {
        return lunchHall;
    }

    /**
     * Method getPlayedCard returns the assistant card used by the player during the ongoing turn.
     *
     * @return {@link GameComponentClient} - instance of the played assistant card.
     */
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     * Method getAssistantCards returns the cards playable by the player.
     *
     * @return {@code List}<{@link AssistantCard}> - list of cards available to play.
     */
    public List<AssistantCard> getAssistantCards() {
        return assistantCards;
    }


    /**
     * Method playCard plays the selected assistant card, removing it from the list of available ones.
     *
     * @param card of type {@link AssistantCard} - the card that the player wants to play.
     */
    public void playCard(AssistantCard card) {
        if (assistantCards.remove(card)) this.playedCard = card;
    }

    /**
     * Method getWizard returns the wizard assigned to the player.
     *
     * @return {@link Wizard} - wizard assigned to the player.
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     * Method getNickName returns the nickname of the player.
     *
     * @return {@code String} - the player's nickname.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Method toString returns the nickname of the player.
     *
     * @return {@code String} - the player's nickname.
     */
    @Override
    public String toString() {
        return nickName;
    }
}
