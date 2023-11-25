package group.socialapp;

import group.socialapp.Domain.User;
import group.socialapp.GUI.UserAlert;
import group.socialapp.Service.ServiceUser;
import group.socialapp.Validators.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {

    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField textFieldPassword;

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
        if (user != null) {
            setFields(user);
        }
    }

    private void setFields(User user) {
        textFieldFirstName.setText(user.getFirstName());
        textFieldLastName.setText(user.getLastName());
        textFieldEmail.setText(user.getEmail());
    }

    @FXML
    public void handleSave() {
        String FirstName = textFieldFirstName.getText();
        String LastName = textFieldLastName.getText();
        String Email = textFieldEmail.getText();
        String Password = textFieldPassword.getText();
        if (this.user == null) {
            saveUser(FirstName, LastName, Email, Password);
        } else {
            String Id = user.getId();
            updateUser(Id, FirstName, LastName, Email, Password);
        }
    }

    private void updateUser(String id, String firstName, String lastName, String email, String password) {
        try {
            User user2 = this.serviceUser.updateAnUser(id, firstName, lastName, email, password);
            if (user2 == null)
                UserAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Update User", "User was updated");
        } catch (ValidationException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void saveUser(String firstName, String lastName, String email, String password) {
        try {
            User user2 = this.serviceUser.addOneUser(firstName, lastName, email, password);
            if (user2 == null)
                UserAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Create User", "User was created");
        } catch (ValidationException e) {
            UserAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
}
