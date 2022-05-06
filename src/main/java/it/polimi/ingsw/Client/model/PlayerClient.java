package it.polimi.ingsw.Client.model;

import it.polimi.ingsw.Server.model.HouseColor;
import it.polimi.ingsw.Server.model.Wizard;

import java.util.Arrays;

public class PlayerClient {
    private final String nickName;
    private final Wizard wizard;
    private final boolean[] usedCards;
    private final GameComponentClient entranceHall;
    private final GameComponentClient lunchHall;
    private byte playedCard;
    private byte towersLeft;
    private final HouseColor houseColor;

    public PlayerClient(String nickName, Wizard wizard, byte numberOfCards, GameComponentClient entranceHall, GameComponentClient lunchHall, byte playedCard, byte towersLeft, HouseColor houseColor) {
        this.nickName = nickName;
        this.wizard = wizard;
        this.usedCards = new boolean[numberOfCards];
        Arrays.fill(usedCards,false);
        this.entranceHall = entranceHall;
        this.lunchHall = lunchHall;
        this.playedCard = playedCard;
        this.towersLeft=towersLeft;
        this.houseColor = houseColor;
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
        this.usedCards[playedCard-1]=true;
    }

    public String getNickName() {
        return nickName;
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
}
