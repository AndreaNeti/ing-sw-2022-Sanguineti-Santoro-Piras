package it.polimi.ingsw.client.phaseAndComand.Commands;

import it.polimi.ingsw.client.model.CharacterCardClient;
import it.polimi.ingsw.client.model.GameComponentClient;
import it.polimi.ingsw.client.phaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.client.view.cli.ViewCli;
import it.polimi.ingsw.client.view.gui.GuiFX;
import it.polimi.ingsw.client.view.gui.SceneController.SceneController;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.network.toServerMessage.*;
import it.polimi.ingsw.utils.AssistantCard;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.IPAddress;
import it.polimi.ingsw.utils.MatchType;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * GameCommand enum represents all the possible commands that a client can execute inside the application. <br>
 * Each command has
 */
public enum GameCommand {
    /**
     * CONNECT_SERVER GameCommand is used by the client to connect to the server.
     */
    CONNECT_SERVER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            boolean connected = viewCli.connectToServer(viewCli.getIpAddressInput(false));
            if (!connected) {
                viewCli.addMessage("Server Error: Cannot connect to this server");
                viewCli.repeatPhase();
            }
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputIp");
                String ipString = t.getText();
                if (ipString == null) {
                    GuiFX.showError("Input fields empty", "Input not valid", "Error");
                    return;
                }
                byte[] ip = IPAddress.getIpFromString(ipString);
                if (ip == null) {
                    t.setText(null);
                } else {
                    if (!viewGUI.connectToServer(ip)) viewGUI.addMessage("Server Error: Cannot connect to this server");
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Connect to server".
         */
        @Override
        public String toString() {
            return "Connect to server";
        }
    },
    /**
     * SET_NICKNAME GameCommand is used to associate a nickname to the client's player.
     */
    SET_NICKNAME() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String nick = viewCli.getStringInput("Select nickname", 16, false);
            viewCli.sendToServer(new NickName(nick));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputNickName");
                String nick = t.getText();
                if (nick == null || nick.isBlank() || nick.length() > 16) {
                    GuiFX.showError("Input fields empty", "Input not valid", "Error");
                    return;
                }
                viewGUI.sendToServer(new NickName(nick));
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Set nickname".
         */
        @Override
        public String toString() {
            return "Set nickname";
        }
    },
    /**
     * CREATE_MATCH GameCommand is used to by the client create a new match of a specific type in the server.
     */
    CREATE_MATCH() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new CreateMatch(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                try {
                    RadioButton r = (RadioButton) GuiFX.getActiveSceneController().getElementById("#gameType");
                    boolean isExpert = r.getText().equals("Expert");
                    RadioButton r1 = (RadioButton) GuiFX.getActiveSceneController().getElementById("#player");
                    byte players = Byte.parseByte(r1.getText());
                    viewGUI.sendToServer(new CreateMatch(new MatchType(players, isExpert)));
                } catch (IllegalArgumentException | NullPointerException ex) {
                    GuiFX.showError("Input fields empty", "Input not valid", "Error");
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Create match".
         */
        @Override
        public String toString() {
            return "Create match";
        }
    },
    /**
     * JOIN_MATCH_BY_TYPE GameCommand is used by the client to join a match of a specific type available in the server.
     */
    JOIN_MATCH_BY_TYPE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new JoinMatchByType(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                try {
                    RadioButton r = (RadioButton) GuiFX.getActiveSceneController().getElementById("#gameType");
                    boolean isExpert = r.getText().equals("Expert");
                    RadioButton r1 = (RadioButton) GuiFX.getActiveSceneController().getElementById("#player");
                    byte players = Byte.parseByte(r1.getText());
                    viewGUI.sendToServer(new JoinMatchByType(new MatchType(players, isExpert)));
                } catch (IllegalArgumentException | NullPointerException ex) {
                    GuiFX.showError("Input fields empty", "Input not valid", "Error");
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Join match by type".
         */
        @Override
        public String toString() {
            return "Join match by type";
        }
    },
    /**
     * JOIN_MATCH_BY_ID GameCommand is used by the client to join a match with a specific ID available in the server.
     */
    JOIN_MATCH_BY_ID() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            long ID = viewCli.getLongInput("Write game ID", false);
            viewCli.sendToServer(new JoinMatchById(ID));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputIdMatch");
                try {
                    viewGUI.sendToServer(new JoinMatchById(Long.parseLong(t.getText())));
                } catch (IllegalArgumentException ex) {
                    GuiFX.showError("Input fields empty", "Input not valid", "Error");
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Join match by ID".
         */
        @Override
        public String toString() {
            return "Join match by ID";
        }
    },
    /**
     * PLAY_CARD GameCommand is used by the client to play an assistant card.
     */
    PLAY_CARD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput(false)));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                Node clicked = (Node) mouseEvent.getSource();
                AssistantCard assistantCard = (AssistantCard) clicked.getProperties().get("cardValue");
                viewGUI.sendToServer(new PlayCard(assistantCard));
                GuiFX.getActiveSceneController().getElementById("#assistantCardsBox").setVisible(false);
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Play card".
         */
        @Override
        public String toString() {
            return "Play card";
        }
    },
    /**
     * MOVE_STUDENT GameCommand is used by the client to move a student.
     */
    MOVE_STUDENT() {
        private Color color = null;

        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            // request the student color
            Color color = viewCli.getColorInput(false);
            // request the destination where you want the student to move
            GameComponentClient destination = viewCli.getMoveStudentDestination(false);
            viewCli.sendToServer(new MoveStudent(color, destination.getId(), destination.getNameOfComponent()));
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                sceneController.disableEverything();
                Node clicked = (Node) mouseEvent.getSource();
                Color c = (Color) clicked.getProperties().get("color");
                // has a color property, it's a student
                if (c != null) {
                    EventHandler<MouseEvent> moveStudentClick = GameCommand.MOVE_STUDENT.getGUIHandler(viewGUI);
                    viewGUI.enableEntrance(moveStudentClick);
                    sceneController.disableNode(clicked);
                    sceneController.selectNode(clicked);
                    // enable all islands
                    viewGUI.enableIslands(moveStudentClick);
                    // lunchHall is the third children
                    VBox lunchHall = (VBox) ((AnchorPane) sceneController.getElementById("#mainBoard")).getChildren().get(3);
                    sceneController.enableNode(lunchHall);
                    lunchHall.setOnMouseClicked(moveStudentClick);
                    color = c;
                } else {
                    // if an island is merged, relativeId is the one representing the "archipelago" (!= id)
                    Object relativeId = clicked.getProperties().get("relativeId");
                    int id;
                    if (relativeId != null) id = (int) relativeId;
                    else // it's the lunch hall
                        id = Integer.parseInt(clicked.getId());
                    viewGUI.sendToServer(new MoveStudent(color, id, (String) clicked.getProperties().get("name")));
                    color = null;
                    sceneController.disableEverything();
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Move student".
         */
        @Override
        public String toString() {
            return "Move student";
        }
    },
    /**
     * CHOOSE_CHARACTER GameCommand is used by the client to choose a character card to play.
     */
    CHOOSE_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            int index = viewCli.getCharacterCharToPlayInput();
            CharacterCardClient chosenCharacter = viewCli.getModel().getCharacters().get(index);
            if (chosenCharacter.getCost() <= viewCli.getModel().getCoinsPlayer((byte) viewCli.getModel().getMyWizard().ordinal())) {
                viewCli.setCurrentCharacterCard(index);
                viewCli.sendToServer(new ChooseCharacter(chosenCharacter.getCharId(), chosenCharacter.toString()));
                //viewCli.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false, false);
            } else {
                viewCli.addMessage("You don't have enough coins to play this card");
                viewCli.repeatPhase();
            }
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                Node clicked = (Node) mouseEvent.getSource();
                //clicked is the button-> the parent is the anchor pane of the card
                AnchorPane singleChar = (AnchorPane) clicked.getParent();
                int index = (int) singleChar.getProperties().get("index");
                CharacterCardClient chosenCharacter = viewGUI.getModel().getCharacters().get(index);
                if (chosenCharacter.getCost() <= viewGUI.getModel().getCoinsPlayer((byte) viewGUI.getModel().getCurrentPlayer().getWizard().ordinal())) {
                    viewGUI.setCurrentCharacterCard(index);
                    viewGUI.sendToServer(new ChooseCharacter(chosenCharacter.getCharId(), chosenCharacter.toString()));
                    //viewGUI.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false, false);
                } else GuiFX.showError("Character error", "You don't have enough money to play this card", "Error");
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Choose a character card".
         */
        @Override
        public String toString() {
            return "Choose a character card";
        }
    },
    /**
     * SET_CHARACTER_INPUT GameCommand is used by the client to add inputs to the chosen character card.
     */
    SET_CHARACTER_INPUT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            CharacterCardClient current = viewCli.getCurrentCharacterCard();
            if (current.isFull()) viewCli.addMessage("Can't add more inputs to this character");
            else {
                current.setNextInput(viewCli);
                if (current.canPlay()) viewCli.addMessage("Card can already be played");
            }
            viewCli.repeatPhase();
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return null;
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Set the input for the character".
         */
        @Override
        public String toString() {
            return "Set the input for the character";
        }
    },
    /**
     * PLAY_CHARACTER GameCommand is used by the client to play the chosen character card with the provided inputs.
     */
    PLAY_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            CharacterCardClient current = viewCli.getCurrentCharacterCard();
            if (current == null) {
                viewCli.addMessage("No card selected");
                viewCli.repeatPhase();
                return;
            }
            boolean confirmToPlay = viewCli.getBooleanInput("Confirm you want to play this character card?", false);
            if (current.canPlay() && confirmToPlay) {
                viewCli.sendToServer(new PlayCharacter(current.getInputs()));
                current.resetInput();
            } else {
                viewCli.addMessage("Card cannot be played because it needs more input");
            }
            viewCli.unsetCurrentCharacterCard();
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Play character");
                alert.setContentText("Do you want to play the card?");
                alert.initOwner(GuiFX.getPrimaryStage());
                CharacterCardClient current = viewGUI.getCurrentCharacterCard();
                if (alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
                    viewGUI.sendToServer(new PlayCharacter(current.getInputs()));
                    current.resetInput();
                    viewGUI.unsetCurrentCharacterCard();
                    AnchorPane singleChar = (AnchorPane) ((Node) mouseEvent.getSource()).getParent();
                    singleChar.getChildren().get(4).setVisible(false);
                    singleChar.getChildren().get(3).setVisible(false);
                } else {
                    viewGUI.repeatPhase();
                }

            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Play character".
         */
        @Override
        public String toString() {
            return "Play character";
        }
    },
    /**
     * MOVE_MOTHER_NATURE GameCommand is used by the client to move mother nature.
     */
    MOVE_MOTHER_NATURE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveMotherNature(viewCli.getMotherNatureMovesInput(false)));
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Move mother nature".
         */
        @Override
        public String toString() {
            return "Move mother nature";
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                Node clicked = (Node) mouseEvent.getSource();
                int moves = (int) clicked.getProperties().get("moves");
                viewGUI.sendToServer(new MoveMotherNature(moves));
            };
        }
    },
    /**
     * MOVE_FROM_CLOUD GameCommand is used by the client to move students from a cloud to the entrance hall.
     */
    MOVE_FROM_CLOUD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Move from cloud".
         */
        @Override
        public String toString() {
            return "Move from cloud";
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                GuiFX.getActiveSceneController().disableEverything();
                Node clicked = (Node) mouseEvent.getSource();
                int id = Integer.parseInt(clicked.getId());
                viewGUI.sendToServer(new MoveFromCloud(id));
            };
        }
    },
    /**
     * TEXT_MESSAGE GameCommand is used by the client to send a text message to the other clients in the match.
     */
    TEXT_MESSAGE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String comment = viewCli.getStringInput("Comment", 50, false);
            viewCli.sendToServer(new TextMessageCS(comment));
            viewCli.addMessage("[You]: " + comment);
            viewCli.repeatPhase();
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#message");

                if (!t.getText().isBlank()) {
                    viewGUI.sendToServer(new TextMessageCS(t.getText()));
                    viewGUI.addMessage("[You]: " + t.getText());
                }
                t.clear();
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Send message".
         */
        @Override
        public String toString() {
            return "Send message";
        }
    },
    /**
     * DESELECT_CHARACTER GameCommand is used by the client to undo the selection of a character card and the respective inputs.
     */
    DESELECT_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) {
            if (viewCli.getCurrentCharacterCard() != null) {
                viewCli.addMessage("Reset all input");
                viewCli.getCurrentCharacterCard().resetInput();
                viewCli.unsetCurrentCharacterCard();
                viewCli.sendToServer(new ChooseCharacter(null, ""));
            }
        }

        @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return mouseEvent -> {
                if (viewGUI.getCurrentCharacterCard() != null) {
                    viewGUI.addMessage("Reset all input");
                    viewGUI.getCurrentCharacterCard().resetInput();
                    viewGUI.unsetCurrentCharacterCard();

                    AnchorPane singleChar = (AnchorPane) ((Node) mouseEvent.getSource()).getParent();
                    singleChar.getChildren().get(4).setVisible(false);
                    singleChar.getChildren().get(3).setVisible(false);
                    viewGUI.sendToServer(new ChooseCharacter(null, ""));
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Undo".
         */
        @Override
        public String toString() {
            return "Undo";
        }
    },
    /**
     * QUIT GameCommand is used by the client to quit the current match if the client is in one or the application.
     */
    QUIT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            if (viewCli.getBooleanInput("Quit?", false)) {
                viewCli.setQuit(false);
            } else viewCli.repeatPhase();
        }

        // @Override
        public EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI) {
            return event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setContentText("Do you want to quit?");
                alert.initOwner(GuiFX.getPrimaryStage());
                if (alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
                    viewGUI.setQuit(false);
                }
            };
        }

        /**
         * Method toString returns the name of the command.
         *
         * @return {@code String} - "Quit".
         */
        @Override
        public String toString() {
            return "Quit";
        }
    };

    /**
     * Method playCLICommand requests the CLI client to provide the inputs required and then execute the command,
     * sending it to the server in a {@link ToServerMessage}.
     *
     * @param viewCli of type {@link ViewCli} - instance of the client's view (CLI).
     * @throws SkipCommandException if the command must be skipped before receiving all the inputs.
     */
    public abstract void playCLICommand(ViewCli viewCli) throws SkipCommandException;

    /**
     * Method getGUIHandler returns the event handler for a mouse event to add to a specific node of the GUI.
     *
     * @param viewGUI of type {@link ViewGUI} - instance of the client's view (GUI).
     * @return {@code EventHandler}<{@code MouseEvent}> - function that will be executed when the node that adds the
     * event handler is clicked.
     */
    public abstract EventHandler<MouseEvent> getGUIHandler(ViewGUI viewGUI);

    /**
     * Method attachToAPhase adds the command to the list of provided game phases, so that it can be executed during them.
     *
     * @param clientPhases of type {@code List}<{@link ClientPhase}> - list of the game phases to which the command is added.
     */
    public void attachToAPhase(List<ClientPhase> clientPhases) {
        for (ClientPhase clientPhase : clientPhases) {
            clientPhase.addCommand(this);
        }
    }

}
