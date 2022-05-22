package it.polimi.ingsw.Client.View.Cli;

import it.polimi.ingsw.Client.model.*;
import it.polimi.ingsw.Enum.Color;
import it.polimi.ingsw.Enum.HouseColor;
import it.polimi.ingsw.Enum.Wizard;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CliPrinter {

    private GameClient game;

    public CliPrinter(GameClient game) {
        this.game = game;
    }

    public void print() {

//        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        AnsiConsole.systemInstall();
        List<PlayerClient> players = game.getPlayers();
        System.out.println("----------------------ERYANTIS----------------------");
        System.out.print(printIslands(game.getIslands()));
        System.out.print(printCloudsAndTeams(game.getClouds(), players));
        System.out.print(printBoardsChatCharacters(players));
        System.out.print(printAssistantCards(players));
        AnsiConsole.systemUninstall();
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
                    .append(island.getNumber()).append(" \u001b[0m\u2572  ");
        }
        islandPrint.append("\n");
        // print number of red students per island and mother nature if it's on that island
        for (IslandClient island : islands) {
            String mt = " ";
            if(game.getMotherNaturePosition() == islands.indexOf(island))
                mt = "MT ";
            byte r = island.howManyStudents(Color.RED);
            String red = r < 10 ? ("0" + r) : String.valueOf(r);
            islandPrint.append(" \u2571  ").append(mt).append("\u001b[41;1m ").append(red).append(" \u001b[0m   \u2572 ");
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
            islandPrint.append(" \u2572 \u001b[42;1m ").append(green).append(" \u001b[0m\u001b[45;1m ").append(pink).append(" \u001b[0m \u2571 ");
        }
        islandPrint.append("\n");
        // print bottom line of each island
        islandPrint.append("   \u203e\u203e\u203e\u203e\u203e\u203e\u203e\u203e   ".repeat(islands.size()));
        islandPrint.append("\n");
        return islandPrint;
    }

    private StringBuilder printCloudsAndTeams(ArrayList<GameComponentClient> clouds, List<PlayerClient> players) {
        byte numOfTeams = (byte) (players.size() % 2 == 0 ? 2 : 3);
        StringBuilder cloudsTeamsPrint = new StringBuilder();
        // print cloud number
        for (int i = 0; i < clouds.size(); i++) {
            cloudsTeamsPrint.append(" \u256d\u2500\u2500\u2524").append(i + 1).append("\u251c\u2500\u2500\u256e  ");
        }
        // print team names
        for (int i = 0; i < numOfTeams; i++) {
            HouseColor t = HouseColor.values()[i];
            cloudsTeamsPrint.append("\t\u001b[7m ").append(t.toString()).append(" TEAM \u001b[0m\t");
        }
        cloudsTeamsPrint.append("\u001b[0m \n");
        // print red students per cloud
        for (GameComponentClient cloud : clouds) {
            byte red = cloud.howManyStudents(Color.RED);
            cloudsTeamsPrint.append(" \u2502  \u001b[41;1m ").append(red).append(" \u001b[0m  \u2502  ");
        }
        // print number of towers per team
        for (int i = 0; i < numOfTeams; i++) {
            byte towers = 0;
            for (PlayerClient p : players) {
                if (p.getHouseColor().ordinal() == i) towers += p.getTowersLeft();
            }
            cloudsTeamsPrint.append("\t\u25d8  x ").append(towers).append("\t\t");
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
        for (int i = 0; i < numOfTeams; i++) {
            if (players.size() < 4) {
                for (PlayerClient p : players) {
                    cloudsTeamsPrint.append("\t").append(p.getWizard()).append(": ").append(p);
                }
            } else {
                boolean t = false;
                for (PlayerClient p : players) {
                    if (p.getHouseColor().ordinal() == i) {
                        if (!t) {
                            cloudsTeamsPrint.append("\t").append(p.getWizard()).append(": ").append(p);
                            t = true;
                        } else {
                            secondRow.append("\t").append(p.getWizard()).append(": ").append(p);
                        }
                    }
                }
            }
        }
        cloudsTeamsPrint.append("\n");
        // print green and pink students per cloud
        for (GameComponentClient cloud : clouds) {
            byte green = cloud.howManyStudents(Color.GREEN);
            byte pink = cloud.howManyStudents(Color.PINK);
            cloudsTeamsPrint.append(" \u2502\u001b[44;1m ").append(green).append(" \u001b[0m \u001b[43;1m ").append(pink).append(" \u001b[0m\u2502  ");
        }
        // print second team members, if present
        cloudsTeamsPrint.append(secondRow).append("\n");
        // print bottom line of clouds
        cloudsTeamsPrint.append(" \u2570\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u256f  ".repeat(clouds.size()));
        cloudsTeamsPrint.append("\n");
        return cloudsTeamsPrint;
    }

    private StringBuilder printBoardsChatCharacters(List<PlayerClient> players) {
        StringBuilder boardsCharChatPrint = new StringBuilder();
        byte numOfPlayers = (byte) players.size();
        boolean expert = game.isExpert();
        // upper line of boards
        boardsCharChatPrint.append(" \u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557 ".repeat(numOfPlayers));

        // names of the character cards
        for (CharacterCardClient ch : game.getCharacters()) {
            String name = ch.toString();
            boardsCharChatPrint.append("\t\u256d\u2500").append(name).append("\u2500".repeat(17 - name.length())).append("\u256e");
        }
        // colors
        StringBuilder colors = new StringBuilder("\u001b[31m\u001b[34m\u001b[33m\u001b[32m\u001b[35m\u001b[0m");
        // print professors of each player
        for (int i = 0; i < numOfPlayers; i++) {
            StringBuilder profs = new StringBuilder();
            for (int j = 0; j < Color.values().length; j++) {
                if (game.getProfessors()[0] == players.get(i).getWizard())
                    profs.append(" ").append(colors.charAt(j)).append("\u25d9 ");
                else profs.append(colors.charAt(j)).append("   ");
            }
            boardsCharChatPrint.append(" \u2551").append(profs).append("\u2551 ");
        }
        if(expert) {
            // print cost of character cards and students if there's any
            for (CharacterCardClient ch : game.getCharacters()) {
                boardsCharChatPrint.append("\t\u2502 Cost: ").append(ch.getCost()).append("  ");
                if (ch.toString().equals("Monk") || ch.toString().equals("Jester") || ch.toString().equals("Spoiled Princess"))
                    boardsCharChatPrint.append("\u2502  \u001b[41;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.RED)).append(" \u001b[0m  ");
                else
                    boardsCharChatPrint.append("\u2502       \u2502");
            }
        }
        boardsCharChatPrint.append("\n");
        // boards separator
        boardsCharChatPrint.append(" \u255f\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2562 ".repeat(numOfPlayers));
        // print second row of character cards with students if present
        if(expert) {
            for (CharacterCardClient ch : game.getCharacters()) {
                boardsCharChatPrint.append("\t\u2502          ");
                if (ch.toString().equals("Monk") || ch.toString().equals("Jester") || ch.toString().equals("Spoiled Princess"))
                    boardsCharChatPrint.append("\u2502\u001b[44;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.BLUE))
                            .append(" \u001b[0m \u001b[43;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.YELLOW)).append(" \u001b[0m");
                else
                    boardsCharChatPrint.append("\u2502          \u2502");
            }
        }
        boardsCharChatPrint.append("\n");
        // print students in lunch hall
        for (int i = 10; i > 0; i--) {
            for (PlayerClient p : players) {
                StringBuilder stud = new StringBuilder();
                for (Color c : Color.values()) {
                    if (p.getLunchHall().howManyStudents(c) >= i)
                        stud.append(" ").append(colors.charAt(c.ordinal())).append("\u25cf ");
                    else stud.append(colors.charAt(c.ordinal())).append("   ");
                }
                boardsCharChatPrint.append(" \u2551").append(stud).append("\u2551 ");
            }
            switch (i) {
                case 10 -> {
                    if(expert) {
                        // print third row of character cards with students if present
                        for (CharacterCardClient ch : game.getCharacters()) {
                            boardsCharChatPrint.append("\t\u2502          ");
                            if (ch.toString().equals("Monk") || ch.toString().equals("Jester") || ch.toString().equals("Spoiled Princess"))
                                boardsCharChatPrint.append("\u2502\u001b[42;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.GREEN))
                                        .append(" \u001b[0m \u001b[45;1m ").append(((CharacterCardClientWithStudents) ch).howManyStudents(Color.PINK)).append(" \u001b[0m");
                            else if(ch.toString().equals("Grandma weeds"))
                                boardsCharChatPrint.append("\u2502   \u001b[31mX\u001b[0m: ").append(game.getNewProhibitionsLeft()).append("\u2502");
                        }
                    }
                    boardsCharChatPrint.append("\n");
                }
                case 9 -> {
                    if(expert) {
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
                        boardsCharChatPrint.append("\t\u2500".repeat(35)).append("\u2524\u001b[4m CHAT \u001b[0m\u251c").append("\u2500".repeat(35)).append("\n");
                // print messages of chat
                default -> {
                    String mex = "\t\u2502\u001b[1m Franco: \u001b[0mCiao!";
                    boardsCharChatPrint.append(mex).append(" ".repeat((78 - mex.length() + 8))).append("\u2502\n");
                }
            }
        }
        // board separator between lunch and entrance hall
        boardsCharChatPrint.append(" \u255f\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2562 ".repeat(numOfPlayers));
        // messages
        String mex = "\t\u2502\u001b[1m Paolino: \u001b[0mCome va?";
        boardsCharChatPrint.append(mex).append(" ".repeat((78 - mex.length() + 8))).append("\u2502\n");
        // calculate entrance hall students (its color) of each player
        HashMap<PlayerClient, ArrayList<Character>> entranceStud = new HashMap<>();
        for (PlayerClient p : players) {
            ArrayList<Character> studs = new ArrayList<>();
            for (int i = 0; i < colors.length() - 1; i++)
                for (int j = 0; j < p.getEntranceHall().howManyStudents(Color.values()[i]); j++)
                    studs.add(colors.charAt(i));
            Collections.shuffle(studs);
            entranceStud.put(p, studs);
        }
        // print first row of entrance hall students
        for (PlayerClient p : players) {
            boardsCharChatPrint.append(" \u2551");
            for (int i = 0; i < 5; i++) {
                boardsCharChatPrint.append(entranceStud.get(p).get(i)).append(" \u25cf \u001b[0m");
            }
            boardsCharChatPrint.append("\u2551 ");
        }
        // message
        mex = "\t\u2502\u001b[1m Paolino: \u001b[0mCome va?";
        boardsCharChatPrint.append(mex).append(" ".repeat((78 - mex.length() + 8))).append("\u2502\n");

        // print second row of entrance hall students
        for (PlayerClient p : players) {
            boardsCharChatPrint.append(" \u2551");
            for (int i = 5; i < 10; i++) {
                if (i < game.getMatchConstants().entranceHallStudents() - 1)
                    boardsCharChatPrint.append(entranceStud.get(p).get(i)).append(" \u25cf \u001b[0m");
                else
                    boardsCharChatPrint.append("   ");
            }
            boardsCharChatPrint.append("\u2551 ");
        }
        // message
        mex = "\t\u2502\u001b[1m Paolino: \u001b[0mCome va?";
        boardsCharChatPrint.append(mex).append(" ".repeat((78 - mex.length() + 8))).append("\u2502\n");

        // bottom line of boards and chat
        boardsCharChatPrint.append(" \u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d ".repeat(players.size()));
        boardsCharChatPrint.append("\t\u2514");
        boardsCharChatPrint.append("\u2500".repeat(78));
        boardsCharChatPrint.append("\u2518\n");
        // print wizard names under each board
        for (int i = 0; i < players.size(); i++) {
            String a = i == 2 ? "   " : "  ";
            String b = i == 2 ? " " : "  ";
            // if wizard is current player wizard name is going to be highlighted
            if(game.getCurrentPlayer().getWizard() == players.get(i).getWizard()) {
                boardsCharChatPrint.append("\u001b[31;1m");
            }
                boardsCharChatPrint.append(a).append(Wizard.values()[i]).append("\u001b[0m");
            if (expert) {
                byte c = game.getCoinsPlayer((byte) i);
                String coins = c < 10 ? "0" + c : String.valueOf(c);
                boardsCharChatPrint.append("\u001b[33;1m \u25cb\u001b[0m x ").append(coins).append(b);
            }
            else boardsCharChatPrint.append("       ").append(b);
        }
        boardsCharChatPrint.append("\n\n ");
        return boardsCharChatPrint;

    }

    private StringBuilder printAssistantCards(List<PlayerClient> players) {
        StringBuilder assistantCardsPrint = new StringBuilder();
        PlayerClient player = players.get(0);
        // find player
        for (PlayerClient p : players)
            if (p.getWizard() == game.getMyWizard()) player = p;

        boolean[] cards = player.getUsedCards();

        // print available cards numbers
        for (int i = 0; i < cards.length; i++) {
            if (!cards[i]) {
                String space = i == 9 ? "\u256d\u2500\u2524" : "\u256d\u2500\u2500\u2524";
                assistantCardsPrint.append(space).append(i + 1).append("\u251c\u2500\u2500\u256e   ");
            }
        }
        assistantCardsPrint.append("\n");

        // print "moves"
        for (boolean card : cards) {
            if (!card) {
                assistantCardsPrint.append("\u2502 Moves ").append("\u2502   ");
            }
        }
        assistantCardsPrint.append("\n");

        // print number of moves
        for (int i = 0; i < cards.length; i++) {
            if (!cards[i]) {
                assistantCardsPrint.append("\u2502   ").append((i / 2) + 1).append("   \u2502   ");
            }
        }
        assistantCardsPrint.append("\n");
        // print bottom line of cards
        for (boolean card : cards) {
            if (!card) {
                assistantCardsPrint.append("\u2570").append("\u2500".repeat(7)).append("\u256f   ");
            }
        }
        assistantCardsPrint.append("\n");
        return assistantCardsPrint;
    }
}