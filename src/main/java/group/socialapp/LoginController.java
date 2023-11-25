package group.socialapp;

import group.socialapp.Domain.User;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    public TextField loginEmailField;
    @FXML
    public PasswordField loginPasswordField;

    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private Stage startStage;

    public void setData(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage primaryStage) {
        this.serviceFriendship = serviceFriendship;
        this.serviceUser = serviceUser;
        this.startStage = primaryStage;
    }

    public void handleLogin(ActionEvent actionEvent) {
        try{
            User user = serviceUser.getByEmail(loginEmailField.getText()).get();
            if (user.check_password(loginPasswordField.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Login successful");
                alert.setContentText("Welcome "+user.getFirstName()+" "+user.getLastName());
                alert.showAndWait();

                FXMLLoader userLoader = new FXMLLoader();
                userLoader.setLocation(getClass().getResource("views/mainpage-view.fxml"));
                AnchorPane userLayout = userLoader.load();
                Scene scene = new Scene(userLayout);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
                MainPageController startController = userLoader.getController();
                startController.setService(serviceUser, serviceFriendship, startStage, user);
                startStage.setScene(scene);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Please re-enter login details");
                alert.setContentText("Password and email do not match");
                alert.showAndWait();
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("404");
            alert.setContentText("Server error please re-enter login details");
            alert.showAndWait();
        }
    }


    public void openRegisterWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/register-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/register.css")).toExternalForm());
        startStage.setScene(scene);
    }
}
