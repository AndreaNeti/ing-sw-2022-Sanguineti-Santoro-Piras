package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Enum.Wizard;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.network.toClientMessage.ErrorException;
import it.polimi.ingsw.network.toClientMessage.MatchInfo;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable, GameListener {
    // TODO: Maybe move here check for turn, nickname and match joined
    private final Socket socket;
    private String nickName;
    private Controller controller;
    private boolean quit;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;

    public ClientHandler(Socket socket) {
        if (socket == null) throw new NullPointerException();
        this.socket = socket;
        try {
            objOut = new ObjectOutputStream(socket.getOutputStream());
            objIn = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Connected with client " + socket.getPort());
    }

    @Override
    public void run() {
        // notify connection ok
        update(new OK());
        // what the server receives from the player
        ToServerMessage command;
        do {
            try {
                command = (ToServerMessage) objIn.readObject();
                System.out.println(command);
                command.execute(this);

            } catch (GameException e1) {
                update(new ErrorException(e1.getMessage()));
            } catch (NullPointerException ex) {
                ex.printStackTrace();
                // controller or game are null somewhere
                if (controller == null)
                    update(new ErrorException("Must join a match before"));
                else
                    update(new ErrorException("Game not started yet"));
            } catch (IOException | ClassNotFoundException e) {
                if (controller != null)
                    controller.disconnectPlayerQuit(this);
                // cannot receive a quit message because it's disconnected
                quit = true;
            }
        }
        while (!quit);
        Server.removeNickName(nickName);
        if (objIn != null) {
            try {
                objIn.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (objOut != null) {
            try {
                objOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setNickName(String nickName) throws NotAllowedException {
        if (this.nickName == null) {
            Server.setNickName(nickName);
            this.nickName = nickName;
        } else {
            throw new NotAllowedException("Nickname already set");
        }
        update(new OK());
    }

    public void quit() {
        if (controller == null) quit = true;
        else {
            controller.disconnectPlayerQuit(this);
            controller = null;
        }
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public void update(ToClientMessage m) {
        System.out.println(m);
        try {
            objOut.reset();
            objOut.writeObject(m);
            objOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("can't send to the client");
        }

    }

    public Controller getController() {
        return controller;
    }

    public void joinByMatchType(MatchType matchType) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        Long controllerId = Server.getOldestMatchId(matchType);
        joinMatch(Server.getMatchById(controllerId));
    }

    public void joinByMatchId(Long matchId) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        joinMatch(Server.getMatchById(matchId));
    }

    public void createMatch(MatchType matchType) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        joinMatch(Server.createMatch(matchType));
    }

    private void joinMatch(Controller match) throws GameException {
        controller = match;
        update(new MatchInfo(controller.getMatchType(), controller.getMatchId(), Wizard.values()[controller.getPlayersInMatch()]));
        update(new OK());
        controller.addPlayer(this, nickName);
    }
}