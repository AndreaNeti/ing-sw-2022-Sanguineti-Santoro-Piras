package it.polimi.ingsw.client.view.gui.SceneController;

import it.polimi.ingsw.client.model.*;
import it.polimi.ingsw.client.phaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.client.view.gui.GuiFX;
import it.polimi.ingsw.client.view.gui.ViewGUI;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.*;

@SuppressWarnings("unchecked") // imho, \_O_/
public class BoardController implements SceneController {
    ViewGUI viewGUI;
    public AnchorPane root;
    public AnchorPane islands;
    //boards is the anchor pane that contains all the board from top to bottom->check showAllModel for more information
    public AnchorPane boards;
    public AnchorPane mainBoard;
    //this is a hbox that contains three anchor pane, each contains the image character card [0], anchor pane of students (even the char that don't have it)[1],
    // the button to choose[2], the button to undo[3], the button to play[4]
    public HBox characters;
    public Button chatButton;
    public Button quitButton;
    public VBox chat;
    public Pane paneForChat;
    public Button sendButton;
    public HBox assistantCardsBox;
    private ObservableList<String> observableListChat;
    public HBox clouds;
    public Button rulesButton;
    public Button musicButton;
    public final Set<Node> clickableElement = new HashSet<>();
    public final Set<Node> visibleElement = new HashSet<>();
    public final Set<Node> selectedElement = new HashSet<>();
    private final HashMap<Node, Timeline> timelines = new HashMap<>();
    private MediaPlayer music;

