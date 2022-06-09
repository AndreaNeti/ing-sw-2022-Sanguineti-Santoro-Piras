package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.*;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class BoardController implements SceneController {
    AbstractView view;
    public AnchorPane root;
    public AnchorPane islands;
    //boards is the anchor pane that contains all the board from top to bottom->check showAllModel for more information
    public AnchorPane boards;
    //this is a hbox that contains three anchor pane, each contains  the anchor pane of students (even the char that don't have it), the image character card and possibily the prohibition
    public HBox characters;

    @Override
    public void setViewGUI(ViewGUI viewGUI) {
        view = viewGUI;
        updateModelCreated();
    }

    @Override
    public void hideEverything() {

    }

    @Override
    public Node getElementById(String id) {
        return root.lookup(id);
    }

    @Override
    public void updateModelCreated() {
        view.getModel().addListener(this);
        showAllModel();
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
        Platform.runLater(() -> {
            int idIsland = view.getModel().getIslands().get(motherNaturePosition).getId();
            AnchorPane island = (AnchorPane) getElementById("#Island" + idIsland);
            island.getChildren().add(getMotherNature());
        });
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {
        int id = gameComponent.getId();
        //entrance hall and lunch are handled in a different way in view so i need to distinguish them
        //but island,character card, cloud with student are handled in the same way
        if (id < 8 && id > 0) {
            if (id % 2 == 0) {
                updateEntranceHall(gameComponent);
            } else {
                updateLunchHall(gameComponent);
            }
        } else {
            //a game component usually has an anchor pane for himself that contains an image( children 0), an anchor pane with all the students
            //this anchor pane has an anchor pane for all colors-> this contains an image view (children 0) and a label for the number of students
            System.out.println("#" + gameComponent.getNameOfComponent() + id);
            AnchorPane paneGameComponent = (AnchorPane) this.getElementById("#" + gameComponent.getNameOfComponent() + id);
            AnchorPane paneStudents = (AnchorPane) paneGameComponent.getChildren().get(1);
            for (int i = 0; i < Color.values().length; i++) {
                AnchorPane paneSingleStudent = (AnchorPane) paneStudents.getChildren().get(i);
                //get(1) is the label in the pane of the students
                Label l = (Label) paneSingleStudent.getChildren().get(1);
                l.setText(String.valueOf(gameComponent.howManyStudents(Color.values()[i])));
            }
        }
    }

    private void updateEntranceHall(GameComponentClient entranceHall) {

    }

    private void updateLunchHall(GameComponentClient lunchHall) {

    }

    @Override
    public void updateGameComponent(IslandClient island) {
        //all Island in gui are saved as anchor pane=> this contains the image of the island and an anchor pane of the students
        //the anchor pane of the students contains all 5 possible color in 5 other anchor pane
        Platform.runLater(() -> {
            updateGameComponent(island);
            AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.getNameOfComponent() + island.getId());
            HouseColor islandTeam = island.getTeam();
            if (islandTeam != null) {
                paneIsland.getChildren().add(getTower(islandTeam));
            }
        });
    }

    @Override
    public void updateDeletedIsland(IslandClient island) {
        //I retrieve the team that deleted the island by the position of mother nature
        Platform.runLater(() -> {
            HouseColor winnerTeam = view.getModel().getIslands().get(view.getModel().getMotherNaturePosition()).getTeam();
            AnchorPane paneIsland = (AnchorPane) this.getElementById("#Island" + island.getId());
            //remove the pane of students
            paneIsland.getChildren().remove(1);
            //add tower
            paneIsland.getChildren().add(getTower(winnerTeam));
        });
    }

    @Override
    public void updateTowerLeft(HouseColor houseColor, Byte towerLefts) {
    }

    @Override
    public void updateProfessor(Color color, Wizard wizard) {

    }

    @Override
    public void updateMembers(int membersLeftToStart, String nickPlayerJoined) {

    }

    @Override
    public void updateCardPlayed(AssistantCard playedCard) {

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

    }

    private ImageView getMotherNature() {
        ImageView mn = new ImageView("Graphical_Assets/MotherNature.png");
        mn.setFitWidth(50.0);
        mn.setLayoutX(60.0);
        mn.setLayoutY(75.0);
        mn.setPreserveRatio(true);
        AnchorPane.setBottomAnchor(mn, 75.0);
        AnchorPane.setTopAnchor(mn, 75.0);
        AnchorPane.setLeftAnchor(mn, 61.5);
        return mn;
    }

    private ImageView getTower(HouseColor houseColor) {
        ImageView tower = null;
        switch (houseColor) {
            case BLACK -> tower = new ImageView("Graphical_Assets/Towers/black_tower.png");
            case WHITE -> tower = new ImageView("Graphical_Assets/Towers/white_tower.png");
            case GREY -> tower = new ImageView("Graphical_Assets/Towers/grey_tower.png");
        }
        tower.setFitWidth(50.0);
        tower.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(tower, 61.5);
        AnchorPane.setTopAnchor(tower, 20.0);
        return tower;
    }

    private void showAllModel() {
        GameClientView model = view.getModel();
        if (model.isExpert()) {
            List<CharacterCardClient> characters = model.getCharacters();
            //add all image of the character card
            for (int i = 0; i < characters.size(); i++) {
                CharacterCardClient character = characters.get(i);
                AnchorPane singleChar = (AnchorPane) this.characters.getChildren().get(i);
                ImageView imageView=(ImageView) singleChar.getChildren().get(0);
                imageView.setImage(new Image("Graphical_Assets/CharacterCards/CarteTOT_front" + character.getCharId() + ".jpg"));
                if (character.containsStudents()) {
                    CharacterCardClientWithStudents character1 = (CharacterCardClientWithStudents) character;
                    singleChar.getChildren().get(1).setVisible(true);
                    singleChar.setId(character1.getNameOfComponent() + character1.getId());
                }
            }
        }
        // variable boards contains 4 children->all the possible board in game. The last one is the main board of the client
        //each board contains an image view, label, anchor pane (entrance hall), vbox (lunch hall) [this contains 5 hbox], and another anchor pane(tower left)
        Platform.runLater(() -> {
            List<PlayerClient> players = view.getModel().getPlayers();
            int localWizardIndex = model.getMyWizard().ordinal();
            for (int i = boards.getChildren().size() - 1; i >= 0; i--) {
                //TODO
                //hide the board from the top make it drop that's some wap
                if (i <= boards.getChildren().size() - 1 - players.size()) {
                    boards.getChildren().get(i).setVisible(false);
                } else {
                    PlayerClient player = players.get(localWizardIndex);
                    AnchorPane board = (AnchorPane) boards.getChildren().get(i);
                    board.setId(String.valueOf(Wizard.values()[localWizardIndex]));
                    for (int j = 0; j < 4; j++) {
                        switch (j) {
                            //set the label of the main board
                            case 1 -> ((Label) board.getChildren().get(j)).setText(player.getNickName());
                            //set the id of the entrance hall
                            case 2 -> {
                                board.getChildren().get(j).setId(player.getEntranceHall().getNameOfComponent() + player.getEntranceHall().getId());
                                updateEntranceHall(player.getEntranceHall());
                            }
                            //set the id of the lunch hall
                            case 3 -> {
                                board.getChildren().get(j).setId(player.getLunchHall().getNameOfComponent() + player.getLunchHall().getId());
                                updateLunchHall(player.getLunchHall());
                            }

                        }
                    }
                    localWizardIndex = (localWizardIndex + 1) % players.size();
                }
            }
        });

        for (IslandClient i : model.getIslands()) {
            updateGameComponent(i);
        }
        updateMotherNature(model.getMotherNaturePosition());
        for (GameComponentClient cloud : model.getClouds()) {
            updateGameComponent(cloud);
        }
    }
}
