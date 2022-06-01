package it.polimi.ingsw.Client.PhaseAndComand.Commands;

import it.polimi.ingsw.Client.PhaseAndComand.Phases.ClientPhase;
import it.polimi.ingsw.Client.View.Cli.ViewCli;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.GamePhase;
import it.polimi.ingsw.exceptions.clientExceptions.SkipCommandException;
import it.polimi.ingsw.network.toServerMessage.*;

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
        public String toString() {
            return "Connect to server";
        }
    },
    SET_NICKNAME() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String nick = viewCli.getStringInput("Select nickname", 16, false);
            viewCli.sendToServer(new NickName(nick));
        }

        @Override
        public String toString() {
            return "Set nickname";
        }
    },
    CREATE_MATCH() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new CreateMatch(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public String toString() {
            return "Create match";
        }
    },
    JOIN_MATCH_BY_TYPE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new JoinMatchByType(viewCli.getMatchTypeInput(false)));
        }

        @Override
        public String toString() {
            return "Join match by type";
        }
    },
    JOIN_MATCH_BY_ID() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            long ID = viewCli.getLongInput("Write game ID", false);
            viewCli.sendToServer(new JoinMatchById(ID));
        }

        @Override
        public String toString() {
            return "Join match by ID";
        }
    },
    PLAY_CARD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new PlayCard(viewCli.getAssistantCardToPlayInput(false)));
        }

        @Override
        public String toString() {
            return "Play card";
        }
    },
    MOVE_STUDENT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            // request the student color
            Color color = viewCli.getColorInput(false);
            // request the destination where you want the student to move
            int destination = viewCli.getMoveStudentDestination(false);
            viewCli.sendToServer(new MoveStudent(color, destination));
        }

        @Override
        public String toString() {
            return "Move student";
        }
    },
    CHOOSE_CHARACTER() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            List<CharacterCardClient> characters = viewCli.getModel().getCharacters();
            int index = viewCli.getCharacterCharToPlayInput();
            viewCli.setCurrentCharacterCard(index);
            viewCli.sendToServer(new ChooseCharacter((byte) characters.get(index).getCharId()));
            viewCli.setPhaseInView(GamePhase.PLAY_CH_CARD_PHASE, false, false);
        }

        @Override
        public String toString() {
            return "Choose a character card";
        }
    },
    SET_CHARACTER_INPUT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            CharacterCardClient current = viewCli.getCurrentCharacterCard();
            if (current.isFull())
                viewCli.addMessage("Can't add more inputs to this character");
            else {
                current.setNextInput(viewCli);
                if (current.canPlay())
                    viewCli.addMessage("Card can already be played");
            }
            viewCli.repeatPhase(false);
        }

        @Override
        public String toString() {
            return "Set the input for the character";
        }
    },
    PLAY_CHARACTER() {
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
    },
    MOVE_MOTHER_NATURE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveMotherNature(viewCli.getMotherNatureMovesInput(false)));
        }

        @Override
        public String toString() {
            return "Move mother nature";
        }
    },
    MOVE_FROM_CLOUD() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            viewCli.sendToServer(new MoveFromCloud(viewCli.getCloudSource(false)));
        }

        @Override
        public String toString() {
            return "Move from cloud";
        }
    },
    TEXT_MESSAGE() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            String comment = viewCli.getStringInput("Comment", 50, false);
            viewCli.sendToServer(new TextMessageCS(comment));
            viewCli.addMessage("[You]: " + comment);
            viewCli.repeatPhase(false);
        }

        @Override
        public String toString() {
            return "Send message";
        }
    },
    UNDO() {
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
    },
    QUIT() {
        @Override
        public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
            if (viewCli.getBooleanInput("Quit?", false)) {
                viewCli.setQuit(false);
            } else
                viewCli.repeatPhase(false);
        }

        @Override
        public String toString() {
            return "Quit";
        }
    };

    public void playCLICommand(ViewCli viewCli) throws SkipCommandException {
    }

    public void attachToAPhase(List<ClientPhase> clientPhases) {
        for (ClientPhase clientPhase : clientPhases) {
            clientPhase.addCommand(this);
        }
    }
}
