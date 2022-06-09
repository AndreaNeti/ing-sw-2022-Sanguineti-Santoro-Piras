package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.SceneController.SceneController;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Server.controller.MatchType;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.GamePhase;
import it.polimi.ingsw.Util.IPAddress;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.network.toServerMessage.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.List;

public enum GameCommand {
    CONNECT_SERVER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            boolean connected = viewCli.connectToServer(viewCli.getIpAddressInput(false));
            if (!connected) {
                viewCli.addMessage("Server Error: Cannot connect to this server");
                viewCli.goToOldPhase();
            }
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputIp");
                String ipString = t.getText();
                if (ipString == null) {
                    showInputError();
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

        @Override
        public String toString() {
            return "Connect to server";
        }
    }, SET_NICKNAME() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String nick = viewCli.getStringInput("Select nickname", 16, false);
            viewCli.sendToServer(new NickName(nick));
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputNickName");
                String nick = t.getText();
                if (nick == null || nick.isBlank()) {
                    showInputError();
                    return;
                }
                viewGUI.sendToServer(new NickName(nick));
            };
        }

        @Override
        public String toString() {
            return "Set nickname";
        }
    }, CREATE_MATCH() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new CreateMatch(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                try {
                    RadioButton r = (RadioButton) GuiFX.getActiveSceneController().getElementById("#gameType");
                    boolean isExpert = r.getText().equals("ExpertGame");
                    RadioButton r1 = (RadioButton) GuiFX.getActiveSceneController().getElementById("#player");
                    byte players = Byte.parseByte(r1.getText());
                    viewGUI.sendToServer(new CreateMatch(new MatchType(players, isExpert)));
                } catch (IllegalArgumentException | NullPointerException ex) {
                    showInputError();
                }
            };
        }

        @Override
        public String toString() {
            return "Create match";
        }
    }, JOIN_MATCH_BY_TYPE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new JoinMatchByType(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                try {
                    RadioButton r = (RadioButton) GuiFX.getActiveSceneController().getElementById("#gameType");
                    boolean isExpert = r.getText().equals("ExpertGame");
                    RadioButton r1 = (RadioButton) GuiFX.getActiveSceneController().getElementById("#player");
                    byte players = Byte.parseByte(r1.getText());
                    viewGUI.sendToServer(new JoinMatchByType(new MatchType(players, isExpert)));
                } catch (IllegalArgumentException | NullPointerException ex) {
                    showInputError();
                }
            };
        }

        @Override
        public String toString() {
            return "Join match by type";
        }
    }, JOIN_MATCH_BY_ID() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            long ID = viewCli.getLongInput("Write game ID", false);
            viewCli.sendToServer(new JoinMatchById(ID));
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#inputIdMatch");
                try {
                    viewGUI.sendToServer(new JoinMatchById(Long.parseLong(t.getText())));
                } catch (IllegalArgumentException ex) {
                    showInputError();
                }
            };
        }

        @Override
        public String toString() {
            return "Join match by ID";
        }
    }, PLAY_CARD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput(false)));
        }

        @Override
        public String toString() {
            return "Play card";
        }
    }, MOVE_STUDENT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            // request the student color
            Color color = viewCli.getColorInput(false);
            // request the destination where you want the student to move
            GameComponentClient destination = viewCli.getMoveStudentDestination(false);
            viewCli.sendToServer(new MoveStudent(color, destination.getId(), destination.getNameOfComponent()));
        }

        @Override
        public String toString() {
            return "Move student";
        }
    }, CHOOSE_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            int index = viewCli.getCharacterCharToPlayInput();
            CharacterCardClient chosenCharacter = viewCli.getModel().getCharacters().get(index);
            viewCli.setCurrentCharacterCard(index);
            viewCli.sendToServer(new ChooseCharacter((byte) chosenCharacter.getCharId(), chosenCharacter.toString()));
            viewCli.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false, false);
        }

        @Override
        public String toString() {
            return "Choose a character card";
        }
    }, SET_CHARACTER_INPUT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            CharacterCardClient current = viewCli.getCurrentCharacterCard();
            if (current.isFull()) viewCli.addMessage("Can't add more inputs to this character");
            else {
                current.setNextInput(viewCli);
                if (current.canPlay()) viewCli.addMessage("Card can already be played");
            }
            viewCli.repeatPhase(false);
        }

        @Override
        public String toString() {
            return "Set the input for the character";
        }
    }, PLAY_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            CharacterCardClient current = viewCli.getCurrentCharacterCard();
            if (current == null) {
                viewCli.addMessage("No card selected");
                viewCli.goToOldPhase();
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
            viewCli.goToOldPhase();
        }

        @Override
        public String toString() {
            return "Play character";
        }
    }, MOVE_MOTHER_NATURE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveMotherNature(viewCli.getMotherNatureMovesInput(false)));
        }

        @Override
        public String toString() {
            return "Move mother nature";
        }
    }, MOVE_FROM_CLOUD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
        }

        @Override
        public String toString() {
            return "Move from cloud";
        }
    }, TEXT_MESSAGE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String comment = viewCli.getStringInput("Comment", 50, false);
            viewCli.sendToServer(new TextMessageCS(comment));
            viewCli.addMessage("[You]: " + comment);
            viewCli.repeatPhase(false);
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                SceneController sceneController = GuiFX.getActiveSceneController();
                TextField t = (TextField) sceneController.getElementById("#message");
                viewGUI.sendToServer(new TextMessageCS(t.getText()));
                viewGUI.addMessage("[You]: " + t.getText());
                t.clear();

            };
        }

        @Override
        public String toString() {
            return "Send message";
        }
    }, UNDO() {
        @Override
        public void playCLICommand(ViewCli viewCli) {
            if (viewCli.getCurrentCharacterCard() != null) {
                viewCli.addMessage("Reset all input");
                viewCli.getCurrentCharacterCard().resetInput();
                viewCli.unsetCurrentCharacterCard();
            }
            viewCli.goToOldPhase();
        }

        @Override
        public String toString() {
            return "Undo";
        }
    }, QUIT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            if (viewCli.getBooleanInput("Quit?", false)) {
                viewCli.setQuit(false);
            } else viewCli.repeatPhase(false);
        }

        @Override
        public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
            return actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Exit");
                alert.setContentText("Do you want to quit?");

                if (alert.showAndWait().filter(ButtonType.OK::equals).isPresent()) {
                    viewGUI.setQuit(false);
                }
            };
        }

        @Override
        public String toString() {
            return "Quit";
        }
    };

    public abstract void playCLICommand(ViewCli viewCli) throws SkipCommandException;

    //TODO set this function abstract
    public EventHandler<ActionEvent> getGUIHandler(ViewGUI viewGUI) {
        return null;
    }

    public void attachToAPhase(List<ClientPhase> clientPhases) {
        for (ClientPhase clientPhase : clientPhases) {
            clientPhase.addCommand(this);
        }
    }

    public void showInputError() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input fields empty");
        alert.setContentText("Input not valid");
        alert.showAndWait();
    }
}
