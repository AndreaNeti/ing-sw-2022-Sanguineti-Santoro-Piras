package it.polimi.ingsw.Client.View.Gui.SceneController;


import it.polimi.ingsw.Client.View.AbstractView;
import it.polimi.ingsw.Client.View.GameClientListener;

public interface SceneController extends GameClientListener {
    void setView(AbstractView abstractView);
}
