package group.socialapp;


import group.socialapp.Domain.User;
import group.socialapp.GUI.Events.UserChangeEvent;
import group.socialapp.GUI.Observer.Observer;
import group.socialapp.GUI.UserAlert;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {

    @FXML
    private TextField searchField;
    private ServiceUser serviceUser;

    private ServiceFriendship serviceFriendship;

    private final ObservableList<User> model = FXCollections.observableArrayList();

    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;

    public void setService(ServiceUser ServiceUser, ServiceFriendship ServiceFriendship) {
        serviceUser = ServiceUser;
        serviceFriendship = ServiceFriendship;
        serviceUser.addObserver(this);
        serviceFriendship.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<User> users = serviceUser.getAll();
        List<User> UserList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(UserList);
    }

    public void handleDeleteMessage(ActionEvent actionEvent) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            User deleted = serviceFriendship.deleteUser(selected.getId()).get();
            UserAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
        } else UserAlert.showErrorMessage(null, "Nu ati selectat nici un student!");
    }

    @Override
    public void update(UserChangeEvent UserChangeEvent) {
        initModel();
    }


    @FXML
    public void handleUpdateMessage(ActionEvent ev) throws IOException {
        User selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showUserEditDialog(selected);
        } else
            UserAlert.showErrorMessage(null, "NU ati selectat nici un student");
    }

    @FXML
    public void handleAddMessage(ActionEvent ev) throws IOException {
        showUserEditDialog(null);
    }


    public void showUserEditDialog(User User) throws IOException {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/edituser-view.fxml"));
            AnchorPane root = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();

            dialogStage.setTitle("User Data");
            dialogStage.initModality(Modality.WINDOW_MODAL);
//            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
            dialogStage.setScene(scene);

            EditUserController editMessageViewController = loader.getController();
            editMessageViewController.setService(serviceUser, dialogStage, User);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSearch(KeyEvent keyEvent) {
        String searchText = searchField.getText().toLowerCase();

        if (!searchText.isEmpty()) {
            Iterable<User> users = serviceUser.filterByEmail(searchText);
            List<User> UserList = StreamSupport.stream(users.spliterator(), false)
                    .collect(Collectors.toList());
            model.setAll(UserList);
        } else {
            initModel();
        }

    }
}


