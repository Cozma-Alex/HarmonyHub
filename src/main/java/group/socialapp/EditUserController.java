package group.socialapp;

import group.socialapp.Domain.User;
import group.socialapp.Service.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class EditUserController {

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private PasswordField textFieldConfirmPassword;
    @FXML
    private PasswordField textFieldOldPassword;

    private ServiceUser serviceUser;

    private Stage dialogStage;

    private User user;

    @FXML
    private void initialize() {
    }

    public void setService(ServiceUser serviceUser, Stage dialogStage, User user) {
        this.serviceUser = serviceUser;
        this.dialogStage = dialogStage;
        this.user = user;
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }


    public void handleUpdate(ActionEvent actionEvent) {
        if (!user.check_password(textFieldOldPassword.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Update Failed");
            alert.setHeaderText("Please re-enter user details");
            alert.setContentText("Old Password incorrect");
            alert.showAndWait();
        }
        else{
            if (!Objects.equals(textFieldPassword.getText(), textFieldConfirmPassword.getText()))  {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Update failed");
                alert.setContentText("New Passwords do not match");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Are you sure you want to delete your account?");
                alert.setContentText("Click OK to proceed or Cancel to abort");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK){

                    serviceUser.updateAnUser(user.getId(), textFieldFirstName.getText(), textFieldLastName.getText(), user.getEmail(), textFieldPassword.getText());
                    user = serviceUser.searchUserById(user.getId()).get();
                    Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert2.setTitle("Register successful");
                    alert2.setContentText("Account updated for "+user.getFirstName()+" "+user.getLastName());
                    alert2.showAndWait();
                }
            }
        }
    }
}
