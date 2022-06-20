package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.View.GameClientListener;
import it.polimi.ingsw.Client.model.*;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import org.fusesource.jansi.AnsiConsole;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CliPrinter implements GameClientListener {


    private final static String operatingSystem = System.getProperty("os.name");
    private final ViewCli view;
    private GameClientView game;
    private final PrintStream out;

    public CliPrinter(ViewCli view) {
        this.view = view;
        if (operatingSystem.contains("Windows")) {
            try {
                // set terminal to utf8
                new ProcessBuilder("cmd", "/c", "chcp 65001").inheritIO().start().waitFor();
            } catch (InterruptedException | IOException e) {
                System.err.println(e.getMessage());
            }
        }
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    private void printLobby() {
        synchronized (view.getChat()) {
            ArrayList<String> chatCopy = view.getChat();
//        Collections.reverse(chatCopy);
            StringBuilder chat = new StringBuilder();
            chat.append(" \u250c").append("\u2500".repeat(39)).append("\u2524 CHAT \u251c").append("\u2500".repeat(39)).append("\u2510\n");
            String mex;
            for (int i = chatCopy.size() - 15; i < chatCopy.size(); i++) {
                if (i >= 0) {
                    mex = chatCopy.get(i);
                    chat.append(" \u2502 ").append(mex).append(" ".repeat(85 - mex.length())).append("\u2502\n");
                } else {
                    chat.append(" \u2502 ").append(" ".repeat(85)).append("\u2502\n");
                }
            }
            chat.append(" \u2514").append("\u2500".repeat(86)).append("\u2518\n\n");
            out.print(chat);
        }
    }

    public void printGame() {
        clearConsole();
        game = view.getModel();
        if (game != null) {
//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
            AnsiConsole.systemInstall();
            List<PlayerClient> players = game.getPlayers();
            out.print(printIslands(game.getIslands()));
            out.print(printCloudsAndTeams(game.getClouds(), game.getTeams()));
            out.print(printBoardsChatCharacters(players));
            out.print(printAssistantCards(players));
            AnsiConsole.systemUninstall();
        } else {
            printLobby();
        }
        view.setMustReprint(false);
    }

    private StringBuilder printIslands(ArrayList<IslandClient> islands) {
        StringBuilder islandPrint = new StringBuilder();
        // print island number and prohibitions
        for (int i = 0; i < islands.size(); i++) {
            String space = i < 9 ? " " : "";
            islandPrint.append("   \u001b[4m#:").append(i + 1).append(space).append(" \u001b[31mX\u001b[0m\u001b[4m:").
                    append(islands.get(i).getProhibition()).append("\u001b[0m   ");
        }
        islandPrint.append("\n");

        // print island team and number of each island
        for (IslandClient island : islands) {
            HouseColor t = island.getTeam();
            String team = t == null ? "  " : (t == HouseColor.WHITE ? "WT" : (t == HouseColor.BLACK ? "BT" : "GT"));
            islandPrint.append("  \u2571\u001b[40;1m ").append(team).append("  \u001b[0m\u001b[40;1m ")
                    .append(island.getArchipelagoSize()).append(" \u001b[0m\u2572  ");
        }
        islandPrint.append("\n");
        // print number of red students per island and mother nature if it's on that island
        for (IslandClient island : islands) {
            String mt = " ";
            String mt2 = "   ";
            if (game.getMotherNaturePosition() == islands.indexOf(island)) {
                mt = "MT ";
                mt2 = " ";
            }
            byte r = island.howManyStudents(Color.RED);
            String red = r < 10 ? ("0" + r) : String.valueOf(r);
            islandPrint.append(" \u2571  ").append(mt).append("\u001b[41;1m ").append(red).append(" \u001b[0m")
                    .append(mt2).append("\u2572 ");
        }
        islandPrint.append("\n");
        // print number of blue and yellow students per island
        for (IslandClient island : islands) {
            byte b = island.howManyStudents(Color.BLUE);
            String blue = b < 10 ? ("0" + b) : String.valueOf(b);
            byte y = island.howManyStudents(Color.YELLOW);
            String yellow = y < 10 ? ("0" + y) : String.valueOf(y);
            islandPrint.append(" \u2572 \u001b[44;1m ").append(blue).append(" \u001b[0m\u001b[43;1m ").append(yellow).append(" \u001b[0m \u2571 ");
        }
        islandPrint.append("\n");
        // print number of green and pink students per island
        for (IslandClient island : islands) {
            byte g = island.howManyStudents(Color.GREEN);
            String green = g < 10 ? ("0" + g) : String.valueOf(g);
            byte p = island.howManyStudents(Color.PINK);
            String pink = p < 10 ? ("0" + p) : String.valueOf(p);
            islandPrint.append("  \u2572\u001b[42;1m ").append(green).append(" \u001b[0m\u001b[45;1m ").append(pink).append(" \u001b[0m\u2571  ");
        }
        islandPrint.append("\n");
        // print bottom line of each island
        islandPrint.append("   \u203e\u203e\u203e\u203e\u203e\u203e\u203e\u203e   ".repeat(islands.size()));
        islandPrint.append("\n");
        return islandPrint;
    }

    private StringBuilder printCloudsAndTeams(ArrayList<GameComponentClient> clouds, List<TeamClient> teams) {
        StringBuilder cloudsTeamsPrint = new StringBuilder();
        // print cloud number
        for (int i = 0; i < clouds.size(); i++) {
            cloudsTeamsPrint.append(" \u256d\u2500\u2500\u2524").append(i + 1).append("\u251c\u2500\u2500\u256e  ");
        }
        // print team names
        for (TeamClient t : teams) {
            cloudsTeamsPrint.append("\t\t\u001b[7m ").append(t.getHouseColor()).append(" TEAM \u001b[0m\t");
        }
        cloudsTeamsPrint.append("\u001b[0m \n");
        // print red students per cloud
        for (GameComponentClient cloud : clouds) {
            byte red = cloud.howManyStudents(Color.RED);
            cloudsTeamsPrint.append(" \u2502  \u001b[41;1m ").append(red).append(" \u001b[0m  \u2502  ");
        }
        // print number of towers per team
        for (TeamClient t : teams) {
            cloudsTeamsPrint.append("\t\t\u25d8  x ").append(t.getTowersLeft()).append("\t\t");
        }
        cloudsTeamsPrint.append("\n");
        // print blue and yellow students per cloud
        for (GameComponentClient cloud : clouds) {
            byte blue = cloud.howManyStudents(Color.BLUE);
            byte yellow = cloud.howManyStudents(Color.YELLOW);
            cloudsTeamsPrint.append(" \u2502\u001b[44;1m ").append(blue).append(" \u001b[0m \u001b[43;1m ").append(yellow).append(" \u001b[0m\u2502  ");
        }
        // second team members when playing with 4 players
        StringBuilder secondRow = new StringBuilder();
        // print team members
        cloudsTeamsPrint.append("\t\t");
        for (TeamClient t : teams) {
            String player = t.getPlayers().get(0).getWizard() + ": " + t.getPlayers().get(0);
            cloudsTeamsPrint.append(player).append(" ".repeat(32 - player.length()));
            if (game.getMatchType().nPlayers() >= 4) {
                player = t.getPlayers().get(1).getWizard() + ": " + t.getPlayers().get(1);
                secondRow.append(player).append(" ".repeat(32 - player.length()));
            }
        }
        cloudsTeamsPrint.append("\n");
        // print green and pink students per cloud
        for (
                GameComponentClient cloud : clouds) {
            byte green = cloud.howManyStudents(Color.GREEN);
            byte pink = cloud.howManyStudents(Color.PINK);
            cloudsTeamsPrint.append(" \u2502\u001b[42;1m ").append(green).append(" \u001b[0m \u001b[45;1m ").append(pink).append(" \u001b[0m\u2502  ");
        }
        // print second team members, if present
        cloudsTeamsPrint.append("\t\t").append(secondRow).append("\n");
        // print bottom line of clouds
        cloudsTeamsPrint.append(" \u2570\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u256f  ".repeat(clouds.size()));
        cloudsTeamsPrint.append("\n");
        return cloudsTeamsPrint;
    }

    private StringBuilder printBoardsChatCharacters(List<PlayerClient> players) {
        StringBuilder boardsCharChatPrint = new StringBuilder();
        byte numOfPlayers = (byte) players.size();
        boolean expert = game.isExpert();

        List<String> chat = view.getChat();
        if (chat.size() > 9)
            chat.subList(chat.size() - 9, chat.size());
        // upper line of boards
        boardsCharChatPrint.append(" \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557 ".repeat(numOfPlayers));

        // names of the character cards
        if (expert) {
            for (CharacterCardClient ch : game.getCharacters()) {
                String name = ch.toString();
                if (game.getCurrentCharacterCard() != null && game.getCurrentCharacterCard().getCharId() == ch.getCharId())
                    boardsCharChatPrint.append("\t\u256d\u2500\u001b[31;1m");
                else
                    boardsCharChatPrint.append("\t\u256d\u2500");
                boardsCharChatPrint.append(name).append("\u001b[0m\u2500".repeat(17 - name.length())).append("\u256e");
            }
        }
        boardsCharChatPrint.append("\n");
        // colors
        String[] colors = new String[]{"\u001b[31m", "\u001b[34m", "\u001b[33m", "\u001b[32m", "\u001b[35m", "\u001b[0m"};
//        StringBuilder colors = new StringBuilder("\u001b[31m\u001b[34m\u001b[33m\u001b[32m\u001b[35m\u001b[0m");
        // print professors of each player
        Wizard[] professors = game.getProfessors();
        for (int i = 0; i < numOfPlayers; i++) {
            StringBuilder profs = new StringBuilder();
            for (int j = 0; j < Color.values().length; j++) {
                if (professors[j] == players.get(i).getWizard())
                    profs.append(" ").append(colors[j]).append("\u25d9 ");
                else profs.append(colors[j]).append("   ");
            }
            boardsCharChatPrint.append("\u001b[0m").append(" \u2551").append(profs).append("\u001b[0m").append("\u2551 ");
        }
        if (expert) {
            // print cost of character cards and students if there's any
            for (CharacterCardClient ch : game.getCharacters()) {
                boardsCharChatPrint.append("\t\u2502 Cost: ").append(ch.getCost()).append("  ");
                if (ch.containsStudents())
                    boardsCharChatPrint.append("\u2502  \u001b[41;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.RED)).append(" \u001b[0m  \u2502");
                else
                    boardsCharChatPrint.append("\u2502       \u2502");
            }
        }
        boardsCharChatPrint.append("\n");
        // boards separator
        boardsCharChatPrint.append(" \u255f\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2562 ".repeat(numOfPlayers));
        // print second row of character cards with students if present
        if (expert) {
            for (CharacterCardClient ch : game.getCharacters()) {
                boardsCharChatPrint.append("\t\u2502          ");
                if (ch.containsStudents())
                    boardsCharChatPrint.append("\u2502\u001b[44;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.BLUE))
                            .append(" \u001b[0m \u001b[43;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.YELLOW)).append(" \u001b[0m\u2502");
                else
                    boardsCharChatPrint.append("\u2502       \u2502");
            }
        }
        boardsCharChatPrint.append("\n");
        // print students in lunch hall
        for (int i = 10; i > 0; i--) {
            for (PlayerClient p : players) {
                StringBuilder stud = new StringBuilder();
                for (Color c : Color.values()) {
                    if (p.getLunchHall().howManyStudents(c) >= i)
                        stud.append(" ").append(colors[c.ordinal()]).append("\u25cf\u001b[0m ");
                    else if (i % 3 == 0 && expert)
                        stud.append(" \u25ef ");
                    else
                        stud.append("   ");
                }
                boardsCharChatPrint.append(" \u2551").append(stud).append("\u001b[0m").append("\u2551 ");
            }
            switch (i) {
                case 10 -> {
                    if (expert) {
                        // print third row of character cards with students if present
                        for (CharacterCardClient ch : game.getCharacters()) {
                            if (ch.toString().equals("Grandma weeds"))
                                boardsCharChatPrint.append("\t\u2502   \u001b[31mX\u001b[0m: ").append(game.getNewProhibitionsLeft()).append("   ");
                            else
                                boardsCharChatPrint.append("\t\u2502          ");
                            if (ch.containsStudents())
                                boardsCharChatPrint.append("\u2502\u001b[42;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.GREEN))
                                        .append(" \u001b[0m \u001b[45;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.PINK)).append(" \u001b[0m\u2502");
                            else
                                boardsCharChatPrint.append("\u2502       \u2502");
                        }
                    }
                    boardsCharChatPrint.append("\n");
                }
                case 9 -> {
                    if (expert) {
                        // print second row of character cards
                        for (int j = 0; j < 3; j++) {
                            boardsCharChatPrint.append("\t\u2570").append("\u2500".repeat(18)).append("\u256f");
                        }
                    }
                    boardsCharChatPrint.append("\n");
                }
                // line separator between chat and cards
                case 8 -> boardsCharChatPrint.append("\n");
                // first chat line
                case 7 ->
                        boardsCharChatPrint.append("\t\u250c").append("\u2500".repeat(39)).append("\u2524 CHAT \u251c").append("\u2500".repeat(39)).append("\u2510\n");
                // print messages of chat
                default -> {
                    String mex = "";
                    if (chat.size() >= (3 + i))
                        mex = chat.get(chat.size() - 3 - i);
                    boardsCharChatPrint.append("\t\u2502 ").append(mex).append(" ".repeat((78 - mex.length() + 7))).append("\u2502\n");
                }
            }
        }
        // board separator between lunch and entrance hall
        boardsCharChatPrint.append(" \u255f\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2562 ".repeat(numOfPlayers));
        // messages
        String mex = "";
        if (chat.size() >= 3)
            mex = chat.get(chat.size() - 3);
//        "\t\u2502\u001b[1m Paolino: \u001b[0mCome va?";
        boardsCharChatPrint.append("\t\u2502 ").append(mex).append(" ".repeat((78 - mex.length() + 7))).append("\u2502\n");
        // calculate entrance hall students (its color) of each player
        HashMap<PlayerClient, ArrayList<String>> entranceStud = new HashMap<>();
        for (PlayerClient p : players) {
            ArrayList<String> studs = new ArrayList<>();
            for (Color c : Color.values())
                for (int j = 0; j < p.getEntranceHall().howManyStudents(c); j++)
                    studs.add(colors[c.ordinal()]);
            Collections.shuffle(studs, new Random(0));
            entranceStud.put(p, studs);
        }
        // print first row of entrance hall students
        for (PlayerClient p : players) {
            boardsCharChatPrint.append(" \u2551");
            for (int i = 0; i < 5; i++) {
                if (i < p.getEntranceHall().howManyStudents())
                    boardsCharChatPrint.append(entranceStud.get(p).get(i)).append(" \u25cf \u001b[0m");
                else
                    boardsCharChatPrint.append("   ");
            }
            boardsCharChatPrint.append("\u2551 ");
        }
        // message
        mex = "";
        if (chat.size() >= 2)
            mex = chat.get(chat.size() - 2);
        boardsCharChatPrint.append("\t\u2502 ").append(mex).append(" ".repeat((78 - mex.length() + 7))).append("\u2502\n");

        // print second row of entrance hall students
        for (PlayerClient p : players) {
            boardsCharChatPrint.append(" \u2551");
            for (int i = 5; i < 10; i++) {
                if (i < p.getEntranceHall().howManyStudents())
                    boardsCharChatPrint.append(entranceStud.get(p).get(i)).append(" \u25cf \u001b[0m");
                else
                    boardsCharChatPrint.append("   ");
            }
            boardsCharChatPrint.append("\u2551 ");
        }
        // message
        mex = "";
        if (chat.size() >= 1)
            mex = chat.get(chat.size() - 1);
        boardsCharChatPrint.append("\t\u2502 ").append(mex).append(" ".repeat((78 - mex.length() + 7))).append("\u2502\n");

        // bottom line of boards and chat
        boardsCharChatPrint.append(" \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d ".repeat(players.size()));
        boardsCharChatPrint.append("\t\u2514");
        boardsCharChatPrint.append("\u2500".repeat(86));
        boardsCharChatPrint.append("\u2518\n");
        // print wizard names under each board
        for (int i = 0; i < players.size(); i++) {
            String spacing1 = i == 2 ? "   " : "  ";
            String spacing2 = i == 2 ? " " : "  ";
            // if wizard is current player wizard name is going to be highlighted
            boardsCharChatPrint.append(spacing1);
            if (game.getCurrentPlayer() != null)
                if (game.getCurrentPlayer().getWizard() == players.get(i).getWizard())
                    boardsCharChatPrint.append("\u001b[31;1m");
            // highlight player's wizard
            if (game.getMyWizard() == players.get(i).getWizard())
                boardsCharChatPrint.append("\u001b[7m");

            boardsCharChatPrint.append(players.get(i).getWizard()).append("\u001b[0m");
            if (expert) {
                byte c = game.getCoinsPlayer((byte) i);
                String coins = c < 10 ? "0" + c : String.valueOf(c);
                boardsCharChatPrint.append("\u001b[33;1m \u25cb\u001b[0m x ").append(coins).append(spacing2);
            } else boardsCharChatPrint.append("       ").append(spacing2);
        }
        boardsCharChatPrint.append("\n\n");
        return boardsCharChatPrint;

    }

    private StringBuilder printAssistantCards(List<PlayerClient> players) {
        StringBuilder assistantCardsPrint = new StringBuilder();
        PlayerClient player = players.get(game.getMyWizard().ordinal());
        List<AssistantCard> assistantCards = player.getAssistantCards();
        // print available cards numbers
        for (AssistantCard card : assistantCards) {
            String space = card.value() == 10 ? "\u256d\u2500\u2524" : "\u256d\u2500\u2500\u2524";
            assistantCardsPrint.append(space).append(card.value()).append("\u251c\u2500\u2500\u256e   ");
        }
        assistantCardsPrint.append("\n");

        // print "moves"
        for (AssistantCard ignored : assistantCards) {
            assistantCardsPrint.append("\u2502 Moves ").append("\u2502   ");
        }
        assistantCardsPrint.append("\n");

        // print number of moves
        for (AssistantCard card : assistantCards) {
            assistantCardsPrint.append("\u2502   ").append(card.moves()).append("   \u2502   ");
        }
        assistantCardsPrint.append("\n");
        // print bottom line of cards
        for (AssistantCard ignored : assistantCards) {
            assistantCardsPrint.append("\u2570").append("\u2500".repeat(7)).append("\u256f   ");
        }
        assistantCardsPrint.append("\n");
        return assistantCardsPrint;
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {

    }

    @Override
    public void updateGameComponent(IslandClient island) {

    }

    @Override
    public void updateDeletedIsland(IslandClient island) {

    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {

    }

    @Override
    public void updateProfessor(Color color, Wizard wizard) {

    }

    @Override
    public void updateMembers(int membersLeftToStart, String nickPlayerJoined) {
        if (membersLeftToStart > 0) {
            view.addMessage(nickPlayerJoined + " joined. " + membersLeftToStart + " members left before game starts");
        }
    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard) {

    }

    @Override
    public void updateIgnoredColor(Color color) {
        view.addMessage("During this turn color " + color + " will not add influence");
    }

    @Override
    public void updateExtraSteps(boolean extraSteps) {
        if (extraSteps) {
            view.addMessage("During this turn you have 2 more extra steps");
        }
    }

    @Override
    public void updateCharacter(List<CharacterCardClient> characters) {

    }

    @Override
    public void updateCoins(Byte coins) {

    }

    @Override
    public void setWinners(List<HouseColor> winners) {
        String s = "Winner";
        if (winners.size() == 1) s += " is ";
        else s += "s are ";
        s += winners;
        view.addMessage(s);
    }

    @Override
    public void updateMessage(String message) {
        view.setMustReprint(true);
    }



    public void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}