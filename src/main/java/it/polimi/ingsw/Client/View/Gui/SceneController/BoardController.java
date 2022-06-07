package it.polimi.ingsw.Client.View.Gui.SceneController;

import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.Gui.ViewGUI;
import it.polimi.ingsw.Client.model.CharacterCardClient;
import it.polimi.ingsw.Client.model.GameComponentClient;
import it.polimi.ingsw.Client.model.IslandClient;
import it.polimi.ingsw.Util.Color;
import it.polimi.ingsw.Util.HouseColor;
import it.polimi.ingsw.Util.Wizard;
import it.polimi.ingsw.Util.AssistantCard;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class BoardController implements SceneController {
    AbstractView view;
    public AnchorPane root;
    public AnchorPane islands;

    @Override
    public void setViewGUI(ViewGUI viewGUI) {
        view = viewGUI;
    }

    @Override
    public void hideEverything() {

    }

    @Override
    public Node getElementById(String id) {
        return root.lookup(id);
    }

    @Override
    public void updateMotherNature(Byte motherNaturePosition) {
        int idIsland = view.getModel().getIslands().get(motherNaturePosition).getId();
        AnchorPane island = (AnchorPane) getElementById("#Island" + idIsland);
        island.getChildren().add(getMotherNature());
    }

    @Override
    public void updateGameComponent(GameComponentClient gameComponent) {
    }

    @Override
    public void updateGameComponent(IslandClient island) {
        //all Island in gui are saved as anchor pane=> this contains the image of the island and an anchor pane of the students
        //the anchor pane of the students contains all 5 possible color in 5 other anchor pane
        AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.toString() + island.getId());
        AnchorPane paneStudents = (AnchorPane) paneIsland.getChildren().get(1);
        for (int i = 0; i < Color.values().length; i++) {
            AnchorPane paneSingleStudent = (AnchorPane) paneStudents.getChildren().get(i);
            //get(1) is the label in the pane of the students
            Label l = (Label) paneSingleStudent.getChildren().get(1);
            l.setText(String.valueOf(island.howManyStudents(Color.values()[i])));
        }
        HouseColor islandTeam = island.getTeam();
        if (islandTeam != null) {
            paneIsland.getChildren().add(getTower(islandTeam));
        }
    }

    @Override
    public void updateDeletedIsland(IslandClient island) {
        //I retrieve the team that deleted the island by the position of mother nature
        HouseColor winnerTeam = view.getModel().getIslands().get(view.getModel().getMotherNaturePosition()).getTeam();
        AnchorPane paneIsland = (AnchorPane) this.getElementById("#" + island.toString() + island.getId());
        //remove the pane of students
        paneIsland.getChildren().remove(1);
        //add tower
        paneIsland.getChildren().add(getTower(winnerTeam));
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
        ImageView mn = new ImageView("@Graphical_Assets/MotherNature.png");
        mn.setFitWidth(50.0);
        mn.setLayoutX(60.0);
        mn.setLayoutY(75.0);
        mn.setPreserveRatio(true);
        AnchorPane.setBottomAnchor(mn, 75.0);
        AnchorPane.setTopAnchor(mn, 75.0);
        AnchorPane.setLeftAnchor(mn, 75.0);
        return mn;
    }

    private ImageView getTower(HouseColor houseColor) {
        ImageView tower = null;
        switch (houseColor) {
            case BLACK -> tower = new ImageView("@Graphical_Assets/Towers/black_tower.png");
            case WHITE -> tower = new ImageView("@Graphical_Assets/Towers/white_tower.png");
            case GREY -> tower = new ImageView("@Graphical_Assets/Towers/grey_tower.png");
        }
        tower.setFitWidth(50.0);
        tower.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(tower, 61.5);
        AnchorPane.setTopAnchor(tower, 20.0);
        return tower;
    }
}
