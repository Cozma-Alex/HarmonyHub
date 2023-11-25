package group.socialapp;

import group.socialapp.Domain.User;
import group.socialapp.GUI.Events.UserChangeEvent;
import group.socialapp.GUI.Observer.Observer;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPageController implements Observer<UserChangeEvent> {

    @FXML
    private TextField searchField;
    private ServiceUser serviceUser;

    private ServiceFriendship serviceFriendship;

    private User user;

    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;

    @FXML
    Stage mainStage;

    private final ObservableList<User> model = FXCollections.observableArrayList();

    public void setService(ServiceUser ServiceUser, ServiceFriendship ServiceFriendship, Stage startStage, User user) {
        serviceUser = ServiceUser;
        serviceFriendship = ServiceFriendship;
        mainStage = startStage;
        this.user = user;
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

    @Override
    public void update(UserChangeEvent event) {
        initModel();
    }

    private void initModel() {
        ArrayList<User> users = serviceFriendship.getAllFriendsByEmail(user.getEmail());
        List<User> UserList = new ArrayList<>(users);
        model.setAll(UserList);
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

    public void handleSeeFriendRequests(ActionEvent actionEvent) {
    }

    public void handleAddFriend(ActionEvent actionEvent) {
    }

    public void handleDeleteAccount(ActionEvent actionEvent) {
    }

    public void handleUpdateAccount(ActionEvent actionEvent) {
    }
}
