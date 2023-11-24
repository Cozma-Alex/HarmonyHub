package group.socialapp;

import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    public TextField loginEmailField;
    @FXML
    public PasswordField loginPasswordField;

    public void handleLogin(ActionEvent actionEvent) {

        closeLoginPage();
    }

    private void closeLoginPage() {
        //Stage stage = (Stage)
    }

    public void setService(ServiceUser serviceUser, ServiceFriendship serviceFriendship) {
    }
}
