package it.polimi.ingsw.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import it.polimi.ingsw.network.*;


public class ClientStub {
    public static void main(String[] args) throws IOException {
        Socket socket= new Socket("localhost",7896);
        System.out.println("Connessione riuscita");
        ObjectOutputStream objOut=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objIn= new ObjectInputStream(socket.getInputStream());
        int something=0;
        do{
            something=System.in.read();
            try {
                ToServerMessage send=new MoveFromCloud(2);
                objOut.writeObject(send);
                ToClientMessage received=(ToClientMessage)  objIn.readObject();
                System.out.println(received);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }while(something!=-1);
    }

}
