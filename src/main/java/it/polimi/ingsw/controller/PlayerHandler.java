package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EndGameException;
import it.polimi.ingsw.exceptions.GameException;
import it.polimi.ingsw.exceptions.UnexpectedValueException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

            String command;
            do {
                command = in.readLine();

            } while (command != null && !command.equals("quit"));

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

    protected Socket getSocket() {
        synchronized (socket) {
            return socket;
        }
    }

    protected void sendString(String s) {
        synchronized (out) {
            out.println(s);
            out.flush();
        }
    }

    private void callMethod(String comand) throws UnexpectedValueException {
        List<String> tokens = Arrays.asList(comand.split("/"));


        String methodString = tokens.remove(0);
        try {
            switch (methodString) {
                case "playCard": {
                    controller.playCard(tokens.get(0));
                }
                case "chooseCharacter": {
                    controller.chooseCharacter(tokens.get(0));
                }
                case "setCharacterInput": {
                    controller.setCharacterInput(tokens.get(0));
                }
                case "sendMessage": {
                    controller.sendMessage(tokens.get(0));
                }
                case "moveMotherNature": {
                    controller.moveMotherNature(tokens.get(0));
                }
                case "move": {
                    controller.move(tokens.get(0), tokens.get(1));
                }
                case "playCharacter":{
                    controller.playCharacter();
                }
            }
        } catch (GameException ex) {
            handleError(ex);
        }
        finally {
            this.sendString("ok");
        }


    }

    private void handleError(GameException e) {
        this.sendString(e.getMessage());
    }

    private void nickName(String nickName) {
        if (!this.nickNameAlreadySet)
            this.nickName = nickName;
        this.nickNameAlreadySet = true;
    }
}


