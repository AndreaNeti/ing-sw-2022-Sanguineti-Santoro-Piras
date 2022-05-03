package it.polimi.ingsw.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.controller.MatchType;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;
import it.polimi.ingsw.network.*;


public class ClientStub {
    public static ToServerMessage switchNumber(int number){
        switch (number){
            case 0 -> {
                return new ChooseCharacter((byte) 1);
            }
            case 1->{
                return new MoveFromCloud(-3);
            }
            case 2->{
                return new Move(Color.BLUE,4);
            }
            case 3->{
                return new CreateMatch(new MatchType((byte)3, true));
            }
            case 4->{
                return new GetMatchById(45L);
            }
            case 5->{
                return new GetOldestMatchId(new MatchType((byte) 3, true));
            }
            case 6 ->{
                return new MoveMotherNature(3);
            }
            case 7->{
                return new NickName("gianpaolino");
            }
            case 8 ->{
                return new PlayCard((byte )3);
            }
            case 9 ->{
                return new SetCharacterInput(4);
            }
            case 10 ->{
                return new TextMessageCS("ciao");
            }
            case 11-> {
                return new PlayCharacter();
            }
            default -> {
                return new Quit();
            }
        }

    }
    public static void main(String[] args) throws IOException {
        Socket socket= new Socket("localhost",4206);
        System.out.println("Connessione riuscita");
        ObjectOutputStream objOut=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn= new ObjectInputStream(socket.getInputStream());
        int number;
        do{
            number =System.in.read();
            try {
                ToServerMessage send=switchNumber(number);
                objOut.writeObject(send);
                GameComponentClient received=Adapter.transform((GameComponent)  objIn.readObject());
                System.out.println(received);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }while(number !=-1);
    }


}
