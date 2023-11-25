package group.socialapp;

import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StartController {

    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private Stage startStage;

    public void setData(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage primaryStage) {
        this.serviceFriendship = serviceFriendship;
        this.serviceUser = serviceUser;
        this.startStage = primaryStage;
    }

    public void openLoginWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
        LoginController startController = userLoader.getController();
        startController.setData(serviceUser, serviceFriendship, startStage);
        startStage.setScene(scene);
    }


    public void openRegistrationWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/register-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/register.css")).toExternalForm());
        startStage.setScene(scene);
    }

}
