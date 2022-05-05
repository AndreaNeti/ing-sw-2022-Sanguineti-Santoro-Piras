package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.network.toClientMessage.ErrorException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerHandler implements Runnable, GameListener {
    private final Socket socket;
    private String nickName;
    private Controller controller;
    private boolean quit;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;

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
                update(new OK());
            } catch (GameException e1) {
                update(new ErrorException(e1.getMessage()));
            } catch (NullPointerException ex) {
                // controller or game are null somewhere
                if (controller == null)
                    update(new ErrorException("Must join a match before"));
                else
                    update(new ErrorException("Game not started yet"));
            } catch (IOException | ClassNotFoundException e) {
                if(controller!=null)
                    controller.disconnectEveryone(this);
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

    /*private void callMethod(String command) {
        List<String> tokens = Arrays.asList(command.split("/"));


        String methodString = tokens.remove(0);
        try {
            if (!nickNameAlreadySet && !methodString.equals("setNickName"))
                throw new NotAllowedException("Must set a setNickName first");
            switch (methodString) {
                //Controller methods
                case "playCard" -> controller.playCard(Byte.parseByte(tokens.get(0)));
                case "chooseCharacter" -> controller.chooseCharacter(Byte.parseByte(tokens.get(0)));
                case "setCharacterInput" -> controller.setCharacterInput(Integer.parseInt(tokens.get(0)));
                case "sendMessage" -> controller.sendMessage(setNickName, tokens.get(0));
                case "moveMotherNature" -> controller.moveMotherNature(Integer.parseInt(tokens.get(0)));
                case "move" -> controller.move(Color.valueOf(tokens.get(0)), Integer.parseInt(tokens.get(1)));
                case "moveFromCloud" -> controller.moveFromCloud(Integer.parseInt(tokens.get(0)));
                case "playCharacter" -> controller.playCharacter();

                //Server methods
                case "setNickName" -> {
                    Server.setNickName(tokens.get(0));
                    this.setNickName(tokens.get(0));
                }
                case "getOldestMatchId" -> {
                    Long controllerId;
                    controllerId = Server.getOldestMatchId(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller = Server.getMatchById(controllerId);
                    if (controller.addPlayer(this, setNickName)) {
                        Server.removeMatch(controllerId);
                    }
                }
                case "getMatchById" -> {
                    controller = Server.getMatchById(Long.parseLong(tokens.get(0)));
                    if (controller.addPlayer(this, setNickName)) {
                        Server.removeMatch(Long.parseLong(tokens.get(0)));
                    }
                }
                case "createMatch" -> {
                    controller = Server.createMatch(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller.addPlayer(this, setNickName);
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

    public void setNickName(String nickName) throws NotAllowedException {
        if (this.nickName == null) {
            Server.setNickName(nickName);
            this.nickName = nickName;
        }
    }

    public void quit() {
        quit = true;
        controller.removePlayer(this);
        controller = null;
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

    public void joinByMatchType(MatchType matchType) throws GameException {
        Long controllerId;
        controllerId = Server.getOldestMatchId(matchType);
        controller = Server.getMatchById(controllerId);
        if (controller.addPlayer(this, nickName)) {
            Server.removeMatch(controllerId);
        }
    }

    public void joinMatchId(Long matchId) throws GameException {
        controller = Server.getMatchById(matchId);
        if (controller.addPlayer(this, nickName)) {
            Server.removeMatch(matchId);
        }
    }

    public void createMatch(MatchType matchType) throws GameException {
        controller = Server.createMatch(matchType);
        controller.addPlayer(this, nickName);
    }
}