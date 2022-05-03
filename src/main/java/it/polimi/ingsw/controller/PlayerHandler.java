package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class PlayerHandler implements Runnable, GameListener {
    private final Socket socket;
    private String nickName;
    private boolean nickNameAlreadySet;
    private Controller controller;
    private boolean quit;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;
    private GameDelta gameDelta;

    public PlayerHandler(Socket socket) {
        if (socket == null) throw new NullPointerException();
        this.socket = socket;
        try {
            objIn = new ObjectInputStream(socket.getInputStream());
            objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        nickNameAlreadySet = false;
        System.out.println("connesso con client " + socket.getPort());
    }

    @Override
    public void run() {
        // what the server receives from the player
        ToServerMessage command;
        do {
            try {
                command = (ToServerMessage) objIn.readObject();
                System.out.println(command);
                command.execute(this);
                this.update(new OK());
            } catch (GameException e1) {
                this.update(new ErrorException(e1.getMessage()));
            } catch (NullPointerException ex) {
                update(new ErrorException("Must join a match before"));
            } catch (IOException | ClassNotFoundException e) {
                controller.disconnectEveryone(this);
                // cannot receive a quit message because it's disconnected
                quit = true;
            }
        }
        while (!quit);
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

    /*private void callMethod(String command) {
        List<String> tokens = Arrays.asList(command.split("/"));


        String methodString = tokens.remove(0);
        try {
            if (!nickNameAlreadySet && !methodString.equals("nickName"))
                throw new NotAllowedException("Must set a nickName first");
            switch (methodString) {
                //Controller methods
                case "playCard" -> controller.playCard(Byte.parseByte(tokens.get(0)));
                case "chooseCharacter" -> controller.chooseCharacter(Byte.parseByte(tokens.get(0)));
                case "setCharacterInput" -> controller.setCharacterInput(Integer.parseInt(tokens.get(0)));
                case "sendMessage" -> controller.sendMessage(nickName, tokens.get(0));
                case "moveMotherNature" -> controller.moveMotherNature(Integer.parseInt(tokens.get(0)));
                case "move" -> controller.move(Color.valueOf(tokens.get(0)), Integer.parseInt(tokens.get(1)));
                case "moveFromCloud" -> controller.moveFromCloud(Integer.parseInt(tokens.get(0)));
                case "playCharacter" -> controller.playCharacter();

                //Server methods
                case "nickName" -> {
                    Server.setNickNames(tokens.get(0));
                    this.nickName(tokens.get(0));
                }
                case "getOldestMatchId" -> {
                    Long controllerId;
                    controllerId = Server.getOldestMatchId(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller = Server.getMatchById(controllerId);
                    if (controller.addPlayer(this, nickName)) {
                        Server.removeMatch(controllerId);
                    }
                }
                case "getMatchById" -> {
                    controller = Server.getMatchById(Long.parseLong(tokens.get(0)));
                    if (controller.addPlayer(this, nickName)) {
                        Server.removeMatch(Long.parseLong(tokens.get(0)));
                    }
                }
                case "createMatch" -> {
                    controller = Server.createMatch(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller.addPlayer(this, nickName);
                }
                //TODO ignore notExpertGameException
                default -> throw new UnexpectedValueException();
            }
            update(new OK());

        } catch (GameException | IllegalArgumentException ex) {
            update(new ErrorException(ex.getMessage()));
        } catch (NullPointerException ex) {
            update(new ErrorException("Must join a match before"));
        }
    }*/

    public void nickName(String nickName) {
        if (!this.nickNameAlreadySet)
            this.nickName = nickName;
        this.nickNameAlreadySet = true;
    }

    public void quit() {
        quit = true;
        controller.removePlayer(this);
        //send an ACK to confirm
        update(new OK());
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public void update(ToClientMessage m) {
        synchronized (objOut) {
            try {
                objOut.writeObject(m);
                objOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("can't send to the client");
            }
        }
    }

    public Controller getController() {
        return controller;
    }

    public void joinOldestMatchId(MatchType matchType) throws GameException {
        Long controllerId;
        controllerId = Server.getOldestMatchId(matchType);
        controller = Server.getMatchById(controllerId);
        //TODO see if this this works
        if (controller.addPlayer(PlayerHandler.this, nickName)) {
            gameDelta=controller.getGameDelta();
            gameDelta.addListener(this);
            Server.removeMatch(controllerId);
        }
    }

    public void joinMatchId(Long matchId) throws GameException {
        controller = Server.getMatchById(matchId);
        if (controller.addPlayer(PlayerHandler.this, nickName)) {
            gameDelta=controller.getGameDelta();
            gameDelta.addListener(this);
            Server.removeMatch(matchId);
        }
    }

    public void createMatch(MatchType matchType) throws GameException {
        controller = Server.createMatch(matchType);
        controller.addPlayer(PlayerHandler.this, nickName);
        gameDelta=controller.getGameDelta();
        gameDelta.addListener(this);
    }
}