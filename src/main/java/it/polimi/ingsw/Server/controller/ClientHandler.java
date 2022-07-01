package it.polimi.ingsw.Server.controller;

import it.polimi.ingsw.Util.MatchType;
import it.polimi.ingsw.exceptions.serverExceptions.EndGameException;
import it.polimi.ingsw.exceptions.serverExceptions.GameException;
import it.polimi.ingsw.exceptions.serverExceptions.NotAllowedException;
import it.polimi.ingsw.network.PingMessage;
import it.polimi.ingsw.network.PingPong;
import it.polimi.ingsw.network.PingPongInterface;
import it.polimi.ingsw.network.toClientMessage.ErrorException;
import it.polimi.ingsw.network.toClientMessage.OK;
import it.polimi.ingsw.network.toClientMessage.ToClientMessage;
import it.polimi.ingsw.network.toServerMessage.Quit;
import it.polimi.ingsw.network.toServerMessage.ToServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * ClientHandler class represents the 'middleman' between the user client and the controller, sending game updates to the client
 * and using the controller to modify the game through the user commands.
 */
public class ClientHandler implements Runnable, GameListener, PingPongInterface {
    // TODO: Maybe move here check for turn, nickname and match joined
    private final Socket socket;
    private String nickName;
    private Controller controller;
    private boolean quit;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;
    private final PingPong pingPong;

    /**
     * Constructor ClientHandler creates a new instance of ClientHandler.
     *
     * @param socket of type {@code Socket} - socket connection between the server and the client.
     */
    public ClientHandler(Socket socket) throws IOException {
        if (socket == null) throw new NullPointerException();
        this.socket = socket;
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
        pingPong = new PingPong(this);
        System.out.println("Connected with client " + socket.getPort());
    }

    /**
     * Method run waits for a player's command ({@link ToServerMessage}) to be received via socket connection and then executes it to
     * modify the game through the game controller. <br>
     * This method will end only after the player sends a {@link Quit} command while not in a game or if there's an error in the socket
     * connection, closing the socket and relative data streams. <br>
     * If there's an exception is thrown while trying to read a command and execute it, the controller will end the game if it was
     * an {@link EndGameException}, else the error exception will be sent to the clients.
     */
    @Override
    public void run() {
        // notifyGameComponent connection ok
        update(new OK());
        // what the server receives from the player
        ToServerMessage command;
        do {
            try {
                command = (ToServerMessage) objIn.readObject();
                if (!(command instanceof PingMessage))
                    System.out.println(command.getClass().getName());
                command.execute(this);
            } catch (EndGameException e) {
                // if the exception arrives here it means it was an end instantly exception
                System.out.println(e.getMessage());
                controller.endGame();
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
        pingPong.quit();
        Server.removeNickName(nickName);
        if (objIn != null) {
            try {
                objIn.close();
            } catch (IOException e) {
                System.err.println("Couldn't close Object Input Stream");
            }
        }
        if (objOut != null) {
            try {
                objOut.close();
            } catch (IOException e) {
                System.err.println("Couldn't close Object Output Stream");
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Couldn't close Socket");
        }
    }

    /**
     * Method setNickName adds the player's nickname to the client handler.
     *
     * @param nickName of type {@code String} - nickname of the player.
     * @throws NotAllowedException if the nickname has been already set.
     */
    public void setNickName(String nickName) throws NotAllowedException {
        if (this.nickName == null) {
            Server.setNickName(nickName);
            this.nickName = nickName;
        } else {
            throw new NotAllowedException("Nickname already set");
        }
        update(new OK());
    }

    /**
     * Method quit is used to quit the game if a game controller is present in the client handler, else
     * the method closes the connection between the server and the client.
     */
    public void quit() {
        if (controller == null) {
            // is quitting connection with server
            pingPong.quit();
            quit = true;
        } else {
            // is quitting match
            controller.disconnectPlayerQuit(this);
            controller = null;
        }
    }

    /**
     * Method getNickName returns the nickname of the client handler's player.
     *
     * @return {@code String} - nickname of the client handler.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Method update sends a message from the server to the client via socket connection.
     *
     * @param message of type {@link ToClientMessage} - message to send to the client.
     */
    @Override
    public synchronized void update(ToClientMessage message) {
        if (!(message instanceof PingMessage))
            System.out.println(message);
        try {
            objOut.reset();
            objOut.writeObject(message);
            objOut.flush();
        } catch (IOException e) {
            System.err.println("Can't send to the client");
        }

    }

    /**
     * Method getController returns the game controller associated with the client handler.
     *
     * @return {@link Controller} - controller associated with the client handler.
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Method joinByMatchType adds the handler to the oldest game available that satisfy the match type selected.
     *
     * @param matchType of type {@link MatchType} - type of game the player wants to join.
     * @throws GameException if the client handler has already a game controller set or if the client
     *                       cannot be added to the game.
     */
    public void joinByMatchType(MatchType matchType) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        Long controllerId = Server.getOldestMatchId(matchType);
        joinMatch(Server.getMatchById(controllerId));
    }

    /**
     * Method joinByMatchId adds the handler to the game with the ID selected.
     *
     * @param matchId of type {@code Long} - ID of the game the player wants to join.
     * @throws GameException if the client handler has already a game controller set or if the client
     *                       cannot be added to the game.
     */
    public void joinByMatchId(Long matchId) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        joinMatch(Server.getMatchById(matchId));
    }

    /**
     * Method createMatch creates a new game with the specified match type and adds the handler to it.
     *
     * @param matchType of type {@link MatchType} - type of game the player wants to create.
     * @throws GameException if the client handler has already a game controller set or if the client
     *                       cannot be added to the game created.
     */
    public void createMatch(MatchType matchType) throws GameException {
        if (controller != null) throw new NotAllowedException("Already joined a match");
        joinMatch(Server.createMatch(matchType));
    }


    /**
     * Method joinMatch adds the game controller selected in the handler.
     *
     * @param match of type {@link Controller} - game controller to add in the handler.
     * @throws GameException if the player (and respective handler) cannot be added in the selected controller.
     */
    private void joinMatch(Controller match) throws GameException {
        controller = match;
        controller.addPlayer(this, nickName);
    }

    /**
     * Method equals is used to compare two ClientHandlers, based on their unique socket.
     *
     * @param o of type {@code Object} - instance of the other Object.
     * @return {@code boolean} - true if the other object is a ClientHandler and has the same socket of the handler.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientHandler that)) return false;
        return Objects.equals(socket, that.socket);
    }

    /**
     * Method hasCode returns the hash code obtained from the handler's socket.
     *
     * @return {@code int} - hash code of the handler's socket.
     */
    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }

    /**
     * Method sendPingPong sends a {@link PingMessage} to the client.
     */
    @Override
    public void sendPingPong() {
        update(new PingMessage());
    }

    @Override
    public void resetPing() {
        pingPong.resetTime();
    }
}