package it.polimi.ingsw.Client;

import it.polimi.ingsw.controller.MatchType;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class ClientStub {
    public static ToServerMessage switchNumber(int number) {
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
                return new CreateMatch(new MatchType((byte) 3, true));
            }
            case 4 -> {
                return new JoinMatchById(45L);
            }
            case 5 -> {
                return new JoinMatchByType(new MatchType((byte) 3, true));
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
            default -> {
                return new Quit();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4206);
        System.out.println("Connessione riuscita");
        ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
        int number;
        Scanner myInput = new Scanner(System.in);
        do {
            number = myInput.nextInt();
            try {
                ToServerMessage send = switchNumber(number);
                objOut.writeObject(send);
                ToClientMessage received = (ToClientMessage) objIn.readObject();
                System.out.println(received);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } while (number != -1);
    }


}
