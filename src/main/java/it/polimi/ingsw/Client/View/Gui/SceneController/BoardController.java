package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.PhaseAndComand.Commands.GameCommand;
import it.polimi.ingsw.Client.View.Gui.GuiFX;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.*;
import it.polimi.ingsw.Util.AssistantCard;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import javafx.animation.*;
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
import javafx.util.Duration;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardController implements SceneController {
    ViewGUI viewGUI;
    public AnchorPane root;
    public AnchorPane islands;
    //boards is the anchor pane that contains all the board from top to bottom->check showAllModel for more information
    public AnchorPane boards;
    public AnchorPane mainBoard;
    //this is a hbox that contains three anchor pane, each contains the image character card [0], anchor pane of students (even the char that don't have it)[1], the button to choose[2]
    public HBox characters;
    public Button chatButton;
    public Button quitButton;
    public VBox chat;
    public Pane paneForChat;
    public Button sendButton;
    public AnchorPane assistantCardsBox;
    private ObservableList<String> observableListChat;
    public HBox clouds;
    public Map<Node, Timeline> clickableElement = new HashMap<>();


    private void initialize() {
        //create and initialize chat
        chatButton.setOnAction(actionEvent -> chat.setVisible(!chat.isVisible()));
        //create the chat
        observableListChat = FXCollections.observableArrayList();
        observableListChat.addAll(viewGUI.getChat());
        ListView<String> listView = new ListView<>(observableListChat);
        listView.prefWidthProperty().bind(paneForChat.widthProperty());
        listView.prefHeightProperty().bind(paneForChat.heightProperty());
        paneForChat.getChildren().add(listView);
        chat.toFront();
        chat.setVisible(false);
        sendButton.setOnMouseClicked(GameCommand.TEXT_MESSAGE.getGUIHandler(viewGUI));
        quitButton.setOnMouseClicked(GameCommand.QUIT.getGUIHandler(viewGUI));

        //initialize all the model
        GameClientView model = viewGUI.getModel();
        //initialize the character
        //TODO do the coin
        if (model.isExpert()) {
            List<CharacterCardClient> characters = model.getCharacters();
            //add all image of the character card
            for (int i = 0; i < characters.size(); i++) {
                CharacterCardClient character = characters.get(i);
                AnchorPane singleChar = (AnchorPane) this.characters.getChildren().get(i);
                ImageView imageView = (ImageView) singleChar.getChildren().get(0);
                imageView.setImage(new Image("Graphical_Assets/CharacterCards/" + character.getCharId() + ".png"));
                singleChar.getProperties().put("charId", character.getCharId());
                if (character.containsStudents()) {
                    CharacterCardClientWithStudents character1 = (CharacterCardClientWithStudents) character;
                    AnchorPane paneStudent = (AnchorPane) singleChar.getChildren().get(1);
                    paneStudent.setVisible(true);
                    singleChar.setId(String.valueOf(character1.getId()));
                    //set the properties of the color of the student
                    for (Color color : Color.values()) {
                        paneStudent.getChildren().get(color.ordinal()).getProperties().put("color", color);
                    }
                }
//                //adding choose command button and on click showing the description of the character
//                Button b = (Button) singleChar.getChildren().get(2);
//
//
//                //b.setOnMouseClicked( GameCommand.CHOOSE_CHARACTER.getGUIHandler(viewGUI));

                singleChar.setOnMouseClicked(mouseEvent -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(character.getDescription());
                    alert.setTitle("Character card description");
                    alert.initOwner(GuiFX.getPrimaryStage());
                    alert.showAndWait();
                });

            }
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
                for (int j = 0; j < 7; j++) {
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
                            System.out.println(team.getHouseColor() + String.valueOf((i / teamSize) % teamSize));
                            element.setId(team.getHouseColor() + String.valueOf((i / teamSize) % teamSize));
                            updateTowerLeft(team.getHouseColor(), team.getTowersLeft());
                        }
                        case 6 -> {
                            //set id of the assistant card
                            AnchorPane assistantPane = (AnchorPane) board.getChildren().get(j);
                            assistantPane.setId("assistantCard" + Wizard.values()[localWizardIndex]);
                            //0 is the pane that contain the back of all the cards
                            AnchorPane back = (AnchorPane) assistantPane.getChildren().get(0);
                            ImageView imageView = (ImageView) back.getChildren().get(0);
                            imageView.setImage(new Image("Graphical_Assets/Wizard/" + Wizard.values()[localWizardIndex] + ".png"));
                        }
                    }
                }
                localWizardIndex = (localWizardIndex + 1) % players.size();
            }
        }
        for (IslandClient i : model.getIslands()) {
            //TODO see if there is a better way
            updateGameComponent(i);
        }
        updateMotherNature(model.getMotherNaturePosition());

        //delete all the unused clouds
        clouds.getChildren().subList(viewGUI.getModel().getClouds().size(), clouds.getChildren().size()).clear();

        for (GameComponentClient cloud : model.getClouds()) {
            updateGameComponent(cloud);
        }
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
        // TODO not used
        for (Node node : clickableElement.keySet()) {
            node.setDisable(false);
        }
    }

    //to enable a node use this function-> this is needed so there is an eay way to disable all the element
    public void enableNode(Node node) {
        node.setDisable(false);
        node.getStyleClass().add("clickable");
        Platform.runLater(() -> {
            DropShadow shadow = (DropShadow) node.getEffect();
            Timeline timeline = null;
            if (shadow != null) {
                timeline = new Timeline();
                timeline.getKeyFrames().setAll(new KeyFrame(Duration.ZERO, new KeyValue(shadow.spreadProperty(), shadow.getSpread())), new KeyFrame(Duration.millis(1000), new KeyValue(shadow.spreadProperty(), 0)));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.setAutoReverse(true);
                timeline.play();
            } else {
                System.out.println("Not applied effect on " + node);
            }
            clickableElement.put(node, timeline);
        });
    }

    @Override
    public void disableEverything() {
        for (Map.Entry<Node, Timeline> entry : clickableElement.entrySet()) {
            Node node = entry.getKey();
            Timeline timeline = entry.getValue();
            node.setDisable(true);
            node.getStyleClass().remove("clickable");
            if (timeline != null)
                timeline.stop();
        }
        clickableElement.clear();
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
            //update the new mother nature
            int idIsland = viewGUI.getModel().getIslands().get(motherNaturePosition).getId();
            AnchorPane island = (AnchorPane) getElementById("#" + idIsland);
            island.getChildren().add(getMotherNature());
        });
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {
        Platform.runLater(() -> {
            int id = gameComponent.getId();
            //entrance hall and lunch are handled in a different way in view so I need to distinguish them
            //but island,character card, cloud with student are handled in the same way
            if (id < 8 && id >= 0) {
                if (id % 2 == 0) {
                    updateEntranceHall(gameComponent);
                } else {
                    updateLunchHall(gameComponent);
                }
            } else if (id < 0) {
                updateCloud(gameComponent);
            } else {
                //a game component usually has an anchor pane for himself that contains an image( children 0), an anchor pane with all the students
                //this anchor pane has an anchor pane for all colors-> this contains an image view (children 0) and a label for the number of students
                AnchorPane paneGameComponent = (AnchorPane) this.getElementById("#" + id);
                AnchorPane paneStudents = (AnchorPane) paneGameComponent.getChildren().get(1);
                for (int i = 0; i < Color.values().length; i++) {
                    AnchorPane paneSingleStudent = (AnchorPane) paneStudents.getChildren().get(i);
                    //get(1) is the label in the pane of the students
                    Label l = (Label) paneSingleStudent.getChildren().get(1);
                    l.setText(String.valueOf(gameComponent.howManyStudents(Color.values()[i])));
                }
            }
        });
    }

    private void updateCloud(GameComponentClient cloud) {
        //"clouds" contains 4 children: one for cloud
        //each cloud has a image[0] and an anchor pane for students [1]
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
            updateGameComponent((GameComponentClient) island);
            AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.getId());
            HouseColor islandTeam = island.getTeam();
            if (islandTeam != null) {
                paneIsland.getChildren().add(getTower(islandTeam));
            }
        });
    }

    @Override
    public void updateDeletedIsland(IslandClient island) {

        Platform.runLater(() -> {
            // retrieve the team that deleted the island and enlarge it using the position of mother nature
            IslandClient winner = viewGUI.getModel().getIslands().get(viewGUI.getModel().getMotherNaturePosition());
            HouseColor winnerTeam = winner.getTeam();
            AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.getId());
            paneIsland.getProperties().put("relativeId", winner.getId());
            AnchorPane paneWinnerIsland = (AnchorPane) this.getElementById("#" + winner.getId());
            //remove the pane of students
            paneIsland.getChildren().remove(1);
            //add tower
            paneIsland.getChildren().add(getTower(winnerTeam));
            paneIsland.setStyle("-fx-scale-x:1");
            paneIsland.setStyle("-fx-scale-y:1");
            paneWinnerIsland.setStyle("-fx-scale-x:1");
            paneWinnerIsland.setStyle("-fx-scale-y:1");
        });
    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {
        Platform.runLater(() -> {
            /*for (PlayerClient p : viewGUI.getModel().getTeams().get(houseColor.ordinal()).getPlayers()
            ) {
                AnchorPane towers = (AnchorPane) getElementById("towers" + p.getWizard().ordinal());
                for (int i = 0; i < towerLefts; i++) {

                }
            }*/
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
    public void updateMembers(int membersLeftToStart, String nickPlayerJoined) {

    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard) {
        PlayerClient current = viewGUI.getModel().getCurrentPlayer();
        AnchorPane assistantPane = (AnchorPane) getElementById("#assistantCard" + current.getWizard());
        //1 is the pane that contain the played card
        AnchorPane played = (AnchorPane) assistantPane.getChildren().get(1);
        ImageView imageView = (ImageView) played.getChildren().get(0);
        imageView.setImage(new Image("Graphical_Assets/AssistantCard/" + playedCard.value() + ".png"));
    }

    @Override
    public void updateIgnoredColor(Color color) {

    }

    @Override
    public void updateExtraSteps(boolean extraSteps) {

    }

    @Override
    public void updateCharacter(List<CharacterCardClient> characters) {
    }

    @Override
    public void updateCoins(Byte coins) {
    }

    @Override
    public void setWinners(List<HouseColor> winners) {
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

    private ImageView getTower(HouseColor houseColor) {
        ImageView tower = new ImageView("Graphical_Assets/Towers/" + houseColor + ".png");
        tower.setFitWidth(50.0);
        tower.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(tower, 61.5);
        AnchorPane.setTopAnchor(tower, 20.0);
        return tower;
    }
}
