package HarmonyHub.GUI.Controllers;

import HarmonyHub.Service.ServiceFriendship;
import HarmonyHub.Service.ServiceUser;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPageController {

    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private Stage primaryStage;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane logoAnchorPane;

    @FXML
    private AnchorPane progressBarAnchorPane;

    @FXML
    private AnchorPane loginAnchorPane;

    public void setData(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
    }

    public void initialize() {
        animateProgressBar();
    }

    public void openRegisterWindow(ActionEvent actionEvent) {
    }

    private void animateProgressBar() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(progressBar.progressProperty(), 1),new KeyValue(progressBarAnchorPane.visibleProperty(), false))
        );

        FadeTransition logoFadeTransition = new FadeTransition(Duration.seconds(2), logoAnchorPane);
        logoFadeTransition.setFromValue(0);
        logoFadeTransition.setToValue(1);

        FadeTransition loginFadeTransition = new FadeTransition(Duration.seconds(2), loginAnchorPane);
        loginFadeTransition.setFromValue(0);
        loginFadeTransition.setToValue(1);

        SequentialTransition sequentialTransition = new SequentialTransition(
                timeline,
                logoFadeTransition,
                loginFadeTransition
        );

        sequentialTransition.play();

    }

}
