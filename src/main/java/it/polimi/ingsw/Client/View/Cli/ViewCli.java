package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.Controller.ControllerClient;
import it.polimi.ingsw.Client.GameClientListener;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Client.model.PlayerClient;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.network.toServerMessage.*;

import java.util.ArrayList;
import java.util.Scanner;

public class ViewCli implements GameClientListener {
    private final ControllerClient controllerClient;
    private final Scanner myInput = new Scanner(System.in);


    public ViewCli(ControllerClient controllerClient) {
        this.controllerClient = controllerClient;
    }

    public ToServerMessage switchNumber(int number) {
        switch (number) {
            case 0 -> {
                return new ChooseCharacter((byte) 1);
            }
            case 1 -> {
                return new MoveFromCloud(-3);
            }
            case 2 -> {
                return new Move(Color.BLUE, 4);
            }
            case 3 -> {
                MatchType mt = new MatchType((byte) myInput.nextInt(), myInput.nextBoolean());
                controllerClient.setMatchType(mt);
                return new CreateMatch(mt);
            }
            case 4 -> {
                return new JoinMatchById(myInput.nextLong());
            }
            case 5 -> {
                MatchType mt = new MatchType((byte) myInput.nextInt(), myInput.nextBoolean());
                controllerClient.setMatchType(mt);
                return new JoinMatchByType(mt);
            }
            case 6 -> {
                return new MoveMotherNature(3);
            }
            case 7 -> {
                return new NickName(new Scanner(System.in).next());
            }
            case 8 -> {
                return new PlayCard((byte) 3);
            }
            case 9 -> {
                return new SetCharacterInput(4);
            }
            case 10 -> {
                return new TextMessageCS(new Scanner(System.in).next());
            }
            case 11 -> {
                return new PlayCharacter();
            }
            case 12 -> {

                if (!controllerClient.connect(new byte[]{127, 0, 0, 1}, myInput.nextInt())) {
                    System.out.println("Cannot connect to this server");
                }
                return null;
            }
            default -> {
                return new Quit();
            }
        }
    }

    public void run() {
        System.out.println("You've chosen to play with command-line");


        int number;
        do {
            System.out.println("What do you want to do?");
            number = myInput.nextInt();
            ToServerMessage send = switchNumber(number);
            if (send != null)
                controllerClient.sendMessage(send);
        } while (number != -1);
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
        for (IslandClient i : islands) {
            System.out.println(i);
        }
    }

    @Override
    public void update(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void update(Wizard[] professors) {

    }

    @Override
    public void update(PlayerClient currentPlayer) {

    }

    @Override
    public void updateMembers(int membersLeftToStart) {
        if (membersLeftToStart > 0)
            System.out.println(membersLeftToStart + " members left before game starts");
        else System.out.println("Game is about to start");
    }

    @Override
    public void update(GamePhase gamePhase) {

    }

    @Override
    public void updateCardPlayed(Byte playedCard) {

    }

    @Override
    public void error(String e) {
        System.out.println("Operation failed because " + e);
    }

    @Override
    public void ok() {
        System.out.println("Successful operation");
    }


}
