package it.polimi.ingsw.Server.model;


import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.serverExceptions.UsedCardException;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    private final String nickName;
    private final Wizard wizard;
    private final List<AssistantCard> assistantCards;
    private AssistantCard playedCard;
    private transient final EntranceHall entranceHall;
    private transient final LunchHall lunchHall;
    private transient byte cardsLeft;
    private transient final MatchConstants matchConstants;

    public Player(String nickName, Team team, Wizard wizard, MatchConstants matchConstants) throws GameException {
        if (nickName == null || team == null || matchConstants == null)
            throw new IllegalArgumentException("Cannot pass null argument");
        this.matchConstants = matchConstants;
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

    public void useCard(byte value) throws UsedCardException, NotAllowedException, EndGameException {

        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        AssistantCard chosenCard = getCard(value);
        if (chosenCard == null) throw new IllegalArgumentException("Not a valid card to play");

        if (chosenCard.isUsed()) throw new UsedCardException();
        playedCard = chosenCard;
        chosenCard.setUsed();
        cardsLeft--;
        if (cardsLeft == 0) throw new EndGameException(false);
    }

    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    // checks if a card can be played in function of the cards chosen by other players
    public boolean canPlayCard(ArrayList<Byte> playedCardsValues, byte value) {
        if (playedCardsValues == null) throw new IllegalArgumentException("Passing null played cards list");
        // the value goes from 1 to 10
        if (value < 1 || value > matchConstants.numOfCards())
            throw new IllegalArgumentException("Not a valid card to play");
        // you have already used that card
        if (getCard(value).isUsed()) return false;
        // no one has already played a card, you can play it
        if (playedCardsValues.size() == 0) return true;
        // if the card you chose is already chosen by someone else, check if you hadn't other choices
        if (playedCardsValues.contains(value)) {
            for (byte val = 1; val <= matchConstants.numOfCards(); val++) {
                AssistantCard c = getCard(val);
                // there is a card not played by other player that you still didn't choose, you should play that one instead
                if (c != null && !c.isUsed() && !playedCardsValues.contains(val)) {
                    return false;
                }
            }
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

    private AssistantCard getCard(byte value) {
        if (value < 1 || value > matchConstants.numOfCards()) return null;
        // card 1 is in position 0, card 10 in 9
        return assistantCards.get(value - 1);
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
