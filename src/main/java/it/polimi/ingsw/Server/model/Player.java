package it.polimi.ingsw.Server.model;


import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.GameComponents.EntranceHall;
import it.polimi.ingsw.Server.model.GameComponents.LunchHall;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player implements Serializable {
    private final String nickName;
    private final Wizard wizard;
    private final List<AssistantCard> assistantCards;
    private transient AssistantCard playedCard;
    private transient final EntranceHall entranceHall;
    private transient final LunchHall lunchHall;
    private transient byte cardsLeft;

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

    public void useCard(AssistantCard card) throws NotAllowedException, EndGameException {

        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        if (card == null || !assistantCards.contains(card))
            throw new NotAllowedException("Not a valid card to play");

        playedCard = card;
        assistantCards.remove(card);
        cardsLeft--;
        if (cardsLeft == 0) throw new EndGameException(false);
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

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

    public LunchHall getLunchHall() {
        return lunchHall;
    }

    public EntranceHall getEntranceHall() {
        return entranceHall;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public List<AssistantCard> getAssistantCards() {
        // AssistantCard is immutable
        return assistantCards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        // only checks nickname, it's unique
        return this.toString().equals(player.toString());
    }

    @Override
    public String toString() {
        return nickName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName);
    }
}
