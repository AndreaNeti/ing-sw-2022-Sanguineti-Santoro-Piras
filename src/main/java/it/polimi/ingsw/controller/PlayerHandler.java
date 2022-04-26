package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class PlayerHandler implements Runnable {
    private final Socket socket;
    private final PrintWriter out;
    private String nickName;
    private boolean nickNameAlreadySet;
    private Controller controller;

    public PlayerHandler(Socket socket) {
        if (socket == null) throw new NullPointerException();
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        nickNameAlreadySet = false;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // what the server receives from the player
            String command;
            do {
                command = in.readLine();
                callMethod(command);
            } while (!command.equals("quit"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null)
                out.close();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendString(String s) {
        synchronized (out) {
            out.println(s);
            out.flush();
        }
    }

    private void callMethod(String command) {
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
                case "move" -> controller.move(Color.valueOf(tokens.get(0)),Integer.parseInt(tokens.get(1)));
                case "moveFromCloud"-> controller.moveFromCloud(Integer.parseInt(tokens.get(0)));
                case "playCharacter" -> controller.playCharacter();

                //Server methods
                case "nickName" -> {
                    Server.setNickname(tokens.get(0));
                    this.nickName(tokens.get(0));
                }
                case "getOldestMatchId" -> {
                    Long controllerId;
                    controllerId = Server.getOldestMatchId(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller = Server.getMatchById(controllerId);
                    if (controller.addPlayer(this)) {
                        Server.removeMatch(controllerId);
                    }
                }
                case "getMatchById" -> {
                    controller = Server.getMatchById(Long.parseLong(tokens.get(0)));
                    if (controller.addPlayer(this)) {
                        Server.removeMatch(Long.parseLong(tokens.get(0)));
                    }
                }
                case "createMatch" -> {
                    controller = Server.createMatch(new MatchType(Byte.parseByte(tokens.get(0)), Boolean.parseBoolean(tokens.get(1))));
                    controller.addPlayer(this);
                }
                //TODO ignore notExpertGameException
                default -> throw new UnexpectedValueException();
            }
            this.sendString("ok");

        } catch (GameException | IllegalArgumentException ex) {
            handleError(ex);
        } catch (NullPointerException ex) {
            this.sendString("error/Must join a match before");
        }
    }

    private void handleError(Exception e) {
        this.sendString("error/" + e.getMessage());
    }

    private void nickName(String nickName) {
        if (!this.nickNameAlreadySet)
            this.nickName = nickName;
        this.nickNameAlreadySet = true;
    }

    public String getNickName() {
        return nickName;
    }
}


