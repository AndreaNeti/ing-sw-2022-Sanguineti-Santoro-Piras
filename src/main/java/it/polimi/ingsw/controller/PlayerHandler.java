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
    private boolean quit;

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
            } while (!quit);

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
                case "move" -> controller.move(Color.valueOf(tokens.get(0)), Integer.parseInt(tokens.get(1)));
                case "moveFromCloud" -> controller.moveFromCloud(Integer.parseInt(tokens.get(0)));
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

    public void quit() {
        quit = true;
    }

    public String getNickName() {
        return nickName;
    }


    private class Quit implements Message {
        @Override
        public void execute() {
            quit();
        }
    }

    private class PlayCard implements Message {
        byte playedCard;

        @Override
        public void execute() throws GameException {
            controller.playCard(playedCard);
        }
    }

    private class ChooseCharacter implements Message {
        byte character;

        @Override
        public void execute() throws GameException {
            controller.chooseCharacter(character);
        }
    }

    private class SetCharacterInput implements Message {
        int input;

        @Override
        public void execute() throws GameException {
            controller.setCharacterInput(input);
        }
    }

    private class TextMessage implements Message {
        String message;

        @Override
        public void execute()  {
            controller.sendMessage(nickName, message);
        }
    }

    private class MoveMotherNature implements Message {
        int moves;

        @Override
        public void execute() throws GameException {
            controller.moveMotherNature(moves);
        }
    }

    private class Move implements Message {
        Color color;
        int idGameComponent;

        @Override
        public void execute() throws GameException {
            controller.move(color, idGameComponent);
        }
    }

    private class MoveFromCloud implements Message {
        int idGameComponent;

        @Override
        public void execute() throws GameException {
            controller.moveFromCloud(idGameComponent);
        }
    }

    private class PlayCharacter implements Message {
        @Override
        public void execute() throws GameException {
            controller.playCharacter();
        }
    }

    private class NickName implements Message {
        String nickName;

        @Override
        public void execute()  {
            nickName(nickName);
        }
    }

    private class GetOldestMatchId implements Message {
        MatchType matchType;

        @Override
        public void execute() throws GameException {
            Long controllerId;
            controllerId = Server.getOldestMatchId(matchType);
            controller = Server.getMatchById(controllerId);
            //TODO see if this this works
            if (controller.addPlayer(PlayerHandler.this)) {
                Server.removeMatch(controllerId);
            }
        }
    }

    private class GetMatchById implements Message {
        Long matchId;
        @Override
        public void execute() throws GameException {
            controller = Server.getMatchById(matchId);
            if (controller.addPlayer(PlayerHandler.this)) {
                Server.removeMatch(matchId);
            }
        }
    }
    private class CreateMatch implements Message{
        MatchType matchType;

        @Override
        public void execute() throws GameException {
            controller = Server.createMatch(matchType);
            controller.addPlayer(PlayerHandler.this);
        }
    }
}



