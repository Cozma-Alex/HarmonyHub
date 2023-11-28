package group.socialapp;

import group.socialapp.Repository.FriendshipDBRepository;
import group.socialapp.Repository.RequestDBRepository;
import group.socialapp.Repository.UserDBRepository;
import group.socialapp.Service.ServiceFriendship;
import group.socialapp.Service.ServiceUser;
import group.socialapp.Validators.FriendshipValidator;
import group.socialapp.Validators.UserValidator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

public class StartApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        String url ="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "root";
        String password = "Goacamela4@";

        UserValidator userValidator = new UserValidator();
        UserDBRepository userDBRepository = new UserDBRepository(url,username,password);
        ServiceUser serviceUser = new ServiceUser(userDBRepository,userValidator);

        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url ,username, password);
        RequestDBRepository requestRepository = new RequestDBRepository(url ,username, password);
        ServiceFriendship serviceFriendship = new ServiceFriendship(friendshipDBRepository, userDBRepository, requestRepository, friendshipValidator);

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

    public static void main(String[] args){
        launch(args);
    }

}
