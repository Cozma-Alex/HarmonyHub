package group.socialapp;

import group.socialapp.Domain.User;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {
    public PasswordField textFieldPassword;
    public TextField textFieldEmail;
    public TextField textFieldLastName;
    public TextField textFieldFirstName;
    public PasswordField textFieldConfirmPassword;

    private Stage primaryStage;
    private ServiceUser serviceUser;

    private ServiceFriendship serviceFriendship;

    public void setService(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage primaryStage){
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
        this.primaryStage = primaryStage;
    }

    public void handleRegister(ActionEvent actionEvent) throws IOException {
        if (serviceUser.getByEmail(textFieldEmail.getText()).isPresent()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Register failed");
            alert.setContentText("Email is already used");
            alert.showAndWait();
        }
        else {
            if (!Objects.equals(textFieldPassword.getText(), textFieldConfirmPassword.getText()))  {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Register failed");
                alert.setContentText("Passwords do not match");
                alert.showAndWait();
            }
            else {
                serviceUser.addOneUser(textFieldFirstName.getText(), textFieldLastName.getText(), textFieldEmail.getText(), textFieldPassword.getText());
                User user = serviceUser.getByEmail(textFieldEmail.getText()).get();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Register successful");
                alert.setContentText("Welcome "+user.getFirstName()+" "+user.getLastName());
                alert.showAndWait();

                FXMLLoader userLoader = new FXMLLoader();
                userLoader.setLocation(getClass().getResource("views/mainpage-view.fxml"));
                AnchorPane userLayout = userLoader.load();
                Scene scene = new Scene(userLayout);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
                MainPageController startController = userLoader.getController();
                startController.setService(serviceUser, serviceFriendship, primaryStage, user);
                primaryStage.setScene(scene);
                primaryStage.setWidth(928);
            }
        }

    }

    public void handleCancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/startup-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/start.css")).toExternalForm());
        primaryStage.setScene(scene);

        StartController startController = userLoader.getController();
        startController.setData(serviceUser, serviceFriendship, primaryStage);

        primaryStage.setWidth(860);
        primaryStage.show();
    }

    public void openLoginWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
        LoginController startController = userLoader.getController();
        startController.setData(serviceUser, serviceFriendship, primaryStage);
        primaryStage.setScene(scene);
    }
}
