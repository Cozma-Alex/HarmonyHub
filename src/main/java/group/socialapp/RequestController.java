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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestController implements Observer<UserChangeEvent> {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> tableColumnFirstName;
    @FXML
    private TableColumn<User, String> tableColumnLastName;
    @FXML
    private TableColumn<User, String> tableColumnEmail;

    private final ObservableList<User> model = FXCollections.observableArrayList();

    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private Stage startStage;
    private User user;

    public void setService(ServiceUser serviceUser, ServiceFriendship serviceFriendship, Stage mainStage, User user) {
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
        this.user = user;
        this.startStage = mainStage;
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
        serviceFriendship.setUser(user);
        Iterable<User> friendshipRequests = serviceFriendship.getALlRequests(user);
        List<User> friendshipRequestList = StreamSupport.stream(friendshipRequests.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(friendshipRequestList);
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

    public void handleAcceptRequest(ActionEvent actionEvent) {
        serviceFriendship.setUser(user);

        User selected = tableView.getSelectionModel().getSelectedItem();

        serviceFriendship.acceptRequest(user, selected);

        initModel();
    }

    public void handleDenyRequest(ActionEvent actionEvent) {
        serviceFriendship.setUser(user);

        User selected = tableView.getSelectionModel().getSelectedItem();

        serviceFriendship.denyRequest(user, selected);

        initModel();
    }
}
