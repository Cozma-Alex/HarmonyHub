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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SendRequestController implements Observer<UserChangeEvent> {

    @FXML
    private TextField searchField;
    @FXML
    TableView<User> tableView;

    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableColumn<User, String> tableColumnEmail;
    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private Stage startStage;
    private User user;
    private final ObservableList<User> model = FXCollections.observableArrayList();


    public void setService(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage mainStage, User user) {
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
        this.user = user;
        this.startStage = mainStage;
        initModel();
    }

    @Override
    public void update(UserChangeEvent event) {
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
        serviceFriendship.setUser(user);
        Iterable<User> users = serviceFriendship.filterbyRequest();
        List<User> UserList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(UserList);
    }

    public void handleSearch(KeyEvent keyEvent) {
        String searchText = searchField.getText().toLowerCase();

        if (!searchText.isEmpty()) {
            serviceFriendship.setUser(user);
            Iterable<User> users = serviceFriendship.filterByEmailAndRequest(searchText);
            List<User> UserList = StreamSupport.stream(users.spliterator(), false)
                    .collect(Collectors.toList());
            model.setAll(UserList);
        } else {
            initModel();
        }

    }

    public void handleFriendRequest(ActionEvent actionEvent) {
        User selected = tableView.getSelectionModel().getSelectedItem();
        serviceFriendship.setUser(user);
        serviceFriendship.addRequest(selected);
        initModel();
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
        LoginController startController = userLoader.getController();
        startController.setData(serviceUser, serviceFriendship, startStage);
        startStage.setScene(scene);
        startStage.setWidth(860);
    }
}
