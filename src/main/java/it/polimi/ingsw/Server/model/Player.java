package it.polimi.ingsw.Server.model;


import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Player implements Serializable {
    private final String nickName;
    private final Wizard wizard;
    private transient final boolean[] cardsAvailable;
    private transient final EntranceHall entranceHall;
    private transient final LunchHall lunchHall;
    private transient byte playedCard;
    private transient byte cardsLeft;

    public Player(String nickName, Team team, Wizard wizard, MatchConstants matchConstants) throws GameException {
        if (nickName == null || team == null || matchConstants == null)
            throw new UnexpectedValueException();
        this.nickName = nickName;
        team.addPlayer(this);
        this.wizard = wizard;
        this.cardsAvailable = new boolean[matchConstants.numOfCards()];
        Arrays.fill(this.cardsAvailable, true);
        this.cardsLeft = (byte) matchConstants.numOfCards();
        this.playedCard = 0; // 0 = no card, else 1 to 10
        this.entranceHall = new EntranceHall(matchConstants.entranceHallStudents(), (byte) (wizard.ordinal() * 2));
        this.lunchHall = new LunchHall(Color.values().length * matchConstants.maxLunchHallStudents(), (byte) (wizard.ordinal() * 2 + 1));
    }

    public void useCard(byte card) throws UsedCardException, UnexpectedValueException, NotAllowedException, EndGameException {
        if (card < 1 || card > 10) throw new UnexpectedValueException();
        if (cardsLeft < 1) throw new NotAllowedException("No more cards");
        if (!cardsAvailable[card - 1]) throw new UsedCardException();
        playedCard = card;
        cardsLeft--;
        cardsAvailable[card - 1] = false;
        if (cardsLeft == 0) throw new EndGameException(false);
    }

    public byte getPlayedCardMoves() {
        // card max movement is 1 for card 1 and 2, 2 for card 3 and 4, ..., 5 for card 9 and 10
        return (byte) ((playedCard + 1) / 2);
    }

    public byte getPlayedCard() {
        return playedCard;
    }

    public boolean canPlayCard(ArrayList<Byte> playedCards, byte value) {
        //the value goes from 1 to 10
        if (playedCards.size() == 0)
            return true;
        if (playedCards.contains(value)) {
            for (int i = 0; i < cardsAvailable.length; i++) {
                if (cardsAvailable[i] && !playedCards.contains((byte) (i + 1))) {
                    // there is a different card -> you should have played that one
                    return false;
                }
            }
        }
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
