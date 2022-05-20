package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;

import java.util.ArrayList;

public record UpdateCli(ViewCli viewCli) implements GameClientListener {


    @Override
    public void error(String e) {
        System.out.println("Operation failed because " + e);
    }

    @Override
    public void ok() {
        System.out.println("Successful operation");
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
        System.out.println("New motherNaturePosition is " + motherNaturePosition);
    }

    @Override
    public void update(GameComponentClient gameComponent) {
        System.out.println("New gameComponent is " + gameComponent);
    }

    @Override
    public void update(IslandClient island) {
        System.out.println("New gameComponent is " + island);
    }

    @Override
    public void update(ArrayList<IslandClient> islands) {
        System.out.println("Printing all the island");
        for (IslandClient i : islands) {
            System.out.println(i);
        }
    }

    @Override
    public void update(HouseColor houseColor, Byte towerLefts) {
        if (towerLefts > 1)
            System.out.println("Team " + houseColor + "has now " + towerLefts + " towers");
        else
            System.out.println("Team " + houseColor + "has now " + towerLefts + " tower");
    }

    @Override
    public void update(Color color, Wizard wizard) {
        System.out.println(color + " professor is owned by " + wizard);

    }

    @Override
    public void update(String currentPlayer, boolean isMyTurn) {
        if (isMyTurn) System.out.println("It's your turn");
        else System.out.println(currentPlayer + " is now playing his turn");
    }

    @Override
    public void updateMembers(int membersLeftToStart) {
        if (membersLeftToStart > 0)
            System.out.println(membersLeftToStart + " members left before game starts");
        else System.out.println("Game is about to start");
    }

    @Override
    public void updateCardPlayed(Byte playedCard) {
        System.out.println(viewCli.getModel().getCurrentPlayer().getWizard() + " played assistant card " + playedCard);
    }

}
