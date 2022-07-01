package it.polimi.ingsw.server.model;


import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.server.model.gameComponents.EntranceHall;
import it.polimi.ingsw.server.model.gameComponents.LunchHall;
import it.polimi.ingsw.utils.AssistantCard;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.MatchConstants;
import it.polimi.ingsw.utils.Wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Player class represents the players of "Eriantys". Each player is assigned a wizard and
 * has their own unique nickname, an entrance hall, a lunch hall and assistant cards.
 */
public class Player implements Serializable {
    private final String nickName;
    private final Wizard wizard;
    private final List<AssistantCard> assistantCards;
    private transient AssistantCard playedCard;
    private transient final EntranceHall entranceHall;
    private transient final LunchHall lunchHall;
    private transient byte cardsLeft;

    /**
     * Constructor Player creates a new instance of Player.
     *
     * @param nickName of type {@code String} - the nickname to give to the player.
     * @param team of type {@link Team} - the team of the player.
     * @param wizard of type {@link Wizard} - the wizard to give to the player.
     * @param matchConstants of type {@link MatchConstants} - constants such as number of assistant cards and students in
     *                       lunch hall and entrance hall, based on the game type.
     * @throws GameException if the player cannot be added to the selected team.
     */
    public Player(String nickName, Team team, Wizard wizard, MatchConstants matchConstants) throws GameException {
        if (nickName == null || team == null || matchConstants == null)
            throw new IllegalArgumentException("Cannot pass null argument");
        this.nickName = nickName;
        team.addPlayer(this);
        this.wizard = wizard;

        this.cardsLeft = (byte) matchConstants.numOfCards();
        this.assistantCards = new ArrayList<>(this.cardsLeft);

        for (byte i = 1; i <= this.cardsLeft; i++)
            assistantCards.add(new AssistantCard(i, (byte) ((i + 1) / 2)));

        this.playedCard = null; // 0 = no card, else 1 to 10
        this.entranceHall = new EntranceHall(matchConstants.entranceHallStudents(), (byte) (wizard.ordinal() * 2));
        this.lunchHall = new LunchHall(Color.values().length * matchConstants.maxLunchHallStudents(), (byte) (wizard.ordinal() * 2 + 1));
    }


    /**
     * Method useCard is used by the player to play an assistant card, removing it from the list of available ones.
     *
     * @param card of type {@link AssistantCard} - the card the player wants to play.
     * @throws NotAllowedException if there are no more cards left or the chosen card is not valid.
     * @throws EndGameException if the player uses the last card available.
     */
    public void useCard(AssistantCard card) throws NotAllowedException, EndGameException {

        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        if (card == null || !assistantCards.contains(card))
            throw new NotAllowedException("Not a valid card to play");

        playedCard = card;
        assistantCards.remove(card);
        cardsLeft--;
        if (cardsLeft == 0) throw new EndGameException(false);
    }

    /**
     * Method getPlayedCard returns the assistant card used by the player during the ongoing turn.
     *
     * @return {@link AssistantCard} - instance of the played assistant card.
     */
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     * Method canPlayCard checks if a card can be played based on the cards chosen by other players.
     * Each player cannot play the same card of a previous player in the same turn, except it's the only card
     * left available.
     *
     * @param playedCardsInRound of type {@code ArrayList}<{@link AssistantCard}> - list of cards chosen by the players before in this turn.
     * @param card of type {@link AssistantCard} - card that the current player wants to play.
     * @return {@code boolean} - true if the card selected can be played, false else.
     */
    // checks if a card can be played in function of the cards chosen by other players
    public boolean canPlayCard(ArrayList<AssistantCard> playedCardsInRound, AssistantCard card) {
        if (playedCardsInRound == null || card == null)
            throw new IllegalArgumentException("Passing null played cards list");

        // you have already used that card or is a not existing one
        if (!assistantCards.contains(card)) return false;

        // no one has already played a card, you can play it
        if (playedCardsInRound.size() == 0) return true;

        // if the card you chose is already chosen by someone else, check if you hadn't other choices
        if (playedCardsInRound.contains(card)) {
            ArrayList<AssistantCard> differentCards = new ArrayList<>(assistantCards);
            differentCards.removeAll(playedCardsInRound);
            // there is a card not played by other player that you still didn't choose, you should play that one instead
            return differentCards.isEmpty();
        }
        // even if the card you chose is also played by someone else you hadn't other choices
        return true;
    }

    /**
     * Method getLunchHall returns the lunch hall of the player.
     *
     * @return {@link LunchHall} - instance of the player's lunch hall.
     */
    public LunchHall getLunchHall() {
        return lunchHall;
    }

    /**
     * Method getEntranceHall returns the entrance hall of the player.
     *
     * @return {@link EntranceHall} - instance of the player's entrance hall.
     */
    public EntranceHall getEntranceHall() {
        return entranceHall;
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
     * Method getAssistantCards returns the cards playable by the player.
     *
     * @return {@code List}<{@link AssistantCard}> - list of cards available to play.
     */
    public List<AssistantCard> getAssistantCards() {
        // AssistantCard is immutable
        return assistantCards;
    }

    /**
     * Method equals is used to compare two Players, based on their unique nickname.
     *
     * @param o of type {@code Object} - instance of the other Object.
     * @return {@code boolean} - true if the other object is a Player and has the same nickname of the player.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        // only checks nickname, it's unique
        return this.toString().equals(player.toString());
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

    /**
     * Method hasCode returns the hash code obtained from the player's nickname.
     *
     * @return {@code int} - hash code of the player's nickname.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}
