package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.NotAllowedException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;
import it.polimi.ingsw.model.Color;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class PlayerHandler implements Runnable, GameListener {
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
            update(new Error(ex.getMessage()));
        } catch (NullPointerException ex) {
            update(new Error("Must join a match before"));
        }
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

    @Override
    public void update(Message m) {
        synchronized (out) {
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream so = new ObjectOutputStream(bo);
                so.writeObject(m);
                so.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class Quit implements Message {
        public Quit() {
        }

        @Override
        public void execute() {
            quit();
        }
    }

    private class PlayCard implements Message {
        byte playedCard;

        public PlayCard(byte playedCard) {
            this.playedCard = playedCard;
        }

        @Override
        public void execute() throws GameException {
            controller.playCard(playedCard);
        }
    }

    private class ChooseCharacter implements Message {
        byte character;

        public ChooseCharacter(byte character) {
            this.character = character;
        }

        @Override
        public void execute() throws GameException {
            controller.chooseCharacter(character);
        }
    }

    private class SetCharacterInput implements Message {
        int input;

        public SetCharacterInput(int input) {
            this.input = input;
        }

        @Override
        public void execute() throws GameException {
            controller.setCharacterInput(input);
        }
    }

    private class TextMessageCS implements Message {
        String message;

        public TextMessageCS(String message) {
            this.message = message;
        }

        @Override
        public void execute() {
            controller.sendMessage(nickName, message);
        }
    }

    private class MoveMotherNature implements Message {
        int moves;

        public MoveMotherNature(int moves) {
            this.moves = moves;
        }

        @Override
        public void execute() throws GameException {
            controller.moveMotherNature(moves);
        }
    }

    private class Move implements Message {
        Color color;
        int idGameComponent;

        public Move(Color color, int idGameComponent) {
            this.color = color;
            this.idGameComponent = idGameComponent;
        }

        @Override
        public void execute() throws GameException {
            controller.move(color, idGameComponent);
        }
    }

    private class MoveFromCloud implements Message {
        public MoveFromCloud(int idGameComponent) {
            this.idGameComponent = idGameComponent;
        }

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

        public NickName(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public void execute() {
            nickName(nickName);
        }
    }

    private class GetOldestMatchId implements Message {
        MatchType matchType;

        public GetOldestMatchId(MatchType matchType) {
            this.matchType = matchType;
        }

        @Override
        public void execute() throws GameException {
            Long controllerId;
            controllerId = Server.getOldestMatchId(matchType);
            controller = Server.getMatchById(controllerId);
            //TODO see if this this works
            if (controller.addPlayer(PlayerHandler.this, nickName)) {
                Server.removeMatch(controllerId);
            }
        }
    }

    private class GetMatchById implements Message {
        Long matchId;


        public GetMatchById(Long matchId) {
            this.matchId = matchId;
        }

        @Override
        public void execute() throws GameException {
            controller = Server.getMatchById(matchId);
            if (controller.addPlayer(PlayerHandler.this, nickName)) {
                Server.removeMatch(matchId);
            }
        }
    }

    private class CreateMatch implements Message {
        MatchType matchType;

        public CreateMatch(MatchType matchType) {
            this.matchType = matchType;
        }

        @Override
        public void execute() throws GameException {
            controller = Server.createMatch(matchType);
            controller.addPlayer(PlayerHandler.this, nickName);
        }
    }

    private class OK implements Message {
        String ok;

        public OK() {
            ok = "Paolinok";
        }

        @Override
        public void execute() {

        }
    }

    private class Error implements Message {
        String error;

        public Error(String error) {
            this.error = error;
        }

        @Override
        public void execute() {

        }
    }
}



