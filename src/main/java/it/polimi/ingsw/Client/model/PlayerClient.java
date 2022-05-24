package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.Server.controller.MatchConstants;
import it.polimi.ingsw.Server.model.Player;

import java.util.Arrays;

public class PlayerClient {
    private final String nickName;
    private final Wizard wizard;
    private final boolean[] usedCards;
    private final GameComponentClient entranceHall;
    private final GameComponentClient lunchHall;
    private byte playedCard;

    public PlayerClient(Player p, MatchConstants matchConstants) {
        this.nickName = p.toString();
        this.wizard = p.getWizard();
        this.usedCards = new boolean[matchConstants.numOfCards()];
        Arrays.fill(usedCards, false);
        this.entranceHall = new GameComponentClient(2 * wizard.ordinal());
        this.lunchHall = new GameComponentClient(2 * wizard.ordinal() + 1);
    }

    public GameComponentClient getEntranceHall() {
        return entranceHall;
    }

    public GameComponentClient getLunchHall() {
        return lunchHall;
    }

    public byte getPlayedCard() {
        return playedCard;
    }

    public boolean[] getUsedCards() {
        return Arrays.copyOf(usedCards, usedCards.length);
    }

    public void playCard(byte playedCard) {
        this.playedCard = playedCard;
        this.usedCards[playedCard - 1] = true;
    }

    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String toString() {
        return nickName;
    }

    public byte getPlayedCardMoves() {
        // card max movement is 1 for card 1 and 2, 2 for card 3 and 4, ..., 5 for card 9 and 10
        return (byte) ((playedCard + 1) / 2);
    }
}
