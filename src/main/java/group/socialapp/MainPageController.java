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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MainPageController implements Observer<UserChangeEvent> {

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

    @FXML
    TextField searchField;

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
        ArrayList<User> users = serviceFriendship.getAllFriendsByEmail2(user.getEmail());
        List<User> UserList = new ArrayList<>(users);
        model.setAll(UserList);
    }

//    public void handleSearch(KeyEvent keyEvent) {
//        String searchText = searchField.getText().toLowerCase();
//
//        if (!searchText.isEmpty()) {
//            serviceFriendship.setUser(user);
//            Iterable<User> users = serviceFriendship.filterByEmail(searchText);
//            List<User> UserList = StreamSupport.stream(users.spliterator(), false)
//                    .collect(Collectors.toList());
//            model.setAll(UserList);
//        } else {
//            initModel();
//        }
//
//    }

    public void handleSeeFriendRequests(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/requests-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
        RequestController startController = userLoader.getController();
        startController.setService(serviceUser, serviceFriendship, mainStage, user);
        mainStage.setScene(scene);
        mainStage.setWidth(745);
    }

    public void handleAddFriend(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/send-request-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/main.css")).toExternalForm());
        SendRequestController startController = userLoader.getController();
        startController.setService(serviceUser, serviceFriendship, mainStage, user);
        mainStage.setScene(scene);
    }

    public void handleDeleteAccount(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("Click OK to proceed or Cancel to abort");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            serviceFriendship.deleteUser(user.getId());

            FXMLLoader userLoader = new FXMLLoader();
            userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
            AnchorPane userLayout = userLoader.load();
            Scene scene = new Scene(userLayout);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
            LoginController startController = userLoader.getController();
            startController.setData(serviceUser, serviceFriendship, mainStage);
            mainStage.setScene(scene);
            mainStage.setWidth(860);
        }

    }

    public void handleUpdateAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/edituser-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/edit.css")).toExternalForm());
        EditUserController startController = userLoader.getController();
        startController.setService(serviceUser, mainStage, user);
        mainStage.setScene(scene);
        mainStage.setWidth(860);
    }

    public void handleLogout(ActionEvent actionEvent) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("views/login-view.fxml"));
        AnchorPane userLayout = userLoader.load();
        Scene scene = new Scene(userLayout);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
        LoginController startController = userLoader.getController();
        startController.setData(serviceUser, serviceFriendship, mainStage);
        mainStage.setScene(scene);
        mainStage.setWidth(860);
    }
}