    /**
     * Method initialize creates and sets elements in the scene
     */
    private void initialize() {
        URL url = GuiFX.class.getResource("/ostGame.mp3");
        music = new MediaPlayer(new Media(url.toString()));
        music.play();
        music.setOnEndOfMedia(() -> {
            music.play();
        });
        music.setCycleCount(MediaPlayer.INDEFINITE);
        MediaView mediaView = new MediaView(music);
        root.getChildren().add(mediaView);

        musicButton.setOnMouseClicked(mouseEvent -> {
            if (musicButton.getText().equals("\u25b6")) {
                musicButton.setText("\u23f8");
                music.play();
            } else {
                musicButton.setText("\u25b6");
                music.pause();
            }
        });

        String inputFile = "rules.pdf";

        try {
            Path tempOutput = Files.createTempFile("TempManual", ".pdf");
            tempOutput.toFile().deleteOnExit();
            System.out.println("tempOutput: " + tempOutput);
            InputStream is = GuiFX.class.getClassLoader().getResourceAsStream(inputFile);
            Files.copy(is, tempOutput, StandardCopyOption.REPLACE_EXISTING);
            rulesButton.setOnMouseClicked(mouseEvent -> {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(tempOutput.toFile());
                    }
                } catch (IOException e) {
                    GuiFX.showError("Error", "Make sure that you have a pdf viewer on your computer!", "Generic Error");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        //create and initialize chat
        chatButton.setOnAction(actionEvent -> chat.setVisible(!chat.isVisible()));
        //create the chat
        observableListChat = FXCollections.observableArrayList();
        observableListChat.addAll(viewGUI.getChat());
        ListView<String> listView = new ListView<>(observableListChat);
        listView.prefWidthProperty().bind(paneForChat.widthProperty());
        paneForChat.getChildren().add(listView);
        chat.toFront();
        chat.setVisible(false);
        sendButton.setOnMouseClicked(GameCommand.TEXT_MESSAGE.getGUIHandler(viewGUI));
        quitButton.setOnMouseClicked(GameCommand.QUIT.getGUIHandler(viewGUI));

        //initialize all the model
        GameClientView model = viewGUI.getModel();
        //initialize the character

        if (model.isExpert()) {
            List<CharacterCardClient> characters = model.getCharacters();
            //add all image of the character card
            for (int i = 0; i < characters.size(); i++) {
                CharacterCardClient character = characters.get(i);
                AnchorPane singleChar = (AnchorPane) this.characters.getChildren().get(i);
                ImageView imageView = (ImageView) singleChar.getChildren().get(0);
                imageView.setImage(new Image("Graphical_Assets/CharacterCards/" + (-character.getCharId() - 10) + ".png"));
                singleChar.getProperties().put("index", i);
                singleChar.getProperties().put("charId", character.getCharId());
                //this is for grandma weeds
                if (character.hasProhibitions()) {
                    singleChar.getChildren().get(6).setVisible(true);
                    singleChar.setId("grandmaWeeds");
                }
                if (character.hasStudents()) {
                    GameComponentClient gc = model.getComponentOfCharacter(character.getCharId());

                    AnchorPane paneStudent = (AnchorPane) singleChar.getChildren().get(1);
                    paneStudent.setVisible(true);
                    singleChar.setId(String.valueOf(gc.getId()));
                    //set the properties of the color of the student
                    for (Color color : Color.values()) {
                        ((AnchorPane) paneStudent.getChildren().get(color.ordinal())).getChildren().get(0).getProperties().put("color", color);
                    }
                    updateGameComponent(gc);
                }
//                //adding choose command button and on click showing the description of the character
//                Button b = (Button) singleChar.getChildren().get(2);
//
//
//                //b.setOnMouseClicked( GameCommand.CHOOSE_CHARACTER.getGUIHandler(viewGUI));

                imageView.setOnMouseClicked(mouseEvent -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    ImageView image = new ImageView(new Image("Graphical_Assets/CharacterCards/" + (-character.getCharId() - 10) + ".png"));
                    image.setPreserveRatio(true);
                    image.setFitWidth(150);
                    alert.setGraphic(image);
                    alert.setHeaderText(character + ", cost = " + character.getCost());
                    alert.setContentText(character.getDescription());
                    alert.setTitle(character.toString());
                    alert.initOwner(GuiFX.getPrimaryStage());
                    alert.showAndWait();
                });

            }
            HBox colorBox = (HBox) getElementById("#colorBox");
            for (Color color : Color.values()) {
                colorBox.getChildren().get(color.ordinal()).getProperties().put("color", color);
            }
            // coins are in coinsBox
            HBox box = (HBox) getElementById("#coinsBox");
            box.setVisible(true);
            updateCoins(model.getCoinsLeft());
        } else {
            characters.setVisible(false);
        }
        // variable boards contains 4 children->all the possible board in game. The last one is the main board of the client
        //each board contains an image view [0], label [1], anchor pane (entrance hall)[2], vbox (lunch hall) [this contains 5 hbox] [3],
        // the vbox for professor [4] ,an anchor pane for tower left[5]
        //and the last anchor pane for the assistant card [6]

        List<PlayerClient> players = viewGUI.getModel().getPlayers();
        int localWizardIndex = model.getMyWizard().ordinal();
        for (int i = 0; i < boards.getChildren().size(); i++) {

            if (i >= players.size()) {
                boards.getChildren().get(i).setVisible(false);
            } else {
                PlayerClient player = players.get(localWizardIndex);
                AnchorPane board = (AnchorPane) boards.getChildren().get(i);
                board.setId(String.valueOf(Wizard.values()[localWizardIndex]));
                for (int j = 0; j < 8; j++) {
                    Node element = board.getChildren().get(j);
                    switch (j) {
                        //set the label of the main board
                        case 1 -> ((Label) element).setText(player.getNickName());
                        //set the id of the entrance hall
                        case 2 -> {
                            element.setId(String.valueOf(player.getEntranceHall().getId()));
                            element.getProperties().put("name", player.getEntranceHall().getNameOfComponent());
                            updateEntranceHall(player.getEntranceHall());
                        }
                        //set the id of the lunch hall
                        case 3 -> {
                            element.setId((String.valueOf(player.getLunchHall().getId())));
                            element.getProperties().put("name", player.getLunchHall().getNameOfComponent());
                            //setting properties in each child
                            for (Color c : Color.values()) {
                                ((VBox) element).getChildren().get(c.ordinal()).getProperties().put("color", c);
                            }
                            updateLunchHall(player.getLunchHall());
                        }
                        // set the id of professors
                        case 4 -> element.setId("professors" + localWizardIndex);
                        //set the id of the towers

                        case 5 -> {
                            List<TeamClient> teams = viewGUI.getModel().getTeams();
                            TeamClient team = viewGUI.getModel().getTeams().get(localWizardIndex % teams.size());
                            int teamSize = team.getPlayers().size();
                            element.setId(team.getHouseColor() + String.valueOf((i / teamSize) % teamSize));
                            updateTowerLeft(team.getHouseColor(), team.getTowersLeft());
                        }
                        case 6 -> {
                            //set id of the assistant card
                            HBox assistantPane = (HBox) element;
                            assistantPane.setId("assistantCard" + Wizard.values()[localWizardIndex]);
                            // 1 is the pane that contain the back of all the cards
                            AnchorPane back = (AnchorPane) assistantPane.getChildren().get(1);
                            ImageView imageView = (ImageView) back.getChildren().get(0);
                            imageView.setImage(new Image("Graphical_Assets/Wizard/" + Wizard.values()[localWizardIndex] + ".png"));

                            imageView.setOnMouseClicked(mouseEvent -> {
                                if (!assistantCardsBox.isVisible()) viewGUI.updateAssistantBox(player);
                                assistantCardsBox.setVisible(!assistantCardsBox.isVisible());
                            });
                        }
                        case 7 -> {
                            if (model.isExpert()) {
                                element.setVisible(true);
                                updateCoins(player.getWizard(), model.getCoinsPlayer((byte) localWizardIndex));
                            }
                        }
                    }
                }
                localWizardIndex = (localWizardIndex + 1) % players.size();
            }
        }

        for (IslandClient i : model.getIslands()) {
            Node islandPane = islands.getChildren().get(i.getId() - 2 * MatchType.MAX_PLAYERS);
            islandPane.getProperties().put("relativeId", i.getId());
            Set<Integer> containedIslands = new TreeSet<>();
            containedIslands.add(i.getId());
            islandPane.getProperties().put("containedIslands", containedIslands);
            updateGameComponent(i);
        }

        updateMotherNature(model.getMotherNaturePosition());

        //delete all the unused clouds
        clouds.getChildren().subList(viewGUI.getModel().getClouds().size(), clouds.getChildren().size()).clear();

        for (GameComponentClient cloud : model.getClouds()) {
            updateGameComponent(cloud);
        }
        // set who is the current player
        updateCurrentPlayer((byte) model.getCurrentPlayer().getWizard().ordinal());
    }

    @Override
    public void setViewGUI(ViewGUI viewGUI) {
        this.viewGUI = viewGUI;
        if (viewGUI.getModel() != null) {
            viewGUI.getModel().addListener(this);
            initialize();
        }
    }

    @Override
    public void hideEverything() {
        for (Node node : visibleElement) {
            node.setVisible(false);
        }
    }

    @Override
    public void selectNode(Node node) {
        selectedElement.add(node);
        node.getStyleClass().add("selected");
        node.applyCss();
    }

    @Override
    public void enableNode(Node node) {
        enableNode(node, false);
    }

    @Override
    public void disableNode(Node node) {
        disableNode(node, false);
    }

    @Override
    public void disableNode(Node node, boolean removeVisibility) {
        Timeline timeline = timelines.get(node);
        if (timeline != null) timeline.pause();
        node.setDisable(true);
        node.getStyleClass().remove("clickable");
        if (removeVisibility) {
            node.setVisible(false);
            visibleElement.remove(node);
        }
        node.getStyleClass().remove("selected");
        node.applyCss();
        clickableElement.remove(node);
        selectedElement.remove(node);
    }

    //to enable a node use this function-> this is needed so there is an eay way to disable all the element
    @Override
    public void enableNode(Node node, boolean addVisibility) {
        node.setDisable(false);
        clickableElement.add(node);
        node.getStyleClass().add("clickable");
        if (addVisibility) {
            visibleElement.add(node);
            node.setVisible(true);
        }
        // lost 3 hours because of this :D
        node.applyCss();
        DropShadow shadow = (DropShadow) node.getEffect();
        Timeline timeline;
        if (shadow != null) {
            // check if already existing
            timeline = timelines.get(node);
            if (timeline == null) {
                // create timeline with animation
                timeline = new Timeline();
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.ZERO, new KeyValue(shadow.spreadProperty(), shadow.getSpread())), new KeyFrame(Duration.millis(750), new KeyValue(shadow.spreadProperty(), 0)));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.setAutoReverse(true);
                timeline.playFromStart();
                timelines.put(node, timeline);
            } else {
                // already created, play from start
                timeline.playFromStart();
            }
        } else {
            System.err.println("Not applied effect on " + node);
        }

    }

    @Override
    public void disableEverything() {
        for (Node node : clickableElement) {
            Timeline timeline = timelines.get(node);
            node.setDisable(true);
            node.getStyleClass().remove("clickable");
            if (timeline != null) {
                timeline.pause();
            }
            node.applyCss();
        }
        for (Node node : visibleElement) {
            node.getStyleClass().remove("clickable");
            node.setVisible(false);
            node.applyCss();
        }
        for (Node node : selectedElement) {
            node.getStyleClass().remove("selected");
            node.applyCss();
        }
        selectedElement.clear();
        clickableElement.clear();
        visibleElement.clear();
    }

    @Override
    public Node getElementById(String id) {
        if (id.equals("#mainBoard")) return mainBoard;
        return root.lookup(id);
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
        Platform.runLater(() -> {
            //delete old mother nature
            Node motherNature = getElementById("#motherNature");
            if (motherNature != null) {
                AnchorPane islandOld = (AnchorPane) motherNature.getParent();
                islandOld.getChildren().remove(motherNature);
            }
            // update the new mother nature
            IslandClient motherNatureIsland = viewGUI.getModel().getIslands().get(motherNaturePosition);
            AnchorPane islandPane = (AnchorPane) getElementById("#" + getCenterArchipelagoId(motherNatureIsland));
            // System.out.println("Mother Nature position: " + motherNaturePosition + ", moved on island #" + getCenterArchipelagoId(motherNatureIsland));
            islandPane.getChildren().add(getMotherNature());
        });
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {
        Platform.runLater(() -> {
            int id = gameComponent.getId();
            //entrance hall and lunch are handled in a different way in view, so I need to distinguish them
            //but island,character card, cloud with student are handled in the same way
            if (id < 2 * MatchType.MAX_PLAYERS && id >= 0) {
                if (id % 2 == 0) {
                    updateEntranceHall(gameComponent);
                } else {
                    updateLunchHall(gameComponent);
                }
            } else if (id < 0 && id > -10) {
                updateCloud(gameComponent);
            } else {
                updateGeneric(gameComponent);
            }
        });
    }

    /**
     * Method updateGeneric is used to update generic nodes containing students (ex. character cards and islands)
     *
     * @param component of type {@code GameComponentClient} - is the updated component.
     * @param nodeID    of type {@code int} - is the id of the Node to update (used if it's different from the component's id).
     */
    private void updateGeneric(GameComponentClient component, int nodeID) {
        //a game component usually has an anchor pane for himself that contains an image( children 0), an anchor pane with all the students
        //this anchor pane has an anchor pane for all colors-> this contains an image view (children 0) and a label for the number of students
        AnchorPane paneGameComponent = (AnchorPane) this.getElementById("#" + nodeID);
        AnchorPane paneStudents = (AnchorPane) paneGameComponent.getChildren().get(1);
        for (int i = 0; i < Color.values().length; i++) {
            AnchorPane paneSingleStudent = (AnchorPane) paneStudents.getChildren().get(i);
            //get(1) is the label in the pane of the students
            Label l = (Label) paneSingleStudent.getChildren().get(1);
            l.setText(String.valueOf(component.howManyStudents(Color.values()[i])));
        }
    }

    /**
     * Method updateGeneric is used to update generic nodes in the scene containing students (ex. character cards and islands)
     *
     * @param component of type {@code GameComponentClient} - is the updated component.
     */
    private void updateGeneric(GameComponentClient component) {
        updateGeneric(component, component.getId());
    }

    /**
     * Method updateCloud is used to update clouds in the scene
     *
     * @param cloud of type {@code GameComponentClient} - is the updated cloud component.
     */
    private void updateCloud(GameComponentClient cloud) {
        //"clouds" contains 4 children: one for cloud
        //each cloud has an image[0] and an anchor pane for students [1]
        //this anchor pane has 4 images
        byte[] students = cloud.getStudents();
        AnchorPane paneCloud = (AnchorPane) getElementById("#" + cloud.getId());
        AnchorPane paneStudents = (AnchorPane) paneCloud.getChildren().get(1);
        byte insertedStudent = 0;
        for (Color c : Color.values()) {
            for (byte j = 0; j < students[c.ordinal()]; j++) {
                ImageView student = (ImageView) paneStudents.getChildren().get(insertedStudent);
                student.setImage(new Image("Graphical_Assets/Students/" + c + ".png"));
                insertedStudent++;
            }
        }
        int maxStudent = viewGUI.getModel().getMatchConstants().studentsToMove();
        for (; insertedStudent < maxStudent; insertedStudent++) {
            ImageView student = (ImageView) paneStudents.getChildren().get(insertedStudent);
            student.setImage(null);
        }


    }

    /**
     * Method updateEntranceHall is used to update entranceHalls in the scene
     *
     * @param entranceHall of type {@code GameComponentClient} - is the updated entranceHall component.
     */
    private void updateEntranceHall(GameComponentClient entranceHall) {
        byte[] students = entranceHall.getStudents();
        AnchorPane paneEntrance = (AnchorPane) getElementById("#" + entranceHall.getId());
        byte insertedStudent = 0;
        for (Color c : Color.values()) {
            for (byte j = 0; j < students[c.ordinal()]; j++) {
                ImageView student = (ImageView) paneEntrance.getChildren().get(insertedStudent);
                student.setImage(new Image("Graphical_Assets/Students/" + c + ".png"));
                student.getProperties().put("color", c);
                student.setPickOnBounds(true);
                insertedStudent++;
            }
        }
        int maxStudent = viewGUI.getModel().getMatchConstants().entranceHallStudents();
        for (; insertedStudent < maxStudent; insertedStudent++) {
            ImageView student = (ImageView) paneEntrance.getChildren().get(insertedStudent);
            student.setImage(null);
        }
    }

    /**
     * Method updateLunchHall is used to update lunchHalls in the scene
     *
     * @param lunchHall of type {@code GameComponentClient} - is the updated lunchHall component.
     */
    private void updateLunchHall(GameComponentClient lunchHall) {
        VBox paneLunchHall = (VBox) getElementById("#" + lunchHall.getId());
        HBox hBox;
        for (Color c : Color.values()) {
            hBox = (HBox) paneLunchHall.getChildren().get(c.ordinal());
            byte diff = (byte) (lunchHall.howManyStudents(c) - hBox.getChildren().size());
            for (byte i = 0; i < diff; i++) {
                ImageView student = new ImageView();
                student.setImage(new Image("Graphical_Assets/Students/" + c + ".png"));
                student.setFitWidth(30);
                student.setPreserveRatio(true);
                HBox.setMargin(student, new Insets(1.6, 1.6, 1.6, 1.6));
                hBox.getChildren().add(student);
            }
            for (byte i = 0; i > diff; i--) {
                hBox.getChildren().remove(0);
            }
        }
    }

    @Override
    public void updateGameComponent(IslandClient island) {
        //all Island in gui are saved as anchor pane=> this contains the image of the island and an anchor pane of the students
        //the anchor pane of the students contains all 5 possible color in 5 other anchor pane
        Platform.runLater(() -> {
            AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.getId());
            int relativeId;
            // if it's an archipelago, get its center
            if (island.getArchipelagoSize() > 1) relativeId = getCenterArchipelagoId(island);
            else relativeId = Integer.parseInt(paneIsland.getId());

            updateGeneric(island, relativeId);
            HouseColor islandTeam = island.getTeam();
            if (islandTeam != null) {
                Set<Integer> containedIslands = (Set<Integer>) paneIsland.getProperties().get("containedIslands");
                // change tower for each island contained
                for (Integer i : containedIslands) {
                    AnchorPane containedIslandPane = ((AnchorPane) this.getElementById("#" + i));
                    containedIslandPane.getChildren().remove(containedIslandPane.lookup(".tower"));
                    containedIslandPane.getChildren().add(getTower(islandTeam));
                }
            }
            HBox prohibitionsBox = (HBox) paneIsland.lookup(".prohibitions");
            byte islandProhibitions = island.getProhibitions();
            // set the island prohibitions box
            if (islandProhibitions > 0) {
                prohibitionsBox.setVisible(true);
                Label prohibitionsLabel = (Label) prohibitionsBox.getChildren().get(1);
                prohibitionsLabel.setText("× " + islandProhibitions);
            } else prohibitionsBox.setVisible(false);
        });
    }

    @Override
    public void updateDeletedIsland(IslandClient removedIsland, IslandClient winnerIsland) {
        Platform.runLater(() -> {
            // retrieve the team that deleted the island and enlarge it using the position of mother nature
            byte motherNaturePosition = viewGUI.getModel().getMotherNaturePosition();
            // System.out.println("Mother Nature has position: " + motherNaturePosition + " and it's on island #" + getCenterArchipelagoId(winnerIsland) + " (relativeId = " + winnerIsland.getId() + ") removed island #" + removedIsland.getId());
            HouseColor winnerTeam = winnerIsland.getTeam();

            AnchorPane paneWinnerIsland = (AnchorPane) this.getElementById("#" + winnerIsland.getId());
            // make bigger
            paneWinnerIsland.setStyle("-fx-scale-x:1; -fx-scale-y:1");

            Set<Integer> containedIslands = (Set<Integer>) paneWinnerIsland.getProperties().get("containedIslands");
            // add to contained islands the ones contained by the removed island
            containedIslands.addAll((Set<Integer>) this.getElementById("#" + removedIsland.getId()).getProperties().get("containedIslands"));
            containedIslands.add(removedIsland.getId());
            // update relativeId of all merged islands
            for (Integer i : containedIslands) {
                AnchorPane node = (AnchorPane) this.getElementById("#" + i);
                node.getProperties().put("relativeId", winnerIsland.getId());
                // hide the pane of students
                node.getChildren().get(1).setVisible(false);
                //add tower and make bigger
                node.getChildren().add(getTower(winnerTeam));
                node.setStyle("-fx-scale-x:1; -fx-scale-y:1");
                // prohibitions are visible only on the winner island
                HBox prohibitionsBox = (HBox) node.lookup(".prohibitions");
                prohibitionsBox.setVisible(false);
            }
            // update contained islands
            paneWinnerIsland.getProperties().put("containedIslands", containedIslands);
            AnchorPane paneArchipelago = (AnchorPane) this.getElementById("#" + getCenterArchipelagoId(winnerIsland));
            // make students visible on the island where mother nature is visible
            if (winnerIsland.getProhibitions() > 0) paneArchipelago.lookup(".prohibitions").setVisible(true);
            updateGeneric(winnerIsland, Integer.parseInt(paneArchipelago.getId()));
            paneArchipelago.getChildren().get(1).setVisible(true);
            updateMotherNature(motherNaturePosition);
        });
    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {
        Platform.runLater(() -> {
            int teamSize = viewGUI.getModel().getTeams().get(houseColor.ordinal()).getPlayers().size();
            for (int i = 0; i < teamSize; i++) {

                AnchorPane towers = (AnchorPane) getElementById("#" + houseColor + i);
                for (int j = 0; j < towerLefts; j++) {
                    ImageView tower = (ImageView) towers.getChildren().get(j);
                    tower.setImage(new Image("/Graphical_Assets/Towers/" + houseColor + ".png"));
                }
                for (int k = towerLefts; k < towers.getChildren().size(); k++) {
                    ImageView tower = (ImageView) towers.getChildren().get(k);
                    tower.setImage(null);
                }
            }

        });
    }

    @Override
    public void updateProfessor(Color color, Wizard wizard) {
        Platform.runLater(() -> {
            for (int i = 0; i < viewGUI.getModel().getMatchType().nPlayers(); i++)
                ((VBox) getElementById("#professors" + i)).getChildren().get(color.ordinal()).setVisible(false);
            ((VBox) getElementById("#professors" + wizard.ordinal())).getChildren().get(color.ordinal()).setVisible(true);
        });
    }

    @Override
    public void updateMembers(int membersLeftToStart, PlayerClient playerJoined) {

    }

    @Override
    public void updateMatchInfo(MatchType matchType, MatchConstants constants, List<TeamClient> teams) {

    }

    @Override
    public void updateProhibitions(Byte newProhibitions) {
        AnchorPane singleChar = (AnchorPane) getElementById("#grandmaWeeds");
        AnchorPane paneProhibitions = (AnchorPane) singleChar.getChildren().get(6);
        for (int i = 0; i < paneProhibitions.getChildren().size(); i++) {
            paneProhibitions.getChildren().get(i).setVisible(i < newProhibitions);
        }
    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard, Wizard current) {
        Platform.runLater(() -> {
            HBox assistantPane = (HBox) getElementById("#assistantCard" + current);
            // 0 is the pane that contain the played card
            AnchorPane played = (AnchorPane) assistantPane.getChildren().get(0);
            ImageView imageView = (ImageView) played.getChildren().get(0);
            imageView.setImage(new Image("Graphical_Assets/AssistantCard/" + playedCard.value() + ".png"));
        });
    }

    @Override
    public void updateIgnoredColor(Color color) {
        GuiFX.showError("Information", "For this turn " + color + " will not add up while calculating the influence", "Information");

    }

    @Override
    public void updateExtraSteps(boolean extraSteps) {
    }

    @Override
    public void updateCharacter(byte charId) {
        for (Node n : characters.getChildren()) {
            if ((byte) n.getProperties().get("charId") == charId) {
                ((AnchorPane) n).getChildren().get(5).setVisible(true);
            }
        }
    }

    @Override
    public void updateCoins(Integer coins) {
        Platform.runLater(() -> {
            //coins are in coinsBox
            HBox box = (HBox) getElementById("#coinsBox");
            Label label = (Label) box.getChildren().get(1);
            label.setText("× " + coins);
        });
    }

    @Override
    public void updateCoins(Wizard wizard, Integer coins) {
        Platform.runLater(() -> {
            // coins are the 7 children of the board
            AnchorPane board = (AnchorPane) getElementById("#" + wizard);
            Label label = (Label) ((HBox) board.getChildren().get(7)).getChildren().get(1);
            label.setText("× " + coins);
        });
    }

    @Override
    public void setWinners(List<HouseColor> winners) {
        if (winners.contains(HouseColor.PAOLINO)) {
//            String path = new File("src/main/resources/winP.mp4").getAbsolutePath();
            URL url = GuiFX.class.getResource("/Graphical_Assets/winP.mp4");
            MediaPlayer media = new MediaPlayer(new Media(url.toString()));
            MediaView mediaView = new MediaView(media);
            media.setCycleCount(MediaPlayer.INDEFINITE);
            media.play();
            GuiFX.showError("PAOLINO WON!", "You have been destroyed by Paolino's mighty will", "END MATCH", mediaView);
        } else {
            URL url = GuiFX.class.getResource("/Graphical_Assets/win.gif");
            Image image = new Image(url.toString());
            ImageView mediaView = new ImageView(image);
            StringBuilder winnerString = new StringBuilder();
            for (HouseColor hc : winners)
                winnerString.append(hc).append(" TEAM, ");
            winnerString.delete(winnerString.length() - 2, winnerString.length() - 1);
            GuiFX.showError("WINNERS!", winnerString + "WON THE GAME :D", "END MATCH", mediaView);
        }
    }

    @Override
    public void updateMessage(String message) {
        Platform.runLater(() -> {
            if (observableListChat.size() > 15) {
                observableListChat.remove(0);
            }
            observableListChat.add(message);
        });
    }

    @Override
    public void updateError(String error) {
        GuiFX.showError("Server error", error, "Error");
    }

    @Override
    public void updateCurrentPlayer(byte newCurrentPlayer) {
        GameClientView model = viewGUI.getModel();
        byte oldCurrentPlayer = (byte) model.getCurrentPlayer().getWizard().ordinal();
        byte oldPlayerBoardIndex = (byte) Math.floorMod(oldCurrentPlayer - model.getMyWizard().ordinal(), model.getPlayers().size());
        byte newPlayerBoardIndex = (byte) Math.floorMod(newCurrentPlayer - model.getMyWizard().ordinal(), model.getPlayers().size());
        Platform.runLater(() -> {
            boards.getChildren().get(oldPlayerBoardIndex).getStyleClass().remove("currentPlayer");
            boards.getChildren().get(newPlayerBoardIndex).getStyleClass().add("currentPlayer");
        });
    }

    /**
     * Method getMotherNature creates an {@code ImageView} representing mother nature
     *
     * @return {@code ImageView} - the mother nature's {@code ImageView}.
     */
    private ImageView getMotherNature() {
        ImageView mn = new ImageView("Graphical_Assets/MotherNature.png");
        mn.setFitWidth(50.0);
        mn.setLayoutX(60.0);
        mn.setLayoutY(75.0);
        mn.setPreserveRatio(true);
        mn.setId("motherNature");
        AnchorPane.setBottomAnchor(mn, 75.0);
        AnchorPane.setTopAnchor(mn, 75.0);
        AnchorPane.setLeftAnchor(mn, 61.5);
        return mn;
    }

    /**
     * Method getTower creates an {@code ImageView} representing a tower
     *
     * @param houseColor {@code HouseColor} - the tower's color
     * @return {@code ImageView} - the tower's {@code ImageView}.
     */
    private ImageView getTower(HouseColor houseColor) {
        ImageView tower = new ImageView("Graphical_Assets/Towers/" + houseColor + ".png");
        tower.getStyleClass().add("tower");
        tower.setFitWidth(30d);
        tower.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(tower, 72.5d);
        AnchorPane.setTopAnchor(tower, 5d);
        return tower;
    }

    /**
     * Method getCenterArchipelagoId retrieves the id of the island in the center of the archipelago
     *
     * @param island {@code IslandClient} - the island representing the archipelago
     * @return {@code int} - the id of the island in the center of the archipelago.
     */
    private int getCenterArchipelagoId(IslandClient island) {
        // love RB trees
        TreeSet<Integer> containedIslands = (TreeSet<Integer>) getElementById("#" + island.getId()).getProperties().get("containedIslands");
        int last = containedIslands.last();
        int first = containedIslands.first();
        int centerID;
        if (last - first == 11) {
            last = first;
            for (Integer id : containedIslands) {
                // there is a hole
                if (id > last + 1) {
                    first = id;
                    break;
                } else last = id;
            }
            last += 12;
        }
        centerID = Math.round((float) (last + first) / 2);
        if (centerID >= 12 + 2 * MatchType.MAX_PLAYERS) centerID -= 12;
        return centerID;
    }

    @Override
    public void addNodeToRoot(Node node) {
        root.getChildren().add(node);
    }

    @Override
    public void stop() {
        music.stop();

    }
}
