package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Enum.HouseColor;
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
    private byte playedCard, towersLeft;
    private final HouseColor houseColor;

    public PlayerClient(Player p, HouseColor teamColor, MatchConstants matchConstants) {
        this.nickName = p.toString();
        this.wizard = p.getWizard();
        this.usedCards = new boolean[matchConstants.numOfCards()];
        Arrays.fill(usedCards, false);
        this.entranceHall = new GameComponentClient(2 * wizard.ordinal());
        this.lunchHall = new GameComponentClient(2 * wizard.ordinal() + 1);
        this.towersLeft = (byte) matchConstants.towersForTeam();
        this.houseColor = teamColor;
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

    public void playCard(byte playedCard) {
        this.playedCard = playedCard;
        this.usedCards[playedCard - 1] = true;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public byte getTowersLeft() {
        return towersLeft;
    }

    public void setTowersLeft(byte towersLeft) {
        this.towersLeft = towersLeft;
    }

    public HouseColor getHouseColor() {
        return houseColor;
    }

    @Override
    public String toString() {
        return nickName;
    }
}
